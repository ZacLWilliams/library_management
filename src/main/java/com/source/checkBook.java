package com.source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class checkBook {
	private static final String DB_URL = "jdbc:mysql://localhost/library_db";
	private static final String USER = "root";
	private static final String PASS = "Sapiens789-";

    public static void getBooks(String search) {
        String sql = "SELECT * FROM book WHERE" + processWords(search);

        ResultSet result = null;

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		Statement statement = con.prepareStatement(sql);) {
            
            result = statement.executeQuery(sql);
            while (result.next()) {
                System.out.print("Title: " + result.getString("title"));
                System.out.println("Author: " + result.getString("author"));
            }
            //return result.getInt("user_id");
        } catch (SQLException e) {
			e.printStackTrace();
		}
        //return 0;
    } 
    public static String processWords(String search) {
        int i;
        String command = "";
        while ((i = search.indexOf("+")) != -1) {
            command = command + "(CONCAT(title, author) LIKE '%" + search.substring(0, i) + "%') AND ";
            search = search.substring(i+1, search.length());
        }
        command = command + "(CONCAT(title, author) LIKE '%" + search + "%')";
        return command;
    } 
}
