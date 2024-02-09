package com.source;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class checkBook {
	private static final String DB_URL = "jdbc:mysql://localhost/library_db";
	private static final String USER = "root";
	private static final String PASS = "";

    public static ArrayList<fullBook> getBooks(String search) {
        ArrayList<fullBook> bookList = new ArrayList<fullBook>();
        String sql = "SELECT * FROM book WHERE" + processWords(search);
        String sql2 = "SELECT * FROM images WHERE isbn = ?";

        ResultSet rs = null;
        ResultSet resultImages = null;

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		Statement statement = con.prepareStatement(sql); PreparedStatement statement2 = con.prepareStatement(sql2);) {
            
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                statement2.setString(1, rs.getString("isbn"));
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

        Collections.sort(bookList, new customerComparator());
        return bookList;
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

    public static fullBook checkIsbn(String userSearch) {
        String sql = "SELECT EXISTS(SELECT * FROM book WHERE isbn = ?)";
        String sql1 = "SELECT * FROM book WHERE isbn = ?";
        String sql2 = "SELECT * FROM images WHERE isbn = ?";
        ResultSet check = null;
        ResultSet rs = null;
        ResultSet resultImages = null;

	    try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement statement = con.prepareStatement(sql);
	    PreparedStatement statement1 = con.prepareStatement(sql1); PreparedStatement statement2 = con.prepareStatement(sql2);) {

            statement.setString(1, userSearch);
            check = statement.executeQuery();
            check.next();

            if ((check.getInt(1)) < 1) {
                return null;
            }

		    statement1.setString(1, userSearch);
            statement2.setString(1, userSearch);

		    rs = statement1.executeQuery();
            resultImages = statement2.executeQuery();

            rs.next();
            resultImages.next();

            String[] images = new String[] {resultImages.getString("image_s"), resultImages.getString("image_m"),
            resultImages.getString("image_l")};

            // Found isbn
            return new fullBook(rs.getString("isbn"), rs.getString("gen_rating"), rs.getString("title"),
            rs.getString("author"), rs.getString("year"), rs.getString("publisher"), images);

	    } catch (SQLException e) {
            System.out.println("TESTRS");
		    e.printStackTrace();
	    }
        // Did not find isbn
        return null;
    }

    public static void removeBook(String id, String isbn) {
        System.out.println(id);
        System.out.println(isbn);
        String sql = "DELETE FROM library WHERE user_id = ? AND isbn = ?";
        //String sql2 = "INSERT INTO library (user_id, isbn) VALUES (?, ?)";
        //PreparedStatement statement2 = con.prepareStatement(sql2);
        //statement2.setString(1, id);
        //statement2.setString(2, isbn);
        //statement2.executeQuery(sql2);

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		PreparedStatement statement = con.prepareStatement(sql);) {
            
            statement.setString(1, id);
            statement.setString(2, isbn);
            
            statement.executeUpdate();
        } catch (SQLException e) {
			e.printStackTrace();
		}
    }

    public static boolean checkLibrary(String isbn, String id) {
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
}

final class customerComparator implements Comparator<fullBook> {
    @Override
    public int compare(fullBook b1, fullBook b2) {
        return b1.getYear().compareTo(b2.getYear());
    }
}