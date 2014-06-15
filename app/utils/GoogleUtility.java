package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Morten on 14.06.2014.
 */
public class GoogleUtility {

    /**Issuer*/
    public static final String ISSUER = "issuer";

    /**Endpoints*/
    public static final String AUTHORIZATION_ENDPOINT = "authorization_endpoint";
    public static final String TOKEN_ENDPOINT = "token_endpoint";
    public static final String USER_INFO_ENDPOINT = "userinfo_endpoint";
    public static final String REVOCATION_ENDPOINT = "revocation_endpoint";
    public static final String JWKS_URI = "jwks_uri";

    /**Response types supported*/
    public static final String RESPONSE_TYPES_SUPPORTED = "response_types_supported";
    public static final int CODE = 0;
    public static final int TOKEN = 1;
    public static final int ID_TOKEN = 2;
    public static final int CODE_TOKEN = 3;
    public static final int CODE_ID_TOKEN = 4;
    public static final int TOKEN_ID_TOKEN = 5;
    public static final int CODE_TOKEN_ID_TOKEN = 6;

    /**Subject types supported*/
    public static final String SUBJECT_TYPES_SUPPORTED = "response_types_supported";
    public static final int PUBLIC = 0;

    /**Id token alg values supported*/
    public static final String ID_TOKEN_ALG_VALUES_SUPPORTED = "id_token_alg_values_supported";
    public static final int RS256 = 0;

    /**Token endpoint auth methods supported*/
    public static final String TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED = "token_endpoint_auth_methods_supported";
    public static final int CLIENT_SECRET_POST = 0;

    public static class DiscoveryDocument {

        private JSONObject issuer = new JSONObject();

        private JSONObject endpoints = new JSONObject();

        private JSONArray responseTypes;
        private JSONArray subjectTypesSupported;
        private JSONArray idTokenAlgValuesSupported;
        private JSONArray tokenEndpointAuthMethodsSupported;

        public DiscoveryDocument() throws MalformedURLException,IOException {

            JSONObject document = loadDiscoveryDocument();

            issuer.put(ISSUER, document.getString(ISSUER));

            endpoints.put(AUTHORIZATION_ENDPOINT, document.getString(AUTHORIZATION_ENDPOINT));
            endpoints.put(TOKEN_ENDPOINT, document.getString(TOKEN_ENDPOINT));
            endpoints.put(USER_INFO_ENDPOINT, document.getString(USER_INFO_ENDPOINT));
            endpoints.put(REVOCATION_ENDPOINT, document.getString(REVOCATION_ENDPOINT));
            endpoints.put(JWKS_URI, document.getString(JWKS_URI));

            responseTypes = document.getJSONArray(RESPONSE_TYPES_SUPPORTED);
            subjectTypesSupported = document.getJSONArray(SUBJECT_TYPES_SUPPORTED);
            idTokenAlgValuesSupported = document.getJSONArray(ID_TOKEN_ALG_VALUES_SUPPORTED);
            tokenEndpointAuthMethodsSupported = document.getJSONArray(TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED);
        }

        public String getEndpoints(String key) {return endpoints.getString(key);}

        public String getResponseTypes(int index) {return responseTypes.getString(index);}

        public String getSubjectTypesSupported(int index) {return subjectTypesSupported.getString(index);}

        public String getIdTokenAlgValuesSupported(int index) {return idTokenAlgValuesSupported.getString(index);}

        public String getTokenEndpointAuthMethodsSupported(int index) {return tokenEndpointAuthMethodsSupported.getString(index);}


        public JSONObject loadDiscoveryDocument() throws MalformedURLException,IOException {

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
        }
    }
}
