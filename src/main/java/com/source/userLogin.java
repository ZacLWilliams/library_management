package com.source;

import java.util.Scanner;

public class userLogin {
    public void readUserInput() {
        // Try with resources
        try (Scanner userInput = new Scanner(System.in)) {
            System.out.println("Enter username");

            String userName = userInput.nextLine();  // Read user input

            System.out.println("Username is: " + userName);
        }
        //SELECT * FROM book WHERE title LIKE '%banana%';
    }
}
