package com.api.p52.dao;

import com.api.p52.hibernate.CrudDAO;
import com.api.p52.model.Activity;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
public class ActivityDAO extends CrudDAO<Activity,Long> {
    public ActivityDAO() {
        super(Activity.class, Long.class);
    }

    public Activity findOneByName(String name){
        return findOneBy("name",name);
    }
}
