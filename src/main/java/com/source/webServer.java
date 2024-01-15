package com.source;

import java.net.*;
import java.util.stream.Collectors;
import java.io.*;
//import java.util.*;

//Relocate to new source file
public class webServer {
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
            String type;
            String info;
            String content = "";
            int x = 0;
            int i;
            int intchar;
            char c;
            int contentLength = 0;
            //synchronized(this) {
                try {
                    StringBuilder request = new StringBuilder();
                    line = reader.readLine();

                    i = line.indexOf(" ");
                    type = line.substring(0, i);
    
                    webServer.this.userSearch = cleanRequest(line);
                    check = true;
                    
                    while (!line.isBlank()) {
                        //System.out.println("debug");
                        request.append(line + "\r\n");
                        line = reader.readLine();

                        i = line.indexOf(" ");
                        if (i < 0) {
                            break;
                        }
                        info = line.substring(0, i);
                        if (info.equals("Content-Length:")) {
                            contentLength = Integer.valueOf(line.substring(i + 1));
                            System.out.println(contentLength);
                        }
                    }
                    //if ()
                    //printRequest(request);
                    //String requestString = request.toString();
                    System.out.println(type);
                    if (type.equals("POST") && contentLength != 0) {
                        while (x < contentLength) {
                            x++;
                            intchar = reader.read();
                            c = (char) intchar;
                            content = content + c;
                        }
                        System.out.println(content);
                    }
    
                } catch (Exception ex) {ex.printStackTrace();
                    System.out.println("TEST2");}
            //}
        }
    }
    public static void main(String[] args) {
        webServer server = new webServer();
        server.go();
    }

    public void printRequest(StringBuilder request) {
        System.out.println("--REQUEST--");
        System.out.println(request);
    }

    public String cleanRequest(String line) {
        line = line.replace("GET ", "");
        line = line.replace("POST ", "");
        return line.replace(" HTTP/1.1", "");
    }

    public String determineWebpage(String userSearch) {
        String webpage;
        //if (userInput == null || userInput.matches("/") || userInput.matches("/response?search=")) {
        if (userSearch == null || userSearch.equals("/") || userSearch.equals("/response?search=")) {
            webpage = "Homepage.html";
        }
        else if (userSearch.equals("/createaccount")) {
            webpage = "Create_account.html";
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
        String webPage;
        // Make server socket
        try(ServerSocket serverSocket = new ServerSocket(4242)) {
            // Socket sock = new Socket("87.121.93.116", 4242);
            // Handle new messages
            while(true) {
                // Handle new incoming messages
                try(Socket client = serverSocket.accept()) {
                    //System.out.println("Debug:" + client.toString());
                    //System.out.println(client.isClosed());
                    if (check == false) {
                        Thread t = new Thread(new ClientHandler(client));
                        t.start();
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
                        webPage = determineWebpage(userSearch);
                        InputStream in = this.getClass().getClassLoader().getResourceAsStream(webPage);
                        String s = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));

                        check = false;

                        out.println(s);
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
