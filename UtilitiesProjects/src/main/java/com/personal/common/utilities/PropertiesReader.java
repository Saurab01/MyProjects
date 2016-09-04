package com.personal.common.utilities;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
/**
 * Created by saurabhagrawal on 21/06/16.
 */
public class PropertiesReader
{
    private static Logger logger =Logger.getLogger(PropertiesReader.class);

    //to hold all current properties and their values
    private static Properties m_properties;

    private PropertiesReader(){}

    //initialises by loading properties from files
    public static void initialize(List<String> propertiesFileList){
        Properties properties=new Properties();
        for (String propertyFIle:propertiesFileList){
            if (propertyFIle != null)
                logger.debug("in PropertiesReader.initialise Properties file name::"+propertyFIle);
            else logger.debug("in PropertiesReader.initialise Properties file name is null");
            m_properties=loadProperties(propertyFIle,properties);
        }
    }

    private static Properties loadProperties(String propertyFIle, Properties properties) {
        String fullpath="";
        try{
            fullpath=PropertiesReader.class.getClassLoader().getResource(propertyFIle).getPath();
            properties.load(new FileInputStream(fullpath));
            logger.debug("full path"+fullpath);
        }catch (FileNotFoundException e){
            logger.error("file not found",e);

        }catch(Exception e){
            logger.error("failure in accessing file",e);
        }
        //logger.debug("properties::"+properties);
        return properties;
    }
    //if property is not found it will return null
    public static String getProperty(String key){
        return m_properties.getProperty(key);
    }
    public static Set<String> getAllPropertiesNames(){
        return m_properties.stringPropertyNames();
    }
    public boolean containsKey(String key){
        return m_properties.containsKey(key);
    }

    //to write to property file
    public void setProperty(String key, String value){
        m_properties.setProperty(key, value);
    }
}