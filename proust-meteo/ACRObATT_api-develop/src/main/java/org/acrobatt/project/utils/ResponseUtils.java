package org.acrobatt.project.utils;

import javax.ws.rs.core.Response;

public class ResponseUtils {

    private ResponseUtils() {}

    /**
     * Creates a Jersey Response based on an HTTP status and an entity/message to pass to the client
     * @param status the HTTP status code
     * @param entity the message or entity
     * @return the Response object
     */
    public static Response createResponse(int status, Object entity) {
        return Response.status(status).entity(entity).build();
    }

    /**
     * Creates a Jersey Response based on an HTTP status only
     * @param status the HTTP status code
     * @return the Response object
     */
    public static Response createResponse(int status) {
        return Response.status(status).build();
    }
}
