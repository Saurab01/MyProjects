package com.personal.common.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by saurabhagrawal on 21/06/16.
 */
public class DBConnection {
    public static Connection getConnection(String driver,String databaseUrl,String userName,String password) throws SQLException,ClassNotFoundException {

        Class.forName(driver);
        Properties props = new Properties();
        props.put("user", userName);
        props.put("password", password);
        Connection connection= DriverManager.getConnection(databaseUrl, props);
        return connection;
    }
}
