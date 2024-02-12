package com.source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class checkLibrary {
    private static final String DB_URL = "jdbc:mysql://localhost/library_db";
	private static final String USER = "root";
	private static final String PASS = "Sapiens789-";

    public static void removeBook(String id, String isbn) {
        String sql = "DELETE FROM library WHERE user_id = ? AND isbn = ?";

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		PreparedStatement statement = con.prepareStatement(sql);) {
            
            statement.setString(1, id);
            statement.setString(2, isbn);
            
            statement.executeUpdate();
        } catch (SQLException e) {
			e.printStackTrace();
		}
    }

    public static void addBook(String id, String isbn) {
        String sql = "INSERT INTO library (user_id, isbn) VALUES (?, ?)";

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		PreparedStatement statement = con.prepareStatement(sql);) {
            
            statement.setString(1, id);
            statement.setString(2, isbn);
            
            statement.executeUpdate();
        } catch (SQLException e) {
			e.printStackTrace();
		}
    }

    public static boolean checkUserLibrary(String isbn, String id) {
        String sql = "SELECT EXISTS (SELECT * FROM library WHERE user_id = ? AND isbn = ?)";
        //String sql2 = "INSERT INTO library (user_id, isbn) VALUES (?, ?)";
        //PreparedStatement statement2 = con.prepareStatement(sql2);
        //statement2.setString(1, id);
        //statement2.setString(2, isbn);
        //statement2.executeQuery(sql2);
        ResultSet rs = null;

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		PreparedStatement statement = con.prepareStatement(sql);) {
            
            statement.setString(1, id);
            statement.setString(2, isbn);

            rs = statement.executeQuery();
            rs.next();

            // Check if user has that book in library
            if((rs.getInt(1)) > 0) {
                return true;
            }
            
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return false;
    }

    public static ArrayList<fullBook> getBooks(String user_id) {
        ArrayList<fullBook> bookList = new ArrayList<fullBook>();
        String sql = "SELECT * FROM library WHERE user_id = ?";
        String sql1 = "SELECT * FROM book WHERE isbn = ?";
        String sql2 = "SELECT * FROM images WHERE isbn = ?";

        ResultSet rs = null;
        ResultSet result = null;
        ResultSet resultImages = null;

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		PreparedStatement statement = con.prepareStatement(sql); PreparedStatement statement2 = con.prepareStatement(sql2);
        PreparedStatement statement1 = con.prepareStatement(sql1);) {
            
            statement.setString(1, user_id);
            result = statement.executeQuery();

            while (result.next()) {
                statement1.setString(1, result.getString("isbn"));
                statement2.setString(1, result.getString("isbn"));
                rs = statement1.executeQuery();
                rs.next();
                resultImages = statement2.executeQuery();
                resultImages.next();

                String[] images = new String[] {resultImages.getString("image_s"), resultImages.getString("image_m"),
                resultImages.getString("image_l")};

                fullBook b = new fullBook(rs.getString("isbn"), rs.getString("gen_rating"), rs.getString("title"),
                rs.getString("author"), rs.getString("year"), rs.getString("publisher"), images);

                bookList.add(b);
            }

            //return result.getInt("user_id");
        } catch (SQLException e) {
			e.printStackTrace();
		}

        //Collections.sort(bookList, new customerComparator());
        return bookList;
    }
}
