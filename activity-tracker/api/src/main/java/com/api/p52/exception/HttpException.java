package com.api.p52.exception;

import com.api.p52.model.ErrorMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class HttpException extends WebApplicationException{

    public HttpException(){
        this("Internal error");
    }

    public HttpException(String message){
        this(message,500);
    }

    public HttpException(String message,int status){
        super(Response.status(status).entity(new ErrorMessage(message,status)).build());
    }

    public HttpException(String message,Response.Status status){
        super(Response.status(status).type(MediaType.APPLICATION_JSON).entity(new ErrorMessage(message,status.getStatusCode())).build());
    }
}
