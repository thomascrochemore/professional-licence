package org.acrobatt.project.services.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.acrobatt.project.dao.mysql.UserDAO;
import org.acrobatt.project.except.UserNotFoundException;
import org.acrobatt.project.model.mysql.User;
import org.acrobatt.project.utils.auth.JWTAuthKeyGenerator;
import org.acrobatt.project.utils.auth.PBKDF2Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonValue;
import javax.security.sasl.AuthenticationException;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.security.Key;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class AuthenticationService {

    private static Logger logger = LogManager.getLogger(AuthenticationService.class);


    /**
     * unique generated signing key (valid for the entire run time of the application)
     */
    private Key key = JWTAuthKeyGenerator.getKey();


    private UserDAO udao = UserDAO.getInstance();
    private static AuthenticationService instance = null;

    /**
     * Generates an instance of this service
     * @return an instance of the service
     */
    public static AuthenticationService getInstance() {
        if(instance == null) instance = new AuthenticationService();
        return instance;
    }

    /**
     * Registers a user based on his information, supplied inside a JSON object
     * @param username username of the user
     * @param password password of the user
     * @return a unique signed JWT
     */
    public String signUp(String username, String password, UriInfo uriInfo) throws UserNotFoundException, IOException {
        if(username == null || password == null) throw new NullPointerException("Parameter check failed");

        //list all users and loop to find redundant values
        List<User> users = udao.getAll();
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                logger.warn("Conflict: found existing user with username ["+u.getUsername()+"]");
                throw new IllegalArgumentException(String.format("User [%1s] already exists",username));
            }
        }
        String hashpass = PBKDF2Utils.hashPassword(password);

        //insert the new user in the database
        udao.insert(new User(username, hashpass));

        //authenticate the user and return if it passes
        if(!hasValidCredentials(username,password)) throw new AuthenticationException("Invalid credentials");
        return issueToken(username, uriInfo);
    }

    /**
     * Authenticates a user based on his username and password, supplied inside a JSON object
     * @param username username of the user
     * @param password password of the user
     * @return a unique signed JWT
     */
    public String signIn(String username, String password, UriInfo uriInfo) throws UserNotFoundException, IOException {
        if(username == null || password == null) throw new NullPointerException("Parameter check failed");

        if(!hasValidCredentials(username,password)) throw new AuthenticationException("Invalid credentials");
        return issueToken(username, uriInfo);
    }

    /**
     * Utility function verifying his password against the hashed one stored in the database
     * @param username username of the user
     * @param password password of the user
     * @throws AuthenticationException (unused)
     * @throws UserNotFoundException if the user does not exist in the database
     * @throws IOException if something bad happened while verifying the password
     */
    private boolean hasValidCredentials(String username, String password) throws UserNotFoundException, IOException, NullPointerException {
        //return user in database (following operations will fail if none is found)
        User u = udao.getByUsername(username);
        if(u == null) {
            logger.warn(String.format("Could not retrieve user of username [%1s]",username));
            throw new UserNotFoundException();
        }

        //get stored hash
        String hashpass = u.getPassword();

        //compare plain-text password to stored hash
        return PBKDF2Utils.verifyPassword(password, hashpass);
    }


    /**
     * Generates a JWT based on the username of the user
     * @param username username of the authenticating user
     * @return a unique, signed JWT
     */
    private String issueToken(String username, UriInfo uriInfo) throws UserNotFoundException {
        //use the UserDAO to retrieve the database information and create the JWT from it
        User u = udao.getByUsername(username);
        if(u == null) {
            logger.fatal(String.format("Fatal exception occurred : could not retrieve user of username [%1s]", username));
            throw new UserNotFoundException();
        }

        //generate the JSON object with uid and username (enough to identify somebody)
        JsonBuilderFactory jfac = Json.createBuilderFactory(null);
        JsonValue jval = jfac.createObjectBuilder()
                .add("uid", u.getId())
                .add("username", u.getUsername())
                .build();

        //generate a key, then use it to generate the token with subject, issuer, issued date and expiration, signed using HS256
        return Jwts.builder()
                .setSubject(jval.toString())
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Timestamp.from(Instant.now().plus(30, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
