package com.source;

import java.io.FileReader;
import java.util.List; 
import com.opencsv.*; 
//import com.mysql.*;
import java.sql.*;

import java.util.ArrayList;

public class readData {
	private static ArrayList<Book> bookList = new ArrayList<Book>();
	static final String DB_URL = "jdbc:mysql://localhost/library_db";
	static final String USER = "root";
	static final String PASS = "Sapiens789-";

	public static void main(String[] args) {
    	String file = "books.csv";
		readCSV(file);
		add_to_db(bookList);
		//readData t = new readData();
		//ArrayList<Book> bookList = t.readCSV(file);
		//System.out.println(bookList.get(0).getIsbn());
	}

	public static void readCSV(String file) { 
		try { 
			// Create an object of file reader 
			// class with CSV file as a parameter. 
			FileReader filereader = new FileReader(file); 

			// create csvReader object and skip first Line 
			CSVReader csvReader = new CSVReaderBuilder(filereader) 
								.withSkipLines(1) 
								.build(); 
			List<String[]> allData = csvReader.readAll(); 

			//ArrayList<Book> bookList = new ArrayList<Book>();

			// Create ArrayList of Book objects
			for (String[] row : allData) { 
				for (String cell : row) { 
					// Remove quotes
					String cleanCell = cell.replace("\"", "");
					String[] tempBook = cleanCell.split(";", -1);

					String[] tempUrls;
					tempUrls = new String[] {tempBook[5], tempBook[6], tempBook[7]};

					Book b = new Book(tempBook[0], tempBook[1], tempBook[2], tempBook[3], tempBook[4], tempUrls);
					
					bookList.add(b);
				} 
			} 
			//return(bookList);
		} catch (Exception e) { 
			e.printStackTrace(); 
			//return null;
		} 
	}

	public static void add_to_db(ArrayList<Book> bookList) {
		try  {
			// Create connection
			Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
			Statement statement = con.createStatement();

			String sql;
			for (Book book : bookList) {
				// Insert values into database
				sql = "INSERT INTO book VALUES ('" + book.getIsbn() + "', NULL, '" + book.getTitle() + "', '" + book.getAuthor() + "', " + book.getYear() + ", '" + book.getPublisher() + "')";
				System.out.println(sql);
				statement.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

