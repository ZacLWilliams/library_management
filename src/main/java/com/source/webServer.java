package com.source;

import java.net.*;
//import java.util.ArrayList;
//import java.util.stream.Collectors;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

import java.io.*;
//import java.util.*;

//Relocate to new source file
public class webServer {
    private String webPage;
    private String userSearch;
    private boolean check = false;

    // Handle thread of client
    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;
        // Connect to client socket and get buffered read of http request
        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamReader);
            } catch (Exception ex) {ex.printStackTrace();System.out.println("TEST1");}
        }

        public void run() {
            String line;
            //String content;
            //synchronized(wait) {
                try {
                    StringBuilder request = new StringBuilder();
                    line = reader.readLine();
                    
                    // Get user requested url, userSearch
                    webServer.this.userSearch = cleanRequestHead(line);

                    // Process request to determine webpage
                    webPage = manageRequest.processRequest(reader, request, line, userSearch);

                    // Parse request to get content if it exists
                    //content = manageRequest.processRequest(reader, request, line);

                    //while (webPage == null) {
                    //    webPage = determineWebpage(userSearch);
                    //}
                    
                    // Decide webpage based on user input, so both url and user entered content
                    //webPage = manageRequest.determineWebpage(userSearch, content);
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

    public String cleanRequestHead(String line) {
        line = line.replace("GET ", "");
        line = line.replace("POST ", "");
        return line.replace(" HTTP/1.1", "");
    }

    public void go() {
        // Make server socket
        try(ServerSocket serverSocket = new ServerSocket(4242)) {
            // Run server infinitely
            while(true) {
                // Handle new incoming messages
                try(Socket client = serverSocket.accept()) {
                    //System.out.println("Debug:" + client.toString());
                    //System.out.println(client.isClosed());

                    // Create and run thread until it is complete
                    // Use check to prevent repeat requests from same user
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

                    // Send success to client
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-type: text/html");
                    out.println("\r\n");
                    
                    // Make sure we have processed user's input before reloading site
                    if (check == true) {
                        //InputStream in = this.getClass().getClassLoader().getResourceAsStream(webPage);
                        //String s = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));

                        // Convert html document to input string so it can be buffered read
                        Reader inputString = new StringReader(webPage);
                        BufferedReader reader = new BufferedReader(inputString);

                        // Send html to client line by line
                        String line = reader.readLine();
                        while (line != null) {
                            out.println(line);
                            line = reader.readLine();
                        }

                        check = false;

                        //out.println(s);
                        reader.close();
                        out.close();
                        out.flush();
                    
                        client.close();
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