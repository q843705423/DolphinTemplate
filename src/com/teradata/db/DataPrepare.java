package com.teradata.db;

import com.intellij.openapi.ui.Messages;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DataPrepare {
    public static HashMap<String, Object> getData(DataBaseUtil db, String key, String className, String objectName, String title, String tableName) throws Exception {

        List<FieldInfo> list = null;
        list = db.getTableFieldsBySql(tableName);
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("className", className);
        map.put("objectName", objectName);
        map.put("title", title);
        List<HashMap<String, Object>> fieldList = list.stream().map(fieldInfo -> {
            HashMap<String, Object> map1 = new HashMap<>();
            String property = DB.underscore2hump(fieldInfo.getFieldName());
            map1.put("property", property);
            map1.put("label", fieldInfo.getCommit());
            return map1;
        }).collect(Collectors.toList());
        System.out.println(fieldList);
        map.put("fieldList", fieldList);
        return map;
    }
}
