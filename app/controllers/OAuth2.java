package controllers;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import models.User;
import org.json.JSONObject;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import utils.FileUtility;
import utils.GoogleUtility;
import views.html.error;
import views.html.index;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.security.GeneralSecurityException;
import java.util.*;
import java.security.NoSuchAlgorithmException;

/**
 * https://developers.google.com/accounts/docs/OAuth2Login
 *
 * For debugging purposes use: OAuth 2.0 Playground
 * https://developers.google.com/oauthplayground/?code=4/Yru2i1Y0AV8KWhteXZnUNppfLMTj.Mv2PwjP5CW0XPvB8fYmgkJyr8mc3jQI
 *
 */
public class OAuth2 extends Controller {

    /**Secrets*/
    private static final Map<String, String> CONF = FileUtility.getMap("secrets/googleoauth", "=");

    /**Endpoints for authenticating users, and for requesting resources including tokens, user information, and public keys.*/
    public static GoogleUtility.DiscoveryDocument dd;

    /**A session lasts 2 hours*/
    private static final long EXPIRATION_TIME_IN_SECONDS = 2 * 60 * 60;


    public static Result login() {
        return ok(views.html.login.index.render());
    }


    /**
     * Redirects the user to https://accounts.google.com/o/oauth2/auth with a
     * specific query for authentication.
     *
     * @see     https://developers.google.com/accounts/docs/OAuth2Login#sendauthrequest
     *
     * @return  Result
     */
    public static Result authenticate() {

        if(session().containsKey("id")) {
            User user = User.findById(session("id"));
            if(user != null)
                return ok(index.render("Already logged in " + User.findById(session("id")).firstName));

            destroySessions();
        }

        //The discovery document
        if(dd == null) {
               try {
                   dd = new GoogleUtility.DiscoveryDocument();
               } catch(MalformedURLException e) {return notFound(error.render(e.getMessage()));
               } catch(IOException e1) {return notFound(error.render(e1.getMessage()));}
        }

        try {
            //Create an anti-forgery state token
            createStateToken();

            //Redirect to google
            return redirect(dd.getEndpoints(GoogleUtility.AUTHORIZATION_ENDPOINT) + "?" +
                    "client_id=" + CONF.get("client_id") +
                    "&response_type=" + dd.getResponseTypes(GoogleUtility.CODE) +
                    "&scope=openid profile email" +
                    "&redirect_uri=http://localhost:9000/login/oauth2callback" +
                    //"&hd=student.westerdals.no" + //This line cannot be used until all students have this email
                    "&access_type=online" + //We dont need offline access right now
                    "&state=" + session("state"));

        }catch (NoSuchAlgorithmException e) {return internalServerError(error.render(e.getMessage()));}
    }

    /**
     * User redirected from google with a code that can be
     * exchanged for an access_token and id_token. The get query also contains
     * a state(token) that should be validated.
     *
     * @see     https://developers.google.com/accounts/docs/OAuth2Login#exchangecode
     *
     * @return  Result
     */
    public static Result exchange() {

        String code = request().getQueryString("code");
        String state = request().getQueryString("state");

        //Confirm anti-forgery state token
        if(!state.equals(session("state")) || code == null)
            return unauthorized(error.render("Unauthorized Access"));

        try {
            URL url = new URL(dd.getEndpoints(GoogleUtility.TOKEN_ENDPOINT) + "?");

            //!HTTPS!
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            String params = "code=" + code +
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

            //handles the parsing of the string that contains a JSONObject
            //and return the Result
            return obtainUserInformation(response.toString());

        } catch(MalformedURLException e) {return badRequest(error.render(e.getMessage()));
        } catch(ProtocolException e1) {return badRequest(error.render(e1.getMessage()));
        } catch(IOException e2) {return badRequest(error.render(e2.getMessage()));}
    }

    /**
     * If the application already has the user in the db, we only need,
     * the id_token to identify the user.
     *
     * Else, if the user does not exist we need the access_token that can
     * be exchanged for a user profile using the OpenId endpoint.
     *
     * @see     https://developers.google.com/accounts/docs/OAuth2Login#obtainuserinfo
     *
     * @return  Result
     */
    public static Result obtainUserInformation(String jwt) {

        //Key value use of the Json Web Token
        JSONObject jObject = new JSONObject(jwt);
        try {

            JsonFactory factory = new JacksonFactory();

            GoogleIdToken idToken = GoogleIdToken.parse(factory, jObject.getString("id_token"));
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), factory).build();

