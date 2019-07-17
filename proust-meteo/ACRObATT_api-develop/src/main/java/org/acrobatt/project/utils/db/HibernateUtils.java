package org.acrobatt.project.utils.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

/**
 * A list of useful Hibernate commands
 */
public class HibernateUtils {

    private static Logger logger = LogManager.getLogger(HibernateUtils.class);

    private static Configuration cfg;
    private static SessionFactory sf;

    private HibernateUtils() {}

    /**
     * Loads the Hibernate configuration from a file
     * @param f the file
     */
    public static void loadConfiguration(File f) {
        try {
            cfg = new Configuration().configure(f);
            logger.info("Successfully loaded configuration from \'"+ f.getAbsolutePath() + "\'");
        } catch(Exception e) {
            logger.fatal("Failed to initialize Hibernate configuration", e);
        }
    }

    /**
     * Instantiates or retrieves a SessionFactory if it already exists
     * @return the SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if(sf == null) {
            try {
                sf = cfg.buildSessionFactory();
            } catch (Exception e) {
                logger.fatal("Failed to initialize SessionFactory", e);
            }
        }
        return sf;
    }

    /**
     * Refreshes a SessionFactory
     * @return a new SessionFactory instance
     */
    public static SessionFactory refreshSessionFactory() {
        try {
            sf = cfg.buildSessionFactory();
        } catch (Exception e) {
            logger.fatal("Failed to initialize SessionFactory", e);
        }
        return sf;
    }
}
