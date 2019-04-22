package com.teradata.db;

public class DB extends com.teradata.db.DataBaseUtil {
    @Override
    protected String getUser() {
        return "root";
    }

    @Override
    protected String getDatabaseName() {
        return "apicloud";
    }

    @Override
    protected String getUrl() {
        return "jdbc:mysql://127.0.0.1:3306/man?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8";
    }

    @Override
    protected Integer getPort() {
        return 3306;
    }
    @Override
    protected String getIp() {
        return "10.73.134.229";
    }


    @Override
    protected String getPassword() {
        return "root";
//        return "sdfa@eWs127";
    }
}
