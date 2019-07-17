package com.api.p52.dao;

import com.api.p52.hibernate.CrudDAO;
import com.api.p52.model.Activity;
import com.api.p52.model.Property;
import com.sun.jersey.spi.resource.Singleton;

import java.util.List;

@Singleton
public class PropertyDAO extends CrudDAO<Property,Long> {
    public PropertyDAO() {
        super(Property.class, Long.class);
    }

    public List<Property> findByActivityId(Long id){
        return findBy("activity.id",id);
    }
    public List<Property> findByActivity(Activity activity){
        return  findByActivityId(activity.getId());
    }
}
