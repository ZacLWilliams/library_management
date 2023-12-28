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
        try(ServerSocket serverSock = new ServerSocket(4242)) {
            //Socket sock = new Socket("87.121.93.116", 4242);
            while(true) {
                Socket sock = serverSock.accept();
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
