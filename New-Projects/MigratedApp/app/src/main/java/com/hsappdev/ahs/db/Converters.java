package com.hsappdev.ahs.db;

import androidx.room.TypeConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Converts String array data types for use with room database
 */
public class Converters {
    private static final String JSON_str = "iPaths";

    @TypeConverter
    public static String[] fromString(String string) {
        JSONObject json = null;
        try {
            json = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = json.optJSONArray(JSON_str);
        String[] strArr = new String[jsonArray.length()];
        for(int i = 0; i < jsonArray.length(); i++)
            strArr[i] = jsonArray.optString(i);

        return strArr;
    }

    @TypeConverter
    public static List<String> fromStringList(String string) {
        return new ArrayList<>(Arrays.asList(fromString(string))); // Arrays.asList returns a FIXED list, it needs to be mutable
    }

    @TypeConverter
    public static String fromArray(String[] array) {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray(new ArrayList<>(Arrays.asList(array)));
        try {
            json.put(JSON_str,jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @TypeConverter
    public static String fromArrayList(List<String> array) {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray(array);
        try {
            json.put(JSON_str,jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
