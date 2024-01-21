package com.source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.Scanner;

public class checkUser {
	private static final String DB_URL = "jdbc:mysql://localhost/library_db";
	private static final String USER = "root";
	private static final String PASS = "";

    public static boolean check_db(String username) {
        // true means the username is available
        // false means username already exists

        ResultSet usernameResult = null;

        //Check to see if user input fulfils requirements
        String sql1 = "SELECT EXISTS (SELECT * FROM user WHERE username = ?)";
		try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		PreparedStatement statement1 = con.prepareStatement(sql1);) {

			statement1.setString(1, username);

			usernameResult = statement1.executeQuery();
            usernameResult.next();
            
            if (usernameResult.getInt(1) == 1) {
                // false means username already exists
                return false;
            } 
		} catch (SQLException e) {
			e.printStackTrace();
		}
        // true means the username is available
        return true;
    }

    // Static polymorphic method that adds data to database
    public static void check_db(String username, String pass) {
        User user = new User(username, pass);

        // Insert username and password into database
        String sql2 = "INSERT INTO user (username, password) VALUES (?, ?)";
		try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement statement2 = con.prepareStatement(sql2)) {
            
            // Set string and execute sql command
            statement2.setString(1, user.getUsername());
            statement2.setString(2, user.getPassword());
            statement2.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
