package com.source;

import java.net.*;
import java.io.*;

//Relocate to new source file
public class dataServer {
    public static void main(String[] args) {
        dataServer server = new dataServer();
        server.go();
    }
    public void go() {
        // Make server socket
        try(ServerSocket serverSocket = new ServerSocket(4242)) {
            // Socket sock = new Socket("87.121.93.116", 4242);
            // Handle new messages
            while(true) {
                // Handle new incoming messages
                try(Socket client = serverSocket.accept()) {
                    System.out.println("Debug:" + client.toString());

                    // Read request
                    InputStreamReader streamReader = new InputStreamReader(client.getInputStream());

                    BufferedReader reader = new BufferedReader(streamReader);

                    StringBuilder request = new StringBuilder();
                    String userInput = reader.readLine();

                    while (!userInput.isBlank()) {
                        request.append(userInput + "\r\n");
                        userInput = reader.readLine();
                    }

                    System.out.println("--REQUEST--");
                    System.out.println(request);

                    OutputStream clientOutput = client.getOutputStream();
                    clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
                    clientOutput.write(("\r\n").getBytes());
                    clientOutput.write(("Hello World").getBytes());
                    clientOutput.flush();

                    client.close();
                }
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
