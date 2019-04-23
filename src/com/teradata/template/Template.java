package com.teradata.template;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Template {
    public final static String PATH = "file:";
    public final static String URLL = "url:";

    public String getContent(String path) throws Exception {
        if (path == null || path.trim().isEmpty()) {
            throw new Exception("template Exception: not allowed that the path is empty");
        }
        if (path.startsWith(PATH)) {
            path = path.substring(PATH.length());
            File file = new File(path);
            if (!file.exists()) {
                throw new Exception("template Exception: file:" + path + " is not exist,please check it");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String all = "";
            String str = null;
            while ((str = br.readLine()) != null) {
                all += str + "\r\n";
            }
            return all;
        } else if (path.startsWith("url:")) {
            String strUrl = path.substring(URLL.length());
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            String s;
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            reader.close();
            String str = sb.toString();
            return str;
        } else {
            throw new Exception("template exception: unknown data type, file or url?");
//            /*String str = "";
//            InputStream is = this.getClass().getResourceAsStream(path);
//            BufferedReader br = null;
//            try {
//                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            String s = "";
//            while (true) {
//                try {
//                    if ((s = br.readLine()) == null) break;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                str += s + "\r\n";
//
//            }*/
//            return str;
        }

    }

    public static void main(String[] args) {

    }

}
