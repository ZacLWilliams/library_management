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
}
