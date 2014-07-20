package utils;

import java.util.ArrayList;
import java.util.List;

public class Model {

    public static List<String> getEnumAsList(Enum<?> _enum) {

        ArrayList<String> list = new ArrayList<>();

        for(Enum<?> key : _enum.getDeclaringClass().getEnumConstants()) { list.add(key.name());}
        return list;
    }
}
