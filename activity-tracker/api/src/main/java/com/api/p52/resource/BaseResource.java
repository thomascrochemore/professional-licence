package com.api.p52.resource;

import com.api.p52.model.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class BaseResource {
    @Context
    protected SecurityContext context;

    public User getUser(){
        return (User) context.getUserPrincipal();
    }
}
