package com.api.p52.exception;

import com.api.p52.model.ErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

//@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable>{
    @Override
    public Response toResponse(Throwable throwable) {
        throwable.printStackTrace();
        return Response
                .status(500)
                .entity(new ErrorMessage("Internal Error",500))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