            //Verifies the id token and handles the signing of Google's certificates
            if(!verifier.verify(idToken))
                return unauthorized(error.render("Unauthorized Access"));

            //An id token's payload
            IdToken.Payload payload = idToken.getPayload();

            //Make sure Google issued this
            if(!payload.getIssuer().equals(dd.getIssuer(GoogleUtility.ISSUER)))
                return unauthorized(error.render("Unauthorized Access"));

            //Identifies the audience that this ID token is intended for.
            if(!payload.getAudience().equals(CONF.get("client_id")))
                return unauthorized(error.render("Unauthorized Access"));

            //Email must be verified
            if(!idToken.getPayload().getEmailVerified())
                return unauthorized(error.render("Please verify your Google email and try again"));

            //If user exists we dont need to use OpenId Connect
            if(User.exists(payload.getSubject())) {

                //Create the necessary sessionsd
                createSessions(payload.getSubject());
                return ok(index.render("Welcome back " + User.findById(payload.getSubject()).firstName));
            }

            //We need to OpenIdConnect to get email and profile information
            return getUserProfileInformation(jObject.get("access_token").toString());

        } catch(UnsupportedEncodingException e) {return badRequest(error.render(e.getMessage()));
        } catch (GeneralSecurityException e) {return badRequest(error.render(e.getMessage()));
        } catch (IOException e) {return badRequest(error.render(e.getMessage()));}
    }

    /**
    * The user does not exist we use the access_token that can
    * be exchanged for a user profile using the OpenId endpoint.
    *
     * @see     https://developers.google.com/accounts/docs/OAuth2Login#authuser
     *
    * @return   Result
    */
    public static Result getUserProfileInformation(String accessToken) {

        try {
            URL url = new URL(dd.getEndpoints(GoogleUtility.USER_INFO_ENDPOINT));

            //!HTTPS!
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            //Request header
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-length", "0");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            //Read
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            //Key value use of the profile information
            JSONObject jsonObject = new JSONObject(response.toString());

            //Create a new user
            User user = new User(
                    jsonObject.getString("sub"),
                    jsonObject.getString("given_name"),
                    jsonObject.getString("family_name"),
                    User.Gender.valueOf(jsonObject.getString("gender").toUpperCase()),
                    jsonObject.getString("email"),
                    jsonObject.getString("picture").split("\\?")[0]); //We dont want: ?sz=50(sets image size to 50)

            /*I fadderuka har ikke førsteklassingene fått noen egen epost konto fra skolen
            Checks that this users name is not already in the db, preventing users from registering with
            every single account they have.*/
            if(User.findByName(user.firstName, user.lastName) != null)
                return badRequest(error.render("Dine opplysninger finnes allerede i databasen. De som har likt fornavn og etternavn " +
                        "må kontakte admin for registrering. Dette sikkerhets tiltaket vil forsvinne når fadderuken er over og alle har fått egen epost " +
                        "konto fra skolen."));

            return Registration.autofill(user);

        } catch(MalformedURLException e) {return badRequest(error.render("Malformed URL: " + e.getMessage()));
        } catch(ProtocolException e) {return badRequest(error.render("ProtocolException: " + e.getMessage()));
        } catch(IOException e) {return badRequest(error.render("IOException: " + e.getMessage()));}
    }

    /**
     * Creates an anti-forgery state token. This round-trip verification helps
     * to ensure that the user, not a malicious script, is making the request.
     *
     * @see     https://developers.google.com/accounts/docs/OAuth2Login#createxsrftoken
     */
    public static void createStateToken() throws NoSuchAlgorithmException {

        //Only happens if a session is not set
        if (!session().containsValue("state")) {
            session("state", UUID.randomUUID().toString());
        }
    }

    /**
     * Creates sessions that the application can use.
     * The id is corresponding to the subjects id, and
     * expires is managing how long a one time session will
     * last.
     */
    public static void createSessions(String id) {

        long expires = System.currentTimeMillis() + (EXPIRATION_TIME_IN_SECONDS * 1000);

        //These probably need some security messures
        session("id", id);
        session("expires", String.valueOf(expires));
        session().remove("state");
    }

    /**
     * Destroys the sessions when a user logs out or
     * is deleted from the db or revokes access for
     * this application.
     *
     * @return   Result
     */
    public static void destroySessions() {
        session().clear();
    }

}
