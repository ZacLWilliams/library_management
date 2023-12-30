package com.source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class createUser {
	private static final String DB_URL = "jdbc:mysql://localhost/library_db";
	private static final String USER = "root";
	private static final String PASS = "Sapiens789-";

    public static User readInput() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter username: ");
        String scan1 = reader.next();
        System.out.println("Enter password: ");
        String scan2 = reader.next();
        User user = new User(scan1, scan2);
        reader.close();
        return user;
    }
    public static int add_to_db_check() {
        User user = readInput();

        if (user.getPassword().length() < 8) {
            // 2 means password is below required length
            return 2;
        }

        ResultSet usernameResult = null;

        //Check to see if user input fulfils requirements
        String sql1 = "SELECT EXISTS (SELECT * FROM user WHERE username = ?)";
		try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		PreparedStatement statement1 = con.prepareStatement(sql1)) {

			// Set values
			statement1.setString(1, user.getUsername());

			usernameResult = statement1.executeQuery();
            usernameResult.next();
            
            if (usernameResult.getInt(1) == 1) {
                // 0 means username already exists
                return 0;
            }
            else {

            }

		} catch (SQLException e) {
			e.printStackTrace();
		}
        // 1 means username and password were successfully added to the database
        return 1;



        //String sql3 = "INSERT INTO user (username, password) VALUES (?, ?)";

		//try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		//PreparedStatement statement = con.prepareStatement(sql)) {

			// Insert values into database
		//	statement.setString(1, user.getUsername());
		//	statement.setString(2, user.getPassword());
		//	statement.executeUpdate();

		//} catch (SQLException e) {
		//	e.printStackTrace();
		//}
    }
}
