package com.source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//import java.util.Scanner;

public class checkBook {
	private static final String DB_URL = "jdbc:mysql://localhost/library_db";
	private static final String USER = "root";
	private static final String PASS = "Sapiens789-";

    public static int getBooks(String search) {
        String sql = "SELECT user_id FROM user WHERE username = ? AND password = ?";

        ResultSet result = null;

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		PreparedStatement statement = con.prepareStatement(sql);) {

            //statement.setString(1, username);
            //statement.setString(2, password);
            
            result = statement.executeQuery();
            result.next();
            return result.getInt("user_id");
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return 0;
    } 
    public static ArrayList<String> processWords(String search) {
        int i;
        ArrayList<String> words = new ArrayList<String>();
        while ((i = search.indexOf("+")) != -1) {
            words.add(search.substring(0, i));
            search = search.substring(i+1, search.length());
        }
        words.add(search);

        for(int z = 0; z < words.size(); z++) {
            System.out.println(words.get(z));
        }
        return words;
    } 
}
