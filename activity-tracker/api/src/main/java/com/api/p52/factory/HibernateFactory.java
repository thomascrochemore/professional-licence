package com.api.p52.factory;

import com.api.p52.properties.ApplicationProperties;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Singleton
public class HibernateFactory {


    private ApplicationProperties properties;

    private SessionFactory sessionFactory;

    public HibernateFactory(@InjectParam ApplicationProperties properties){
        this.properties = properties;
        configure();
    }
    private void configure(){
        Configuration configuration = new Configuration()
                .configure(properties.getProperty("app.hibernate.conf"))
                .setProperty("hibernate.connection.url",properties.getProperty("app.hibernate.connection.url"))
                .setProperty("hibernate.connection.username",properties.getProperty("app.hibernate.connection.username"))
                .setProperty("hibernate.connection.password",properties.getProperty("app.hibernate.connection.password"))
                .setProperty("hibernate.dialect",properties.getProperty("app.hibernate.dialect"));

        Boolean create = properties.getBooleanProperty("app.hibernate.create");
        if(create) {
            configuration.setProperty("hibernate.hbm2ddl.auto","create");
        }
        sessionFactory = configuration.buildSessionFactory();
    }
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
