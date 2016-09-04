package com.personal.csvloader.service;


import com.personal.common.utilities.CommonUtilities;
import com.personal.common.utilities.DBConnectionUtilities;
import com.personal.common.utilities.PropertiesReader;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by saurabhagrawal on 22/06/16.
 */
public class CreateDataTables {

    public static Logger logger=Logger.getLogger(CreateDataTables.class);

    private InputStream inputStream;
    String driver=PropertiesReader.getProperty("db.driver"),
            databaseUrl=PropertiesReader.getProperty("db.database"),
            userName=PropertiesReader.getProperty("db.username"),
            password=PropertiesReader.getProperty("db.password");

    public boolean createTables() {
        Connection connection=null;
        boolean success=false;
        try{
            logger.info("Excecuting scripts for Table creation::");
            connection= DBConnectionUtilities.getConnection(driver, databaseUrl, userName, password);

            //give the input file to Reader
            inputStream=getClass().getResourceAsStream(PropertiesReader.getProperty("database.createTable"));
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            ScriptRunner scriptRunner=new ScriptRunner(connection);
            scriptRunner.runScript(bufferedReader);

            createProcedure(connection);
            logger.info("Done with script for table creation...");
            success=true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("error while closing connections",e);
            }
        }
        return success;
    }

    private void createProcedure(Connection connection) throws SQLException, IOException {
        logger.info("Creating Procedure...");
        Statement statement =connection.createStatement();
        //give the input file to reader
        inputStream=getClass().getClassLoader().getResourceAsStream(PropertiesReader.getProperty("database.Procedure"));
        BufferedReader bf=new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder stringBuilder=new StringBuilder();
        String line=null;
        while ((line=bf.readLine())!=null){
            stringBuilder.append(line);
        }
        //close the Buffered Reader
        bf.close();
        for (String sqlPart: stringBuilder.toString().split("\\$\\$")){
            if (!CommonUtilities.isStringEmptyorNull(sqlPart))
                statement.addBatch(sqlPart);
        }
        statement.executeBatch();
        statement.close();;
        logger.info("Done with create Procedures...");
    }

}
