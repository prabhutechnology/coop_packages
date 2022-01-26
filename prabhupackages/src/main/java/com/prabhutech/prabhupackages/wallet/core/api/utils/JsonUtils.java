package com.prabhutech.prabhupackages.wallet.core.api.utils;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static Gson gson;
    public static JsonParser parser;

    static {
        gson = new Gson();
        parser = new JsonParser();
    }

    public static String optString(JsonElement element) {
        if (element.isJsonNull()) return null;
        return element.getAsString();
    }

    public static JsonObject toJsonObject(Object object) {
        return gson.toJsonTree(object).getAsJsonObject();
    }

    public static Object getModel(JsonObject object, Type typeOfT) {
        return gson.fromJson(object, typeOfT );
    }

    /**
     * Converts
     *
     * @return
     */
    public static JsonObject toCamelCase(JsonObject object) {
        JsonObject obj = new JsonObject();
        for (String key : object.keySet()) {
            obj.add(toCamelCase(key), object.get(key));
        }
        return obj;
    }

    public static String toCamelCase(String inputString) {
        return inputString.substring(0, 1).toLowerCase() + inputString.substring(1);
    }

    public static String safeString(JsonElement element, String orElse) {
        try {
            return element.getAsString();
        } catch (NullPointerException | UnsupportedOperationException e) {
            return orElse;
        }
    }

    public static String safeStringTypeTwo(String element, String orElse) {
        try {
            return element;
        } catch (NullPointerException | UnsupportedOperationException e) {
            return orElse;
        }
    }

    public static boolean safeBoolean(JsonElement element, boolean orElse) {
        try {
            return element.getAsBoolean();
        } catch (NullPointerException | UnsupportedOperationException e) {
            return orElse;
        }
    }

    public static int safeInt(JsonElement element, int orElse) {
        try {
            return element.getAsInt();
        } catch (NullPointerException | UnsupportedOperationException | NumberFormatException e) {
            return orElse;
        }
    }

    public static float safeFloat(JsonElement element, float orElse) {
        try {
            return element.getAsFloat();
        } catch (NullPointerException | UnsupportedOperationException | NumberFormatException e) {
            return orElse;
        }
    }

    public static JsonArray safeArray(JsonElement element) {

        try {
            JsonParser parser = new JsonParser();
            String data = element.getAsString();
            return parser.parse(data).getAsJsonArray();
        } catch (Exception e) {
            return new JsonArray();
        }

    }

    // TODO: 9/15/19 replace with a stream lib
    public static String findIn(JsonObject req, String first) {
        for (String key : req.keySet())
            if (first.equals(key))
                return req.get(key).getAsString();
        return null;
    }

    /**
     * Get only props that is available in props.
     *
     * @param props
     * @param object
     * @return
     */
    public static JsonObject filter(List<String> props, JsonObject object) {
        JsonObject res = new JsonObject();
        for (String prop : props) {
            JsonElement e = from(object, prop);
            String[] names = prop.split("\\.");
            if (e != null) res.add(names[names.length - 1], e);
        }
        return res;
    }

    /**
     * Searches deep inside an object using dot separated values.
     *
     * @param obj
     * @param path dot separated key value like "apple.red"
     * @return
     */
    public static JsonElement from(JsonObject obj, String path) throws JsonSyntaxException {
        String[] seg = path.split("\\.");
        for (String element : seg) {
            if (obj != null) {
                JsonElement ele = obj.get(element);
                if (ele == null) continue;
                if (!ele.isJsonObject())
                    return ele;
                else
                    obj = ele.getAsJsonObject();
            } else {
                return null;
            }
        }
        return null;
    }

    public static ArrayList<String> toList(JsonArray asJsonArray) {
        ArrayList<String> list = new ArrayList<>();
        for (JsonElement element : asJsonArray) {
            list.add(element.getAsString());
        }
        return list;
    }

    public static JsonArray toJsonArray(ArrayList<String> rcPinsOps) {
        JsonArray array = new JsonArray();
        for (String rcPinsOp : rcPinsOps) {
            array.add(rcPinsOp);
        }
        return array;
    }

    /**
     * For now handles only string elements.
     *
     * @param object
     * @return
     */
    public static Bundle toBundle(JsonObject object) {
        Bundle bundle = new Bundle();
        for (String key : object.keySet()) {
            bundle.putString(key, object.get(key).getAsString());
        }
        return bundle;
    }
}
