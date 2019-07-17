package com.api.p52.filter;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import javax.ws.rs.core.MultivaluedMap;

public class CorsFilter implements ContainerResponseFilter {
    @Override
    public ContainerResponse filter(ContainerRequest containerRequest, ContainerResponse containerResponse) {
        containerResponse.getHttpHeaders().add("Access-Control-Allow-Origin", "*");
        containerResponse.getHttpHeaders().add("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization");
        containerResponse.getHttpHeaders().add("Access-Control-Allow-Credentials", "true");
        containerResponse.getHttpHeaders().add("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        return containerResponse;
    }
}
