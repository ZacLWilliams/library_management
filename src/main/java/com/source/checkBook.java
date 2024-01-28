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
	private static final String PASS = "";

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
    public static String processWords(String search) {
        int i;
        String command = "";
        while ((i = search.indexOf("+")) != -1) {
            command = command + "(CONCAT(title, author) LIKE '%" + search.substring(0, i) + "%') AND ";
            search = search.substring(i+1, search.length());
        }
        command = command + "(CONCAT(title, author) LIKE '%" + search + "%')";
        System.out.println(command);
        return command;
    } 
    public static ArrayList<String> temp(String search) {
        int i;
        ArrayList<String> words = new ArrayList<String>();
        while ((i = search.indexOf("+")) != -1) {
            words.add(search.substring(0, i));
            search = search.substring(i+1, search.length());
        }
        words.add(search);
        return words;
    } 
}
