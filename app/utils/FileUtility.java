package utils;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.File;
import java.net.URI;

import play.Play;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Morten on 12.06.2014.
 */
public class FileUtility {

    public static Map<String, String> getMap(String path, String delimiter) {

        Map<String, String> map = new HashMap<>();

        try {

            URI uri = Play.application().resource(path).toURI();

            File file = new File(uri);

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

                String line;
                while((line = bufferedReader.readLine()) != null) {

                    String[] pair = line.split(delimiter);

                    if (ArrayUtils.isNotEmpty(pair)) {

                        String key = pair[0];
                        String value = pair[1];

                        map.put(key, value);
                    }
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch(URISyntaxException e) {
            e.printStackTrace();
        }

        return map;
    }
}
