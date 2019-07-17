package com.api.p52.exception;

import javax.ws.rs.core.Response;

public class UnauthorizedException extends HttpException {

    public UnauthorizedException(){
        super("You have to signin to access this page.", Response.Status.UNAUTHORIZED);
    }
}
