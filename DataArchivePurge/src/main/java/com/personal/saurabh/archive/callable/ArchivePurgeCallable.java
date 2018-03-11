package com.personal.saurabh.archive.callable;

import com.personal.saurabh.archive.domain.Student;
import org.apache.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * Created by saurabhagrawal on 19/02/17.
 */
public class ArchivePurgeCallable implements Callable<Student> {
    private static transient Logger logger=Logger.getLogger(ArchivePurgeCallable.class);

    private int threadNumber;
    private String purge;

    public ArchivePurgeCallable(int threadNumber, String purge) {
        this.threadNumber = threadNumber;
        this.purge = purge;
    }

    //Entry point of callable and this code will execute asynchronously
    public Student call() throws Exception {
        logger.info("Thread name: " +Thread.currentThread().getName() + " | Thread id: " + Thread.currentThread().getId() );

        return new Student(threadNumber,threadNumber+" "+purge,threadNumber*100,"password");
    }
}
