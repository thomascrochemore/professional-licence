package org.acrobatt.project.listeners;

import org.acrobatt.project.mongo.MongoClientConfig;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationLoaderListener implements ServletContextListener {

    Logger logger = LogManager.getLogger(this);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            //load the properties file
            input = new FileInputStream(event.getServletContext().getRealPath("WEB-INF/classes/proust.properties"));
            prop.load(input);
            logger.info("(1) Loaded proust.properties");

            //load the Hibernate configuration file
            File file = new File(event.getServletContext().getRealPath(prop.getProperty("proust.hibernate.filepath")));
            HibernateUtils.loadConfiguration(file);
            HibernateUtils.getSessionFactory(); // Just call the static initializer of that class
            logger.info("(2) Loaded Hibernate configuration file");

            //load the MongoDB configuration
            MongoClientConfig.setHost(prop.getProperty("proust.mongodb.host"));
            MongoClientConfig.setPort(Integer.parseInt(prop.getProperty("proust.mongodb.port")));
            MongoClientConfig.setDatabase(prop.getProperty("proust.mongodb.db"));
            MongoClientConfig.setCollection(prop.getProperty("proust.mongodb.collection"));
            MongoClientConfig.setNeedAuth(Boolean.parseBoolean(prop.getProperty("proust.mongodb.auth")));
            MongoClientConfig.setLogin(prop.getProperty("proust.mongodb.login"));
            MongoClientConfig.setPassword(prop.getProperty("proust.mongodb.password"));
            logger.info("(3) Loaded MongoDB configuration");
            logger.info("Application successfully configured");

        } catch (Exception e) {
            logger.fatal("Could not load app configuration !", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.fatal("Could not close app configuration. Exiting !", e);

                }
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        HibernateUtils.getSessionFactory().close(); // Free all resources
    }
}
