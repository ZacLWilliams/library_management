package com.source;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class processInfo {
    public static ArrayList<String> determineUserCreateInput(String content) {
        ArrayList<String> arr = new ArrayList<String>();
        String data[];
        data = StringUtils.substringsBetween(content, "=", "&");
        //System.out.println(data[0]);System.out.println(data[1]);System.out.println(data[2]);
        arr.add(data[0]); arr.add(data[1]);arr.add(data[2]);
        arr.add(String.valueOf(createUser.add_to_db_check(data[0], data[1], data[2])));
        return arr;
    }
    public static void determineUserLoginInput(String content) {

    }
}
