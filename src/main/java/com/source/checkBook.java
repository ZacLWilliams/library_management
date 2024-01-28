package com.source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class checkBook {
	private static final String DB_URL = "jdbc:mysql://localhost/library_db";
	private static final String USER = "root";
	private static final String PASS = "Sapiens789-";

    public static void getBooks(String search) {
        ArrayList<fullBook> bookList = new ArrayList<fullBook>();
        String sql = "SELECT * FROM book WHERE" + processWords(search);
        String sql2 = "SELECT * FROM images WHERE isbn = ?";

        ResultSet rs = null;
        ResultSet resultImages = null;

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		Statement statement = con.prepareStatement(sql); PreparedStatement statement2 = con.prepareStatement(sql2);) {
            
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getString("isbn"));
                statement2.setString(1, rs.getString("isbn"));
                resultImages = statement2.executeQuery();
                resultImages.next();
                System.out.println(resultImages.getString("image_s"));

                String[] images = new String[] {resultImages.getString("image_s"), resultImages.getString("image_m"),
                resultImages.getString("image_l")};

                fullBook b = new fullBook(rs.getString("isbn"), rs.getString("gen_rating"), rs.getString("title"),
                rs.getString("author"), rs.getString("year"), rs.getString("publisher"), images);

                bookList.add(b);
            }
            System.out.println(bookList.get(0).getYear());

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
