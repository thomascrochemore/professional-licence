package com.api.p52.resource.guest;

import com.api.p52.model.TokenInfo;
import com.api.p52.model.User;
import com.api.p52.model.UserCredentials;
import com.api.p52.model.UserRegister;
import com.api.p52.properties.ApplicationProperties;
import com.api.p52.resource.BaseResource;
import com.api.p52.service.AuthService;
import com.api.p52.utilities.DateUtils;
import com.api.p52.validator.UserValidator;
import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;


@Path("/user")
public class UserResource extends BaseResource {
    @InjectParam
    ApplicationProperties properties;

    @InjectParam
    private AuthService authService;
    @InjectParam
    private UserValidator userValidator;


    /**
     * signin with yours login and password, return jwt token infos
     * @param credentials login and password
     * @return jwt token info : token and expiration date's timestamp
     */
    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TokenInfo signin(UserCredentials credentials){
        Date expire = DateUtils.plus(Date.from(Instant.now()),
                Calendar.MINUTE,
                properties.getIntegerProperty("app.jwt.expire"));
        userValidator.signinValidator(credentials);
        String token = authService.issueToken("/signin",credentials.getLogin(),expire);
        TokenInfo tokenInfo = new TokenInfo(token, expire);
        return tokenInfo;
    }

    /**
     * register your account with yours informations, need to confirm password
     * @param userRegister informations, contains login, firstname, lastname, password and password confirmation
     * @return yours  newaccount's user with id, login, firstname, lastname and roles
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User register(UserRegister userRegister){
        userValidator.registerValidator(userRegister);
        User user = authService.register(userRegister);
        return user;
    }

}
