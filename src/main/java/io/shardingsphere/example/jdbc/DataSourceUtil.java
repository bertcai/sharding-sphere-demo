package io.shardingsphere.example.jdbc;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

public class DataSourceUtil {
    private static final String HOST = "localhost";
//    private static final int PORT = 3300;
//    private static final String USER_NAME = "root";
//    private static final String PASSWORD = "admin";

    public static DataSource createDataSource(final String dataSourceName, int port, String username, String password) {
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());
        result.setUrl(String.format("jdbc:mysql://%s:%s/%s?useSSL=false&characterEncoding=UTF-8" +
                "&serverTimezone=GMT&allowPublicKeyRetrieval=true", HOST, port, dataSourceName));
        result.setUsername(username);
        result.setPassword(password);
        return result;
    }
}
