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
            String content;
            //synchronized(wait) {
                try {
                    StringBuilder request = new StringBuilder();
                    line = reader.readLine();
    
                    webServer.this.userSearch = cleanRequest(line);

                    content = manageRequest.processRequest(reader, request, line);

                    //while (webPage == null) {
                    //    webPage = determineWebpage(userSearch);
                    //}

                    webPage = manageRequest.determineWebpage(userSearch, content);
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
                        //InputStream in = this.getClass().getClassLoader().getResourceAsStream(webPage);
                        //String s = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
                        Reader inputString = new StringReader(webPage);
                        BufferedReader reader = new BufferedReader(inputString);
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