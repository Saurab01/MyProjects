package com.personal.common.utilities;

import java.sql.*;
import java.util.*;

/**
 * Created by saurabhagrawal on 21/06/16.
 */
public class DBConnectionUtilities {
    public static Connection getConnection(String driver,String databaseUrl,String userName,String password) throws SQLException,ClassNotFoundException {

        Class.forName(driver);
        Properties props = new Properties();
        props.put("user", userName);
        props.put("password", password);
        Connection connection= DriverManager.getConnection(databaseUrl, props);
        return connection;
    }
    public static Map<String,Object> getDatabaseInformation(Connection connection) throws SQLException {
        Map<String,Object> databseInfoMap=new HashMap<String, Object>();
        DatabaseMetaData databaseMetaData = connection.getMetaData();

        databseInfoMap.put("majorVersion",databaseMetaData.getDatabaseMajorVersion());
        databseInfoMap.put("minorVersion",databaseMetaData.getDatabaseMinorVersion());

        databseInfoMap.put("productName",databaseMetaData.getDatabaseProductName());
        databseInfoMap.put("productVersion",databaseMetaData.getDatabaseProductVersion());

        databseInfoMap.put("driverMajorVersion",databaseMetaData.getDriverMajorVersion());
        databseInfoMap.put("driverMinorVersion",databaseMetaData.getDriverMinorVersion());

        return databseInfoMap;
    }

 //listing tables

    /*
    passing it 4 parameters which are all null. The parameters can help limit the number of tables that are
     returned in the ResultSet. However, since I want all tables returned, I passed null in all of these parameters.
     */
    /*
    The ResultSet returned from the getTables() method contains a list of table names matching the 4 given parameters
    (which were all null). This ResultSet contains 10 columns, which each contain information about the given table.
    The column with index 3 contains the table name itself.
     */
    public static List<String> getTableNames(Connection connection) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        List<String> tablesList=new ArrayList<String>();
        String   catalog          = null;
        String   schemaPattern    = null;
        String   tableNamePattern = null;
        String[] types            = null;

        ResultSet result = databaseMetaData.getTables(
                catalog, schemaPattern, tableNamePattern, types );

        while(result.next()) {
            String tableName = result.getString("TABLE_NAME");
            tablesList.add(tableName);
        }
        return tablesList;

    }

    //Listing Columns in a Table

    public static List<Map<String,Object>> getColumnsNamesandType(Connection connection,String table_name) throws SQLException{
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        Map<String,Object> columnNamesTypesMap;
        List<Map<String,Object>> listColumns=new ArrayList<Map<String, Object>>();

        String   catalog           = null;
        String   schemaPattern     = null;
        String   tableNamePattern  = table_name;
        String   columnNamePattern = null;


        ResultSet resultSet = databaseMetaData.getColumns(
                catalog, schemaPattern,  tableNamePattern, columnNamePattern);

        while(resultSet.next()) {
            columnNamesTypesMap=new HashMap<String, Object>();
            columnNamesTypesMap.put("columnName", resultSet.getString("COLUMN_NAME"));
            columnNamesTypesMap.put("columnType", resultSet.getString("TYPE_NAME"));
            columnNamesTypesMap.put("size", resultSet.getInt("COLUMN_SIZE"));
            listColumns.add(columnNamesTypesMap);
        }
        return listColumns;

    }

}
