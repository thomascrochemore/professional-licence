package com.api.p52.filter;

import com.api.p52.exception.ForbidenException;
import com.api.p52.exception.UnauthorizedException;
import com.api.p52.model.User;
import com.api.p52.securitycontext.ApplicationSecurityContext;
import com.api.p52.service.AuthService;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;
import java.security.Key;
import java.util.List;

public class AuthFilter implements ContainerRequestFilter {
    @Context
    UriInfo uriInfo;

    @InjectParam
    AuthService authService;

    @Override

    public ContainerRequest filter(ContainerRequest containerRequest) {
        List<PathSegment> segments = uriInfo.getPathSegments();
        User user = null;
        String authorizationHeader = containerRequest.getHeaderValue(HttpHeaders.AUTHORIZATION);
        String method = containerRequest.getMethod();
        if(method.toUpperCase().equals("OPTIONS")){
            return containerRequest;
        }
        if(authorizationHeader != null && !authorizationHeader.isEmpty()){
            String token = authorizationHeader.substring("Bearer".length()).trim();
            user = authService.authenticate(token);
        }
        containerRequest.setSecurityContext(new ApplicationSecurityContext(user,uriInfo.getRequestUri().getScheme()));
        if(segments.size() == 0){
            return containerRequest;
        }
        String prefix = segments.get(0).getPath();
        if(!ApplicationSecurityContext.isValidRole(prefix)){
            return containerRequest;
        }
        if(!containerRequest.getSecurityContext().isUserInRole(prefix)) {
            if(user == null) {
                throw new UnauthorizedException();
            }else{
                throw new ForbidenException();
            }
        }
        return containerRequest;
    }
}