package com.api.p52.dao;

import com.api.p52.hibernate.CrudDAO;
import com.api.p52.model.User;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
public class UserDAO extends CrudDAO<User,Long>{
    public UserDAO() {
        super(User.class,Long.class);
    }

    public User findOneByLogin(String login){
        return findOneBy("login",login);
    }
}
