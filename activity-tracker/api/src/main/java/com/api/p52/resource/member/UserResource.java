package com.api.p52.resource.member;

import com.api.p52.dao.UserDAO;
import com.api.p52.exception.NotFoundException;
import com.api.p52.model.User;
import com.api.p52.model.UserRegister;
import com.api.p52.resource.BaseResource;
import com.api.p52.validator.UserValidator;
import com.sun.jersey.api.core.InjectParam;
import org.mindrot.jbcrypt.BCrypt;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/member/user")
public class UserResource extends BaseResource {
    @InjectParam
    UserDAO userDAO;

    @InjectParam
    UserValidator userValidator;

    /**
     * return your account's user
     * , need authentication
     * @return yours account's user with id, login, firstname, lastname and roles
     */
    @GET
    @Path("/whoami")
    @Produces(MediaType.APPLICATION_JSON)
    public User whoami(){
        return  getUser();
    }

    /**
     * update your's user account
     * , need authentication
     * @param updateUser user with id, login, firstname, lastname, optional password, confirm password if password is fill out
     * @return yours account updated with login, firstname, lastname and roles
     */
    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User updateMyUser(UserRegister updateUser){
        userValidator.updateValidator(updateUser);
        User user = getUser();
        user.setFirstname(updateUser.getFirstname());
        user.setLastname(updateUser.getLastname());
        if(updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()){
            user.setPassCrypt(BCrypt.hashpw(updateUser.getPassword(),BCrypt.gensalt()));
        }
        userDAO.update(user);
        return user;
    }

    /**
     * get all users
     * , need authentication
     * @return all users with id, login, firstname,lastname and roles
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers(){
     return userDAO.findAll();
    }

    /**
     * get one user of {userId} id
     * , need authentication
     * @param userId id of user
     * @return the user asks with id, login, firstname, lastname and roles
     */
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getOneUser(@PathParam("userId") Long userId){
        User user = userDAO.findOne(userId);
        if(user == null){
            throw new NotFoundException("User not found.");
        }
        return user;
    }
}
