package com.personal.csvloader.main;

import com.personal.common.utilities.PropertiesReader;
import com.personal.csvloader.service.CreateDataTables;
import com.personal.csvloader.service.DataBaseLoader;
import org.apache.log4j.Logger;

/**
 * Created by saurabhagrawal on 22/06/16.
 */
public class CreateandImportMain {
    public static Logger logger=Logger.getLogger(CreateandImportMain.class);

    public static void main(String[] args){


        if ("Y".equalsIgnoreCase(PropertiesReader.getProperty("createTables.Y.N"))){
            CreateDataTables createDataTables=new CreateDataTables();
            createDataTables.createTables();
        }
        boolean truncateFLag=false;   //// TODO: 22/06/16 read from propety file
        DataBaseLoader.loadData(truncateFLag);
        logger.info("Done with data import..");
    }
}
