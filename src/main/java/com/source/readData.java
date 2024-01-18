package com.source;

import java.io.FileReader;
import java.util.List; 
import com.opencsv.*; 
//import com.mysql.*;
import java.sql.*;

import java.util.ArrayList;

public class readData {
	private static ArrayList<Book> bookList = new ArrayList<Book>();
	private static final String DB_URL = "jdbc:mysql://localhost/library_db";
	private static final String USER = "root";
	private static final String PASS = "";

	public static void main(String[] args) {
    	String file = "books.csv";
		readCSV(file);
		add_to_db(bookList);
		add_images_to_db(bookList);
	}

	public static void readCSV(String file) { 
		try { 
			// Create an object of file reader 
			// class with CSV file as a parameter. 
			FileReader filereader = new FileReader(file); 
			
			CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

			CSVReader csvReader = new CSVReaderBuilder(filereader) 
											.withSkipLines(1)
											.withCSVParser(parser) 
											.build(); 
			
			List<String[]> allData = csvReader.readAll();

			for (String[] tempBook : allData) {  

				String[] tempUrls;
				tempUrls = new String[] {tempBook[5], tempBook[6], tempBook[7]};

				Book b = new Book(tempBook[0], tempBook[1], tempBook[2], tempBook[3], tempBook[4], tempUrls);
					
				bookList.add(b);
			}
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
	}

	public static void add_to_db(ArrayList<Book> bookList) {

		String sql = "INSERT IGNORE INTO book VALUES (?, NULL, ?, ?, ?, ?)";
		String temp;

		try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		PreparedStatement statement = con.prepareStatement(sql)) {
			int count = 0;
			for (Book book : bookList) {
				// Insert values into database
				temp = "INSERT INTO book VALUES ('" + book.getIsbn() + "', NULL, '" + book.getTitle() + "', '" + book.getAuthor() + "', " + book.getYear() + ", '" + book.getPublisher() + "')";
				System.out.println(count + " " + temp);
				statement.setString(1, book.getIsbn()); statement.setString(2, book.getTitle());
				statement.setString(3, book.getAuthor()); statement.setInt(4, Integer.valueOf(book.getYear())); 
				statement.setString(5, book.getPublisher());
				statement.executeUpdate();
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void add_images_to_db(ArrayList<Book> bookList) {

		String sql = "INSERT IGNORE INTO images VALUES (?, ?, ?, ?)";
		String temp;

		try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
		PreparedStatement statement = con.prepareStatement(sql)) {
			int count = 0;
			for (Book book : bookList) {

				temp = "INSERT INTO images VALUES ('" + book.getIsbn() + "', '" + book.getUrls()[0] + "', '" + book.getUrls()[1] + "', " + book.getUrls()[2] + "')";
				System.out.println(count + " " + temp);
				count++;

				// Insert values into database
				statement.setString(1, book.getIsbn()); statement.setString(2, book.getUrls()[0]);
				statement.setString(3, book.getUrls()[1]); statement.setString(4, book.getUrls()[2]); 
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

