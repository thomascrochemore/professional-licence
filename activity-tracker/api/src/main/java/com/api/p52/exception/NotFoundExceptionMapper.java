package com.api.p52.exception;

import com.api.p52.model.ErrorMessage;
import com.sun.jersey.api.NotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException>{
    @Override
    public Response toResponse(NotFoundException throwable) {
        return Response
                .status(404)
                .entity(new ErrorMessage("Page Not Found",404))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
