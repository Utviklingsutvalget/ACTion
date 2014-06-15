package utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.*;
/**
 * Created by Morten on 14.06.2014.
 */
public class GoogleUtility {

    public static JSONObject loadDiscoveryDocument() {

        try {

            URL url = new URL("https://accounts.google.com/.well-known/openid-configuration");

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));

            String input;
            StringBuffer response = new StringBuffer();

            while((input = br.readLine()) != null) {

                response.append(input);
            }

            return new JSONObject(response.toString().trim());


        } catch(MalformedURLException e) {
        } catch(IOException e1) {}

        return null;
    }
}
