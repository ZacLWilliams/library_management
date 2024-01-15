package com.source;
import java.io.BufferedReader;

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
}
