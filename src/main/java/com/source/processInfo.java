package com.source;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class processInfo {
    public static boolean dataInput(String content, String username) {
        String data[];
        data = StringUtils.substringsBetween(content, "=", "&");
        return createUser.add_to_db(username, data[0]);
    }
    public static ArrayList<String> processData(String content) {
        ArrayList<String> arr = new ArrayList<String>();
        String data[];
        data = StringUtils.substringsBetween(content, "=", "&");
        arr.add(data[0]);arr.add(data[1]);arr.add(data[2]);
        return arr;
    }
    public static void determineUserLoginInput(String content) {

    }
}