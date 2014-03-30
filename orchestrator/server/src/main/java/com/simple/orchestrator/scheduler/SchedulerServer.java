package com.simple.orchestrator.scheduler;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerServer {

    private static final Logger logger = Logger.getLogger(SchedulerServer.class.getName());

    public void daemonize() throws Exception {
        //System.out.close();
        //System.err.close();
        
        logger.info("Starting to daemonize");

        String propertiesFile = getPropertiesFile();
        
        if (propertiesFile == null) {
            throw new RuntimeException("You must specify the taskengine-server.properties file as a command line argument -Dcom.hp.taskengine.properties=<your properties file>");
        }
        
        logger.info("Using property file " + propertiesFile);

        logger.info("Creating Scheduler Factory");

        SchedulerFactory sf = new StdSchedulerFactory(propertiesFile);

        logger.info("Creating new scheduler");
        Scheduler sched = sf.getScheduler();

        logger.info("------- Initialization Complete -----------");

        logger.info("------- (Not Scheduling any Jobs - relying on a remote client to schedule jobs --");

        logger.info("------- Starting Scheduler ----------------");

        sched.start();

        logger.info("------- Started Scheduler -----------------");
    }

    public static void main(String[] args) throws Exception {
        SchedulerServer server = new SchedulerServer();
        server.daemonize();
    }

    protected String getPropertiesFile() {
        return System.getProperties().getProperty(Constants.PROPERTIES_FILE);
    }
}