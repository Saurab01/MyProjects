package com.personal.common.utilities;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by saurabhagrawal on 21/06/16.
 */
public class DBConnectionUtilities {
    public static Logger logger = Logger.getLogger(DBConnectionUtilities.class);
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
    (which were all null).
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

    public static ResultSet createResultSet(Connection connection, String tableName) throws SQLException {
        Statement stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        StringBuffer sql = new StringBuffer("SELECT");
        List<String> columns=extractColumn(connection,tableName,"NAME");
        for (String col:columns){
            sql.append(" ");
            sql.append(col).append(",");
        }
        sql.deleteCharAt(sql.length()-1);   //deleting comma at last
        sql.append(" FROM ").append(tableName);
        logger.info("sql created is:::"+sql);
        return stmt.executeQuery(sql.toString());

    }

    //category=NAME  -> will send all column names
    //CATEGORY=TYPE--> will send all types

    private static List<String> extractColumn(Connection connection, String tableName,String category) throws SQLException {
        List<Map<String,Object>> columnLists=new ArrayList<Map<String, Object>>();
        columnLists=getColumnsNamesandType(connection,tableName);
        List<String> columns=new ArrayList<String>();
        String key="";
        if (category.equalsIgnoreCase("NAME")) key="columnName";
        else if (category.equalsIgnoreCase("TYPE")) key="columnType";
        for (Map<String,Object> map:columnLists){
            columns.add((String) map.get(key));
        }
        return columns;
    }

    public static void insertRecords(Connection connection,String tableName,String insert_sql,List<Object> values) throws SQLException {
        PreparedStatement preparedStatment=connection.prepareStatement(insert_sql);
        List<String> columns=extractColumn(connection,tableName,"TYPE");
        logger.info("types for tableName: "+tableName+" :: " +columns);
        int count=1,index=0;
        for (String type: columns){
            preparedStatment=setPrepStmtByType(count++,type,preparedStatment,values.get(index++));
        }
        preparedStatment.addBatch();

        //// TODO: 05/09/16 once for all rows addBatch() done finally:
        //int[] rowsUpdated=preparedStatment.executeBatch();

    }

    private static PreparedStatement setPrepStmtByType(int index, String type, PreparedStatement preparedStatment, Object val) throws SQLException {
        if (type.equalsIgnoreCase(DataTypes.INT.toString())){
            preparedStatment.setInt(index, (Integer) val);
        }
        else if (type.equalsIgnoreCase(DataTypes.BIT.toString())){
            preparedStatment.setBoolean(index, (Boolean) val);
        }
        else if (type.equalsIgnoreCase(DataTypes.VARCHAR.toString())){
            preparedStatment.setString(index, (String) val);
        }
        else if (type.equalsIgnoreCase(DataTypes.DATE.toString())){
            preparedStatment.setDate(index, (Date) val);
        }else if (type.equalsIgnoreCase(DataTypes.BIGINT.toString())){
            preparedStatment.setLong(index, (Long) val);
        }else if (type.equalsIgnoreCase(DataTypes.TIMESTAMP.toString())){
            preparedStatment.setTimestamp(index, (Timestamp) val);
        }else if (type.equalsIgnoreCase(DataTypes.DATETIME.toString())){
            preparedStatment.setDate(index, (Date) val);
        }else if (type.equalsIgnoreCase(DataTypes.REAL.toString())){
            preparedStatment.setFloat(index, (Float) val);
        }else if (type.equalsIgnoreCase(DataTypes.FLOAT.toString())){
            preparedStatment.setFloat(index, (Float) val);
        }else if (type.equalsIgnoreCase(DataTypes.DOUBLE.toString())){
            preparedStatment.setDouble(index, (Double) val);
        }
        else if (type.equalsIgnoreCase(DataTypes.NUMERIC.toString())){
            preparedStatment.setBigDecimal(index, (BigDecimal) val);
        }
        else if (type.equalsIgnoreCase(DataTypes.DECIMAL.toString())){
            preparedStatment.setBigDecimal(index, (BigDecimal) val);
        }
        else if (type.equalsIgnoreCase(DataTypes.INTEGER.toString())){
            preparedStatment.setInt(index, (Integer) val);
        }
        return preparedStatment;
    }


}
