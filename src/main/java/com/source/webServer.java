package com.source;

import java.net.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
//import java.util.*;

//Relocate to new source file
public class webServer {
    private String webPage;
    private String content;
    private String userSearch;
    private String user;
    private boolean check = false;
    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;
        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamReader);
            } catch (Exception ex) {ex.printStackTrace();System.out.println("TEST1");}
        }

        public void run() {
            String line;
            //synchronized(this) {
                try {
                    StringBuilder request = new StringBuilder();
                    line = reader.readLine();
    
                    webServer.this.userSearch = cleanRequest(line);

                    content = manageRequest.processRequest(reader, request, line);

                    webPage = determineWebpage(userSearch);
                    //System.out.println(content);

                    check = true;
    
                } catch (Exception ex) {ex.printStackTrace();
                    System.out.println("TEST2");}
            //}
        }
    }
    public static void main(String[] args) {
        webServer server = new webServer();
        server.go();
    }

    public String cleanRequest(String line) {
        line = line.replace("GET ", "");
        line = line.replace("POST ", "");
        return line.replace(" HTTP/1.1", "");
    }

    public String determineWebpage(String userSearch) throws IOException {
        String webpage;
        ArrayList<String> arr = new ArrayList<String>();
        //if (userInput == null || userInput.matches("/") || userInput.matches("/response?search=")) {
        if (userSearch == null || userSearch.equals("/") || userSearch.equals("/response?search=")) {
            webpage = "Homepage.html";
        }
        else if (userSearch.equals("/createaccount")) {
            webpage = "Create_account.html";
            System.out.println("1");
            if (content != "") {
                System.out.println("2");
                arr = processInfo.processData(content);
                if (checkUser.check_db(arr.get(0)) == false) {
                    System.out.println("3");
                    File file = new File("src/main/resources/Create_account.html");
                    Document html = Jsoup.parse(file, "UTF-8"); 
                    //html.select("span[name$=use]").attr("value", "Username taken");
                    html.getElementById("user").append("Username taken");
                    html.select("input[name$=username]").attr("value", arr.get(0));
                    html.select("input[name$=password]").attr("value", arr.get(1));
                    html.select("input[name$=confirmpassword]").attr("value", arr.get(2));

                    FileWriter writer = new FileWriter(file);
                    writer.write(html.toString());
                    writer.flush();
                    writer.close();
                    //webpage = "Username_taken.html";
                } else {
                    System.out.println("4");
                    createUser.add_to_db(arr.get(0), arr.get(1));
                    webpage = "Homepage.html";
                }
            }
        }
        else if (userSearch.equals("/login")) {
            webpage = "Login.html";
        }
        else {
            webpage = "Homepage.html";
        }
        return webpage;
    }

    public void go() {
        // Make server socket
        try(ServerSocket serverSocket = new ServerSocket(4242)) {
            // Handle new messages
            while(true) {
                // Handle new incoming messages
                try(Socket client = serverSocket.accept()) {
                    //System.out.println("Debug:" + client.toString());
                    //System.out.println(client.isClosed());
                    if (check == false) {
                        Thread t = new Thread(new ClientHandler(client));
                        t.start();

                        //try {Thread.sleep(1000);} catch(InterruptedException ex) {ex.printStackTrace();}

                        while (t.isAlive()) {
                            //System.out.println("Waiting...");
                        }
                    }

                    //OutputStream clientOutput = client.getOutputStream();
                    //clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
                    //clientOutput.write(("\r\n").getBytes());
                    //clientOutput.write(("Hello World").getBytes());
                    //clientOutput.flush();


                    PrintWriter out = new PrintWriter(client.getOutputStream());
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-type: text/html");
                    out.println("\r\n");
                    
                    //Make sure we have processed user's input before reloading site
                    if (check == true) {
                        InputStream in = this.getClass().getClassLoader().getResourceAsStream(webPage);
                        String s = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));

                        check = false;

                        out.println(s);
                        out.close();
                        out.flush();
                    
                        client.close();

                        //if (webPage.equals("Create_account.html")) {
                        //    File file = new File("src/main/resources/Create_account.html");
                        //    Document html = Jsoup.parse(file, "UTF-8"); 
                        //    Element test = html.select("p").first();
                        //    test.text("Username");

                            //OutputStream outhtml = new FileOutputStream(file);
                            //outhtml.close();
                        //}
                    }
                } catch(IOException ex) {
                    ex.printStackTrace();
                    System.out.println("TEST4");
                }
            }
        } catch(IOException ex) {
            ex.printStackTrace();
            System.out.println("TEST3");
        }
    }
}