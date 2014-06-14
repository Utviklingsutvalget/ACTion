package controllers;

import models.User;
import org.json.*;
import play.mvc.*;
import utils.FileUtility;

import java.io.IOException;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;

import views.html.debug;

/**
 * Created by Morten on 13.06.2014.
 *
 * OAuth2
 */
public class OAuth2 extends Controller {

    private static final Map<String, String> CONF = FileUtility.getMap("secrets/googleoauth", "=");

    /**
     * Redirects the user to https://accounts.google.com/o/oauth2/auth with a
     * specific query for authentication.
     *
     * @return redirect
     */
    public static Result authenticate() {

        return redirect("https://accounts.google.com/o/oauth2/auth?" +
                "client_id=" + CONF.get("client_id") +
                "&response_type=code" +
                "&scope=openid profile" +
                "&redirect_uri=http://localhost:9000/login/oauth2callback" +
                "&state=" + getStateToken());
    }

    /**
     * User redirected from google with a code that can be
     * exchanged for an access_token and id_token. The get query also contains
     * a state(token) that should be validated.
     *
     * @return
     */
    public static Result exchange() {

        try {

            URL url = new URL("https://accounts.google.com/o/oauth2/token?");

            //!HTTPS!
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            String params = "code=" + request().getQueryString("code") +
                    "&client_id=" + CONF.get("client_id") +
                    "&client_secret=" + CONF.get("client_secret") +
                    "&redirect_uri=http://localhost:9000/login/oauth2callback" +
                    "&grant_type=authorization_code";

            //Request header
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            byte[] bytes = params.getBytes("UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));

            //Send post request
            connection.setDoOutput(true);
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.writeBytes(params);
            dos.flush();
            dos.close();

            //Read
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            //handles the parsing of the string that is containing a JsonArray
            //and return the Result
            return obtainUserInformation(response.toString());

        } catch(MalformedURLException e) {return badRequest(debug.render("Malformed URL: " + e.getMessage()));
        } catch(ProtocolException e1) {return badRequest(debug.render("ProtocolException: " + e1.getMessage()));
        } catch(IOException e2) {return badRequest(debug.render("IOException: " + e2.getMessage()));}
    }

    public static Result obtainUserInformation(String response) {

        JSONObject jObject = new JSONObject(response);

        //There are 3 portions in the id token:
        //header, payload and signature
        String[] portions = jObject.get("id_token").toString().split("\\.");

        byte[] decodedHeader = java.util.Base64.getDecoder().decode(portions[0]);
        byte[] decodedClaims = java.util.Base64.getDecoder().decode(portions[1]);

        try {

            JSONObject hedaer = new JSONObject(new String(decodedHeader, "UTF-8"));
            JSONObject claims = new JSONObject(new String(decodedClaims, "UTF-8"));

            //Identifies the audience that this ID token is intended for.
            //It must be one of the OAuth 2.0 client IDs of your application.
            if(!claims.get("aud").toString().equals(CONF.get("client_id")))
                return badRequest(debug.render("id mismatch"));

            String subId = claims.get("sub").toString();
            User user = User.find.where().eq("id", subId).findUnique();

            if(user == null)
                return getUserProfileInformation(claims, jObject.get("access_token").toString());

            return ok(debug.render("Login User: " + user.name + ", " + user.id));

        } catch(UnsupportedEncodingException e) {
        }


        return ok(debug.render("Length: " +portions.length));
    }

    public static Result getUserProfileInformation(JSONObject claims, String accessToken) {

        try {

            String subId = claims.get("sub").toString();
            URL url = new URL("https://www.googleapis.com/plus/v1/people/" + subId + "?");

            //!HTTPS!
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            String params = "fields=aboutMe" +
                    "&key=" + accessToken;

            //Request header
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", claims.get("at_hash").toString());
            byte[] bytes = params.getBytes("UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));

            //Send post request
            connection.setDoOutput(true);
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.writeBytes(params);
            dos.flush();
            dos.close();

            //Read
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return ok(debug.render(response.toString()));

        } catch(MalformedURLException e) {return badRequest(debug.render("Malformed URL: " + e.getMessage()));
        } catch(ProtocolException e1) {return badRequest(debug.render("ProtocolException: " + e1.getMessage()));
        } catch(IOException e2) {return badRequest(debug.render("IOException: " + e2.getMessage()));}
    }

    /**
     * Creates an anti-forgery state token. This round-trip verification helps
     * to ensure that the user, not a malicious script, is making the request.
     *
     * @return state_token
     */
    public static String getStateToken() {

        if (!session().containsValue("token")) {

            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(UUID.randomUUID().toString().getBytes());
                session("token", new String(messageDigest.digest()));

            } catch(NoSuchAlgorithmException e) {

                //Handle with another algorithm
                return "";
            }
        }
        return session("token");
    }
}
