package controllers;

import org.apache.commons.lang3.ArrayUtils;
import play.libs.oauth.OAuth;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Sessions extends Controller {
    
    public static Result index() {
        if(verifySession()) {
            return redirect((play.mvc.Call) Application.index());
        } else {
            return ok(views.html.index.render());
        }

    }

    public static Result login() {
        final String requestUrl = "https://accounts.google.com/o/oauth2/auth";
        final String tokenUrl = "https://accounts.google.com/o/oauth2/auth";
        final String filePath = "secrets/googleoauth";
        final String callbackUrl = "https://action.cadence.singles/login";

        Map<String, String> oauthConfig = getOauthConfig(filePath);

        String clientId = oauthConfig.get("clientId");
        String clientSecret = oauthConfig.get("clientSecret");
        OAuth.ConsumerKey consumerKey = new OAuth.ConsumerKey(clientId, clientSecret);

        OAuth.ServiceInfo info = new OAuth.ServiceInfo(tokenUrl, callbackUrl, requestUrl, consumerKey);

        OAuth oAuth = new OAuth(info);

        return redirect(tokenUrl);
    }

    private static Map<String, String> getOauthConfig(String fileName) {
        File file = new File(fileName);

        Map<String, String> oauthConfig = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                String[] pair = line.split("=");
                if (ArrayUtils.isNotEmpty(pair)) {
                    String key = pair[0];
                    String value = pair[1];

                    oauthConfig.put(key, value);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return oauthConfig;
    }

    public static void callBack() {

    }


    private static boolean verifySession() {
        if(session().containsKey("requestToken")) {
            // TODO Verify session
        }
        return false;
    }

}
