package org.acrobatt.project.controllers;

import org.acrobatt.project.dto.AuthenticationBody;
import org.acrobatt.project.except.UserNotFoundException;
import org.acrobatt.project.services.auth.AuthenticationService;
import org.acrobatt.project.utils.ResponseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.sasl.AuthenticationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;

@Path("/")
public class AuthenticationController {

    private static Logger logger = LogManager.getLogger(AuthenticationController.class);
    private static AuthenticationService authenticationService = AuthenticationService.getInstance();

    @Context UriInfo uriInfo;

    /**
     * Returns the URI leading to this resource
     * @return the URI
     */
    public URI getUri()
    {
        return uriInfo.getBaseUriBuilder().path(this.getClass()).build();
    }

    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(AuthenticationBody body) {
        logger.debug(String.format("[%1s,%2s]",body.getUsername(),(body.getPassword() == null ? "null" : "********")));
        try {
            //authenticate the user and return if it passes
            String token = authenticationService.signIn(body.getUsername(), body.getPassword(), uriInfo);
            return ResponseUtils.createResponse(202, "{ \"token\": \""+token+"\" }");

        } catch(UserNotFoundException e) {
            return ResponseUtils.createResponse(401, "{ \"err\": 401, \"message\": \"Aucun compte n'existe pour cet identifiant\" }");
        } catch(NullPointerException e) {
            return ResponseUtils.createResponse(401, "{ \"err\": 401, \"message\": \"Informations manquantes\" }");
        } catch(AuthenticationException e) {
            return ResponseUtils.createResponse(401, "{ \"err\": 401, \"message\": \"Identifiant ou mot de passe incorrect\" }");
        } catch(IOException e) {
            return ResponseUtils.createResponse(500, "{ \"err\": 500, \"message\": \"Erreur lors du hashing. Contactez l'administrateur.\" }");
        }
    }


    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(AuthenticationBody body) {
        logger.debug(String.format("[%1s,%2s]",body.getUsername(),(body.getPassword() == null ? "null" : "********")));
        try {

            String token = authenticationService.signUp(body.getUsername(), body.getPassword(), uriInfo);
            return ResponseUtils.createResponse(202, "{ \"token\": \""+token+"\" }");

        } catch(UserNotFoundException e) {
            return ResponseUtils.createResponse(401, "{ \"err\": 401, \"message\": \"Aucun compte n'existe pour cet identifiant\" }");
        } catch(NullPointerException e) {
            return ResponseUtils.createResponse(401, "{ \"err\": 401, \"message\": \"Informations manquantes\" }");
        } catch(AuthenticationException e) {
            return ResponseUtils.createResponse(401, "{ \"err\": 401, \"message\": \"Identifiant ou mot de passe incorrect\" }");
        } catch(IOException e) {
            return ResponseUtils.createResponse(500, "{ \"err\": 500, \"message\": \"Erreur lors du hashing. Contactez l'administrateur.\" }");
        }
    }
}
