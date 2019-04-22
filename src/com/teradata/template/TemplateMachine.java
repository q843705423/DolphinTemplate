package com.teradata.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemplateMachine {

    public static String SPLIT = "$$$";
    public static Template template = new Template();

    public static String dfs(String content, HashMap<String, Object> map) {
        while (content.contains(SPLIT)) {
            int position = content.indexOf(SPLIT);
            int position2 = content.indexOf(SPLIT, position + SPLIT.length()) + SPLIT.length();
            String sourceNoDeal = content.substring(position, position2);
            String source = sourceNoDeal.substring(3, sourceNoDeal.length() - SPLIT.length());
            if (source.contains(">")) {
                String s[] = source.split(">");
                String var = s[0];
                String type = s[1];
                String file = s[2];
                if ("list".contains(type)) {
                    List<HashMap<String, Object>> ls = (List<HashMap<String, Object>>) map.get(var);
                    String tempContents = "";
                    for (int i = 0; i < ls.size(); i++) {
                        String ss = template.getContent(file);
                        tempContents += dfs(ss, ls.get(i));
                    }
                    content = content.replace(sourceNoDeal, tempContents);
                } else if ("map".equals(type)) {
                    String temp = template.getContent(file);
                    HashMap<String, Object> m = (HashMap<String, Object>) map.get(var);
                    content = content.replace(sourceNoDeal, dfs(temp, m));
                } else {

                }
            } else {//String
                try{
                    content = content.replace(sourceNoDeal, map.get(source).toString());
                }catch (Exception e){

                }
            }
        }
        return content;
    }

    public static HashMap<String, Object> getData() {
        String b = "hello world";
        List<HashMap<String, Object>> a = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", "1");
        map.put("name", "张三");
        a.add(map);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("id", "2");
        map2.put("name", "李四");
        a.add(map2);
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("id", "3");
        map3.put("name", "王五");
        a.add(map3);
        HashMap<String, Object> c = new HashMap<>();
        c.put("button1", "center");
        c.put("button2", "cancel");
        HashMap<String, Object> all = new HashMap<>();
        all.put("a", a);
        all.put("b", b);
        all.put("c", c);
        return all;
    }
}
