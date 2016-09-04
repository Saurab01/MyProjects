package com.personal.csvloader.service;

import com.opencsv.CSVWriter;
import com.personal.common.utilities.DBConnectionUtilities;
import com.personal.common.utilities.PropertiesReader;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by saurabhagrawal on 04/09/16.
 */
public class ExportDatatoCSV {

    public static Logger logger = Logger.getLogger(ExportDatatoCSV.class);

    private InputStream inputStream;
    String driver = PropertiesReader.getProperty("db.driver"),
            databaseUrl = PropertiesReader.getProperty("db.database"),
            userName = PropertiesReader.getProperty("db.username"),
            password = PropertiesReader.getProperty("db.password");

    public void checkTablesExistsList() {
        Connection connection = null;
        boolean success = false;
        try {
            logger.info("Checking Database connection::");
            connection = DBConnectionUtilities.getConnection(driver, databaseUrl, userName, password);

            logger.info(DBConnectionUtilities.getDatabaseInformation(connection));

            List<String> tableNames=new ArrayList<String>();
            tableNames=DBConnectionUtilities.getTableNames(connection);
            for (String tab:tableNames){
                logger.info("for table name::"+tab);
                ResultSet rs=DBConnectionUtilities.createResultSet(connection,tab);
                writeTOCSV(tab,rs);
            }

        } catch (ClassNotFoundException e) {
            logger.error(e);
        } catch (SQLException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error("error while writing to csv",e);
        } finally {
            try {
                if (connection!=null)
                    connection.close();

            } catch (SQLException e) {
                logger.error("error while closing connection"+e);
            }
        }
    }

    private void writeTOCSV(String tableName, ResultSet resultSet) throws IOException, SQLException {
        CSVWriter writer = new CSVWriter(new FileWriter(tableName+".csv"), '\t');  //separated by tab
        boolean includeHeaders=true;
        writer.writeAll(resultSet,includeHeaders);
        writer.close();
    }
}
