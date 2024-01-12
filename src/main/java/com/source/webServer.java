package com.source;

import java.net.*;
import java.util.stream.Collectors;
import java.io.*;
//import java.util.*;

//Relocate to new source file
public class webServer {
    private String userInput;
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
            try {
                StringBuilder request = new StringBuilder();
                line = reader.readLine();

                webServer.this.userInput = cleanRequest(line);
                
                while (!line.isBlank()) {
                    //System.out.println("debug");
                    request.append(line + "\r\n");
                    line = reader.readLine();
                }
                printRequest(request);
                check = true;

                //System.out.println("--REQUEST--");
                //System.out.println(request);

            } catch (Exception ex) {ex.printStackTrace();System.out.println("TEST2");}
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
        return line.replace(" HTTP/1.1", "");
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

                    Thread t = new Thread(new ClientHandler(client));
                    t.start();

                    //OutputStream clientOutput = client.getOutputStream();
                    //clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
                    //clientOutput.write(("\r\n").getBytes());
                    //clientOutput.write(("Hello World").getBytes());
                    //clientOutput.flush();

                    //if (userInput == null || userInput.matches("/") || userInput.matches("/response?search=")) {
                    if (userInput == null || userInput.equals("/") || userInput.equals("/response?search=")) {
                        System.out.println("1");
                        webPage = "Homepage.html";
                    }
                    else {
                        System.out.println("2");
                        webPage = "Create_account.html";
                    }

                    PrintWriter out = new PrintWriter(client.getOutputStream());
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-type: text/html");
                    out.println("\r\n");
                    
                    //Make sure we have processed user's input before reloading site
                    if (check == true) {
                        InputStream in = this.getClass().getClassLoader().getResourceAsStream(webPage);
                        String s = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));

                        out.println(s);
                        out.flush();
                        out.close();
                    
                        client.close();
                        check = false;
                    }
                }
            }
        } catch(IOException ex) {
            ex.printStackTrace();
            System.out.println("TEST3");
        }
    }
}
