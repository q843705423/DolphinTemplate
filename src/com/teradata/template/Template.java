package com.teradata.template;

import java.io.*;

public class Template {
	public static final String PATH = "file:";
	public String getContent(String path) {

		String str = "";
		InputStream is = this.getClass().getResourceAsStream(path);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String s = "";
		while (true) {
			try {
				if ((s = br.readLine()) == null) break;
			} catch (IOException e) {
				e.printStackTrace();
			}
			str += s+"\r\n";
		}
		return str;
	}

	public static void main(String[] args) {

	}

}
