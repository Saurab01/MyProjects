package com.personal.saurabh.archive.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by saurabhagrawal on 19/02/17.
 */
public class PropertyLoader {

    public static Properties loadPropertyFile(final String propertyFileNAme) throws Exception{
        final Properties properties=new Properties();

        try{
            if (propertyFileNAme ==null || propertyFileNAme.trim().length() ==0) {
                throw new Exception("unable to load properties file");
            }

            //load properties file
            InputStream inputStream=PropertyLoader.class.getClassLoader().getResourceAsStream(propertyFileNAme);
            properties.load(inputStream);
        } catch (IOException e){
            throw new Exception(e);
        }
        return properties;
    }
}

