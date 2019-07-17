package org.acrobatt.project.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.acrobatt.project.auth.AuthenticationContext;
import org.acrobatt.project.dao.mysql.UserDAO;
import org.acrobatt.project.model.mysql.User;
import org.acrobatt.project.utils.ResponseUtils;
import org.acrobatt.project.utils.auth.JWTAuthKeyGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.io.StringReader;

public class AuthenticationFilter implements ContainerRequestFilter {

    Logger logger = LogManager.getLogger(this);

    /**
     * Filters the incoming request, accepting it if the JWT is valid, and refusing it if not
     * @param context current context of the request (metadata)
     * @throws IOException if something happened during filtering
     */
    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        try {
            //get token
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION)
                    .substring("Bearer".length())
                    .trim();
            //get payload from token
            String payload = verifyToken(token);

            //verify the contents and save the username for the securityContext
            JsonNode node = new ObjectMapper().readTree(new StringReader(payload));
            Long uid = node.get("uid").longValue();
            String username = node.get("username").textValue();

            if(!hasValidPayload(uid, username)) throw new MalformedJwtException("ID/Username mismatch");
            //set the security context (identifying the user himself)
            AuthenticationContext ctx = new AuthenticationContext(uid);
            context.setSecurityContext(ctx);

        } catch(SignatureException | ExpiredJwtException | MalformedJwtException e) {
            logger.error("Authentication exception while filtering JWT", e);
            context.abortWith(ResponseUtils.createResponse(401,"{ \"err\": \"Token invalide ou expiré\" }"));
        } catch(NullPointerException e) {
            context.abortWith(ResponseUtils.createResponse(401, "{ \"err\": \"Veuillez vous connecter pour accéder à cette page\" }"));
        }
    }

    /**
     * Verifies the supplied token using the server signing key
     * @param token supplied JWT
     * @return the subject contained inside the JWT
     * @throws SignatureException if the JWT wasn't signed by the server
     * @throws ExpiredJwtException if the JWT has expired
     * @throws MalformedJwtException if the JWT has an invalid payload / is invalid
     */
    //verifies the token and returns the subject if the token is valid
    private String verifyToken(String token) throws SignatureException, ExpiredJwtException, MalformedJwtException {
        //retrieve the payload
        return Jwts.parser()
                .setSigningKey(JWTAuthKeyGenerator.getKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Checks if the JWT payload is valid
     * @param uid the ID of the user
     * @param username the username of the user
     * @return true if the user ID corresponds to the username, false if not
     */
    private boolean hasValidPayload(Long uid, String username) {
        try {
            UserDAO dao = UserDAO.getInstance();
            User u = dao.getById(uid);
            return u.getUsername().equals(username);
        } catch(NullPointerException e) {
            return false;
        }
    }
}
