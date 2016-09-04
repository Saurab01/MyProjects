package com.personal.csvloader.main;

import com.personal.common.utilities.PropertiesReader;
import com.personal.csvloader.service.ExportDatatoCSV;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saurabhagrawal on 04/09/16.
 */
public class ExportCSVMain {
    public static Logger logger = Logger.getLogger(ExportCSVMain.class);

    public static void main(String[] args) {
        ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
        PropertyConfigurator.configure(classLoader.getResource("log4j.properties"));
        logger.info("initialising properties files");

        List<String> configFilesList=new ArrayList<String>();
        configFilesList.add("database.properties");
        configFilesList.add("application.properties");
        PropertiesReader.initialize(configFilesList);

        ExportDatatoCSV exportDatatoCSV=new ExportDatatoCSV();
        exportDatatoCSV.checkTablesExistsList();

    }
}
