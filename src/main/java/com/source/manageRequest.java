package com.source;
import java.io.BufferedReader;

public class manageRequest {
    public static String processRequest(BufferedReader reader, StringBuilder request, String line) throws Exception {
        String type;
        String info;
        String content = "";
        int contentLength = 0;
        int i;
        int x = 0;
        int intchar;

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
                content = content + (char) intchar;
            }
        }
        //printRequest(request);
        return content;
    }
    public static void printRequest(StringBuilder request) {
        System.out.println("--REQUEST--");
        System.out.println(request);
    }
}
