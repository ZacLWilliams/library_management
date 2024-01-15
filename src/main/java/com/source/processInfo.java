package com.source;

import org.apache.commons.lang3.StringUtils;

public class processInfo {
    public static void determineUserCreateInput(String content) {
        String data[];
        data = StringUtils.substringsBetween(content, "=", "&");
        System.out.println(data[0]);System.out.println(data[1]);System.out.println(data[2]);
        System.out.println(createUser.add_to_db_check(data[0], data[1], data[2]));
    }
    public static void determineUserLoginInput(String content) {

    }
}
