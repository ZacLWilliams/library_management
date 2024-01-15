package com.source;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class processInfo {
    public static ArrayList<String> determineUsernameInput(String content) {
        ArrayList<String> arr = new ArrayList<String>();
        String data[];
        data = StringUtils.substringsBetween(content, "=", "&");
        arr.add(data[0]); arr.add(String.valueOf(checkUser.check_db(data[0])));
        return arr;
    }
    public static boolean passwordInput(String content, String username) {
        String data[];
        data = StringUtils.substringsBetween(content, "=", "&");
        return createUser.add_to_db(username, data[0]);
    }
    public static void determineUserLoginInput(String content) {

    }
}