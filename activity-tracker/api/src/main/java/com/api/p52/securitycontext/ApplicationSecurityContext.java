package com.api.p52.securitycontext;

import com.api.p52.model.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

public class ApplicationSecurityContext implements SecurityContext {

    public static final  String GUEST = "GUEST";
    public static final String MEMBER = "MEMBER";
    public static final String ADMIN = "ADMIN";

    private static final List<String> ROLES = Arrays.asList(new String[]{
            GUEST,
            MEMBER,
            ADMIN
    });

    private User user;
    private String scheme;

    public static boolean isValidRole(String role){
        return ROLES.contains(role.toUpperCase());
    }
    public  ApplicationSecurityContext(User user,String scheme){
        this.user = user;
        this.scheme = scheme;
    }
    @Override
    public Principal getUserPrincipal() {
        return this.user;
    }

    @Override
    public boolean isUserInRole(String role) {
        if(role.toUpperCase().equals(GUEST)){
            return  true;
        }
        else if(user == null){
            return  false;
        }
        return user.hasRole(role);
    }

    @Override
    public boolean isSecure() {
        return scheme.toLowerCase().equals("https");
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }
}
