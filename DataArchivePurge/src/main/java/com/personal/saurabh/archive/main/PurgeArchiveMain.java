package com.personal.saurabh.archive.main;

import com.personal.saurabh.archive.ArchiveContants;
import com.personal.saurabh.archive.callable.ArchivePurgeCallable;
import com.personal.saurabh.archive.domain.Student;
import com.personal.saurabh.archive.utils.PropertyLoader;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created by saurabhagrawal on 19/02/17.
 */
public class PurgeArchiveMain {
    private static transient Logger logger=Logger.getLogger(PurgeArchiveMain.class);

    public static void main(String[] args){
        ExecutorService executorService= Executors.newFixedThreadPool(5);
        try{
            //load properties file
            final Properties properties= PropertyLoader.loadPropertyFile(ArchiveContants.PROPERTY_FILE);
            //properties.getProperty("").trim()
            //create a list to hold the future object associated with callable
            List<Future<Student>> list=new ArrayList<Future<Student>>();

            //creating 5 threads
            for (int i=0;i<5;i++){
                //create callable interface instance and ssign it to user callable class
                Callable<Student> callable=new ArchivePurgeCallable(i,"purge");

                //submit callable tasks to be executed by thread pool
                Future<Student> future =executorService.submit(callable);

                //Add task result in list
                list.add(future);
            }
            //retrieve task result from future list
            for (Future<Student> future :list){
                logger.info(future.get());
            }

        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (executorService!=null){
                executorService.shutdown();
            }
        }
    }
}
