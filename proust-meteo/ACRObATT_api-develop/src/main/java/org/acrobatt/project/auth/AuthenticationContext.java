package org.acrobatt.project.auth;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.security.Principal;

public class AuthenticationContext implements SecurityContext {

    @Context UriInfo uriInfo;
    private Long uid;

    /**
     * Instantiates a new AuthenticationContext
     * @param uid user identifier
     */
    public AuthenticationContext(Long uid) {
        this.uid = uid;
    }

    /**
     * creates a Principal that contains the required info for the JAX-RS resource classes
     * @return a Principal containing the user ID
     * @see Principal
     */
    @Override
    public Principal getUserPrincipal() {
        return () -> uid.toString();
    }

    /**
     * Returns true if the user has the supplied role
     * @param s role of the user (unused)
     * @return true if the user has the role, false if not
     */
    @Override
    public boolean isUserInRole(String s) {
        return true;
    }

    /**
     * Returns true if the URI starts with HTTPS (unused)
     * @return true if HTTPS is used, false if not
     */
    @Override
    public boolean isSecure() {
        return uriInfo.getAbsolutePath().toString().startsWith("https");
    }

    /**
     * Returns the scheme used for authentication
     * @return token-based authentication scheme only
     */
    @Override
    public String getAuthenticationScheme() {
        return "Token-Based-Auth-Scheme";
    }
}
