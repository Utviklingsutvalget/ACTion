package controllers;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.json.JsonObject;
import com.restfb.types.Event;
import com.restfb.types.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import utils.FileUtility;
import views.html.error;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class FacebookApp extends Controller {

    /**Secrets*/
    private static final Map<String, String> CONF = FileUtility.getMap("secrets/facebookoauth", "=");

    public static Result authenticate() {

        //Redirect to google
        return redirect("https://www.facebook.com/dialog/oauth?" +
                "client_id=" + CONF.get("client_id") +
                "&redirect_uri=http://localhost:9000/facebook/callbackhandler" +
                "&scope=rsvp_event");

    }

    public static Result exchange() {

        String code = request().getQueryString("code");

        try {
            URL url = new URL("https://graph.facebook.com/oauth/access_token?");

            //!HTTPS!
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            String params = "code=" + code +
                    "&client_id=" + CONF.get("client_id") +
                    "&client_secret=" + CONF.get("client_secret") +
                    "&redirect_uri=http://localhost:9000/facebook/callbackhandler";

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

    public static Result obtainUserInformation(String response) {

        String accessToken = response.split("&")[0].split("=")[1];

        FacebookClient client = new DefaultFacebookClient(accessToken);
        com.restfb.types.User user = client.fetchObject("601916376573321", com.restfb.types.User.class);

        Connection<Event> connection = client.fetchConnection("search/events", Event.class);

        List<Event> events = connection.getData();

        Logger.debug("Going trough events, size: " + connection.getData().size());
        for(Event event : events) {

            Logger.debug(event.getName());
        }

        String fql = "SELECT daily_active_users, weekly_active_users, monthly_active_users FROM application WHERE app_id = 601916376573321";


        return ok(error.render(user.getName()));
    }
}
