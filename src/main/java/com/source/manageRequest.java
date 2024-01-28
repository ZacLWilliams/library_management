package com.source;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
//import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class manageRequest {
    //private String content;
    public static String[] processRequest(BufferedReader reader, StringBuilder request, String line) throws Exception {
        String[] information = new String[2];
        String type;
        String info;
        String content = "";
        String cookieId = null;
        //String reservedVal = "";
        int contentLength = 0;
        int i;
        int x = 0;
        int intchar;
        //int count = 0;
        char c;
        //byte[] utf;

        i = line.indexOf(" ");
        // Get the type of request, i.e. post, get
        type = line.substring(0, i);
        
        while (!line.isBlank()) {

            // Process read each line from client request and append to request stringbuilder
            request.append(line + "\r\n");
            line = reader.readLine();
            
            // Split each line but getting the index of whitespace
            i = line.indexOf(" ");
            // Bug management
            if (i < 0) {
                break;
            }

            //Get info of http request
            info = line.substring(0, i);

            if (info.equals("Content-Length:")) {
                //Get the value for content length
                contentLength = Integer.valueOf(line.substring(i + 1));
            }
            if (info.equals("Cookie:")) {
                cookieId = line.substring(i + 1);
            }
        }

        if (type.equals("POST") && contentLength != 0) {
            // Read and parse post request from client
            while (x < contentLength) {
                x++;
                intchar = reader.read();
                c = (char) intchar;
                content = content + c;
                //if (("" + c).equals("%") || count == 1 || count == 2) {
                //    count++;
                //    reservedVal = reservedVal + c;
                //} else if (count == 3) {
                //    utf = reservedVal.getBytes("ISO-8859-1");
                //    count = 0;
                //    String tempString = new String(utf, "UTF-8");
                //    reservedVal = "";
                //    content = content + tempString;
                //}
                //else {
                //    content = content + c;
                //}
            }
        }
        printRequest(request);
        information[0] = content; information[1] = cookieId;
        return information;
    }
    public static void printRequest(StringBuilder request) {
        System.out.println("--REQUEST--");
        System.out.println(request);
    }

    // Check url values to decide webpage
    public static String determineWebpage(String userSearch, String[] data, int cookieId, userId user) throws IOException {
        //ArrayList<String> arr = new ArrayList<String>();
        String search;
        File file;
        Document html; 
        //String data[] = null;
        //if (userInput == null || userInput.matches("/") || userInput.matches("/response?search=")) {
        
        // Pick the html file to use for each webpage
        if (userSearch == null || userSearch.equals("/") || userSearch.equals("/response?search=")) {
            file = new File("src/main/resources/Homepage.html");
            html = Jsoup.parse(file, "UTF-8"); 
            if (cookieId != 0) {
                html.getElementById("createaccount").text("");
                html.getElementById("login").text("");
                html.getElementById("profile").text("Hello " + user.getUsername() + "!");
            }
        }
        else if (userSearch.equals("/createaccount")) {
            file = new File("src/main/resources/Create_account.html");
            html = Jsoup.parse(file, "UTF-8"); 
            //webpage = "Create_account.html";

            // For this instance we have a dynamic webpage based on client requests so html file must be edited
            if (data != null) {
                // This means the username has already been taken in the database
                if (checkUser.check_db(data[0]) == false) {
                    //File file = new File("src/main/resources/Create_account.html");
                    //Document html = Jsoup.parse(file, "UTF-8"); 

                    //html.select("span[name$=use]").attr("value", "Username taken");

                    // Set values of html file using prior user input, in this case we are refilling username and password so user
                    //  does not have to retype, also we are notifying the user that the username is taken
                    html.getElementById("user").text("Username taken");
                    html.select("input[name$=username]").attr("value", data[0]);
                    html.select("input[name$=password]").attr("value", data[1]);
                    html.select("input[name$=confirmpassword]").attr("value", data[2]);

                    //FileWriter writer = new FileWriter(file);
                    //writer.write(html.toString());
                    //writer.flush();
                    //writer.close();

                // In this instance the username is available so we will add the username and password to the database
                } else {
                    file = new File("src/main/resources/Success_create.html");
                    html = Jsoup.parse(file, "UTF-8"); 
                    // Polymorphic add to database
                    checkUser.check_db(data[0], data[1]);
                }
            }
        }
        else if (userSearch.equals("/login")) {
            file = new File("src/main/resources/Login.html");
            html = Jsoup.parse(file, "UTF-8"); 
            
            if (data != null) {
                if (checkUser.check_user_pass(data[0], data[1]) == false) {
                    html.getElementById("user").text("Username or password does not exist");  
                    html.select("input[name$=username]").attr("value", data[0]);  
                    html.select("input[name$=password]").attr("value", data[1]);
                }else {
                    file = new File("src/main/resources/Success.html");
                    html = Jsoup.parse(file, "UTF-8");
                    //html.getElementById("createaccount").text("");
                    //html.getElementById("login").text("");
                    //html.getElementById("profile").text("Hello " + user.getUsername() + "!");
                    // Need to add profile
                }
            }
        }
        else if (userSearch.equals("/profile")) {
            file = new File("src/main/resources/Profile.html");
            html = Jsoup.parse(file, "UTF-8");
        }
        else if ((search = checkSearch(userSearch)) != "") {
            checkBook.getBooks(search);
            file = new File("src/main/resources/Search.html");
            html = Jsoup.parse(file, "UTF-8");
        }
        else {
            file = new File("src/main/resources/Homepage.html");
            html = Jsoup.parse(file, "UTF-8"); 

            if (cookieId != 0) {
                html.getElementById("createaccount").text("");
                html.getElementById("login").text("");
                html.getElementById("profile").text("Hello " + user.getUsername() + "!");
            }
        }
        return html.toString();
    }

    public static String checkSearch(String userSearch) {
        String search = "";

        for (int i = 0; i < userSearch.length(); i++) {
            char c = userSearch.charAt(i);
            String character = c + "";
            if (character.equals("=")) {
                search = userSearch.substring(i + 1);
                break;
            }
        }
        return search;
    }
}
