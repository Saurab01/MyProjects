package com.personal.csvloader.service;


import com.opencsv.CSVReader;
import com.personal.common.utilities.DBConnection;
import com.personal.common.utilities.PropertiesReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by saurabhagrawal on 22/06/16.
 */
public class DataBaseLoader {

    public static Logger logger=Logger.getLogger(DataBaseLoader.class);

    public static void loadData(boolean truncateFLag)  {
        String tableName=null;
        PreparedStatement preparedStm=null;
        Connection connection=null;
        CSVReader rowsReader=null;
        String filePath=null;
        String driver="", databaseUrl="", userName="", password=""; //// TODO: 22/06/16 add from properties file


        try{
            File folder=new File(PropertiesReader.getProperty("csv.filePath"));
            File [] listofFiles= folder.listFiles();
            logger.info("loading started");
            assert listofFiles != null;  //// TODO: 22/06/16 check what it does
            for (File file:listofFiles){  //todo:null pointer check
                if (file.getName().contains(".csv") ){
                    connection= DBConnection.getConnection(driver, databaseUrl, userName, password);
                    filePath=PropertiesReader.getProperty("csv.filePath");
                    filePath=filePath.replace("\\","/");  //todo: null pointer
                    tableName=file.getName().substring(file.getName().indexOf(".")+1,file.getName().length()-4);

                    rowsReader=new CSVReader(new FileReader(file),'\t') ;  //separator is '\t'
                    logger.info("finding by separator tab");
                    rowsReader.readNext();
                    logger.info("importing table: " +tableName);
                    createTempFile(rowsReader,tableName);
                  //  long totalRows=rowsReader.getLinesRead()-1;

                    if(truncateFLag){  //true if delete the table entries
                        truncateTable(connection,tableName);
                    }
                    String insertQuery="LOAD DATA LOCAL INFILE '"+filePath+"' INTO TABLE "+tableName+ " IGNORE 1 LINES";
                    logger.info("Query::"+insertQuery);
                    preparedStm=connection.prepareStatement(insertQuery);
                    int result=preparedStm.executeUpdate();
                    logger.info("Table "+tableName+"successfully imported with CSV" +"record count="+result);

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            assert filePath != null;
            File tempFile=new File(filePath);
            if(tempFile.exists())  tempFile.delete();
            try{
                if (preparedStm!=null) {
                    preparedStm.close();
                    logger.info("closing PreparedStatement for tableName::"+tableName);
                }
                if (connection!=null){
                    connection.close();
                    logger.info("closing Connection for tableName::"+tableName);
                }
            }catch (SQLException e){
                logger.error("error while closing connections",e);
            }
        }
    }

    private static void truncateTable(Connection connection, String tableName) {
        Statement statement=null;
        try{
            logger.info("Deleting data from table::"+tableName);
            statement=connection.createStatement();
            statement.execute("DELETE FROM "+tableName);

        } catch (SQLException e) {
            logger.error("Error while truncating table",e);
        }finally {
            try{if (statement!=null)
                     statement.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    private static void createTempFile(CSVReader rowsReader, String tableName) {
        Writer writer=null;
        String tempText=null;
        String[] nextRow=null;
        String[] fields=null;

        try{
            tempText=PropertiesReader.getProperty("csv.filePath")+"temp.txt";
            File tempFile=new File(tempText);
            if (tempFile.exists()) tempFile.delete();

            writer =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempText),"utf-8"));

            while ((nextRow=rowsReader.readNext())!=null){
                fields=parcsCSVLineValidations(nextRow);
                logger.info("appending to file");

                StringBuilder stringBuilder=new StringBuilder();
                for (String field : fields) {
                    String data = field;
                    if (data.isEmpty()) {
                        data = "\\N";  //new Line
                    }
                    stringBuilder.append(data).append("\t");
                }
                writer.write("\n");
                writer.write(stringBuilder.toString());
            }

        } catch (FileNotFoundException e) {
           logger.error(e);
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }finally {
            try{
                assert writer != null;
                writer.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    private static String[] parcsCSVLineValidations(String[] fields) {
        for (String field : fields) {
            field.trim();
            //   fields[i]=fields[i].replace("\"","");
            //or anyother validations like remove double quotes
        }
        return fields;
    }
}
