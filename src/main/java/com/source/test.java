package com.source;

import java.util.ArrayList;


public class test {
    public static void main(String[] args) {
        String search = "test+testtest+test1";
        int i;
        ArrayList<String> words = new ArrayList<String>();
        //System.out.println(i = search.indexOf("+"));
        while ((i = search.indexOf("+")) != -1) {
            words.add(search.substring(0, i));
            search = search.substring(i+1, search.length());
        }
        words.add(search);
        for(int z = 0; z < words.size(); z++) {
            System.out.println(words.get(z));
        }
    }
}
