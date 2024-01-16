package com.source;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class manageRequest {
    public static String processRequest(BufferedReader reader, StringBuilder request, String line) throws Exception {
        String type;
        String info;
        String content = "";
        //String reservedVal = "";
        int contentLength = 0;
        int i;
        int x = 0;
        int intchar;
        //int count = 0;
        char c;
        //byte[] utf;

        i = line.indexOf(" ");
        type = line.substring(0, i);
    
        while (!line.isBlank()) {

            request.append(line + "\r\n");
            line = reader.readLine();
    
            i = line.indexOf(" ");
            if (i < 0) {
                break;
            }

            info = line.substring(0, i);

            if (info.equals("Content-Length:")) {
                contentLength = Integer.valueOf(line.substring(i + 1));
            }
        }

        if (type.equals("POST") && contentLength != 0) {
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
        return content;
    }
    public static void printRequest(StringBuilder request) {
        System.out.println("--REQUEST--");
        System.out.println(request);
    }

    public static String determineWebpage(String userSearch, String content) throws IOException {
        ArrayList<String> arr = new ArrayList<String>();
        File file;
        Document html; 
        //if (userInput == null || userInput.matches("/") || userInput.matches("/response?search=")) {
        if (userSearch == null || userSearch.equals("/") || userSearch.equals("/response?search=")) {
            file = new File("src/main/resources/Homepage.html");
            html = Jsoup.parse(file, "UTF-8"); 
        }
        else if (userSearch.equals("/createaccount")) {
            file = new File("src/main/resources/Create_account.html");
            html = Jsoup.parse(file, "UTF-8"); 
            //webpage = "Create_account.html";
            if (content != "") {
                arr = processInfo.processData(content);
                if (checkUser.check_db(arr.get(0)) == false) {
                    //File file = new File("src/main/resources/Create_account.html");
                    //Document html = Jsoup.parse(file, "UTF-8"); 

                    //html.select("span[name$=use]").attr("value", "Username taken");
                    html.getElementById("user").text("Username taken");
                    html.select("input[name$=username]").attr("value", arr.get(0));
                    html.select("input[name$=password]").attr("value", arr.get(1));
                    html.select("input[name$=confirmpassword]").attr("value", arr.get(2));

                    //FileWriter writer = new FileWriter(file);
                    //writer.write(html.toString());
                    //writer.flush();
                    //writer.close();

                } else {
                    file = new File("src/main/resources/Homepage.html");
                    html = Jsoup.parse(file, "UTF-8"); 
                    createUser.add_to_db(arr.get(0), arr.get(1));
                }
            }
        }
        else if (userSearch.equals("/login")) {
            file = new File("src/main/resources/Login.html");
            html = Jsoup.parse(file, "UTF-8"); 
        }
        else {
            file = new File("src/main/resources/Homepage.html");
            html = Jsoup.parse(file, "UTF-8"); 
        }
        return html.toString();
    }
}
