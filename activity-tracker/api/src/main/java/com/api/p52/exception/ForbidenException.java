package com.api.p52.exception;

import javax.ws.rs.core.Response;

public class ForbidenException extends HttpException {

    public ForbidenException(){
        super("Access denied", Response.Status.FORBIDDEN);
    }
    public ForbidenException(String message){
        super(message,Response.Status.FORBIDDEN);
    }
}
