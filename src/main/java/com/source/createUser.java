package com.source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.Scanner;

public class createUser {
	private static final String DB_URL = "jdbc:mysql://localhost/library_db";
	private static final String USER = "root";
	private static final String PASS = "";

    public static boolean add_to_db(String username, String pass) {
        // true means username and password were successfully added to the database
        // false means username already exists
        User user = new User(username, pass);

        //Check to see if user input fulfils requirements
        String sql2 = "INSERT INTO user (username, password) VALUES (?, ?)";
		try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement statement2 = con.prepareStatement(sql2)) {
            
            statement2.setString(1, user.getUsername());
            statement2.setString(2, user.getPassword());
            statement2.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
        // true means username and password were successfully added to the database
        return true;
    }
}
