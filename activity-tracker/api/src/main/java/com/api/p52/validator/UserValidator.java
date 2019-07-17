package com.api.p52.validator;

import com.api.p52.dao.UserDAO;
import com.api.p52.exception.BadRequestException;
import com.api.p52.model.User;
import com.api.p52.model.UserCredentials;
import com.api.p52.model.UserRegister;
import com.api.p52.service.AuthService;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
public class UserValidator extends BaseValidator{
    @InjectParam
    private AuthService authService;
    @InjectParam
    private UserDAO userDAO;

    public void registerValidator(UserRegister user){
        stringNotBlankMinMaxFieldValidator("login",user.getLogin(),3,20);
        stringNotBlankMinMaxFieldValidator("first name",user.getFirstname(),3,20);
        stringNotBlankMinMaxFieldValidator("last name",user.getLastname(),3,20);
        stringNotBlankMinMaxFieldValidator("password",user.getPassword(),6,20);
        loginNotExistsValidator(user.getLogin());
        confPasswordValidator(user);
    }
    public void updateValidator(UserRegister user){
        stringNotBlankMinMaxFieldValidator("first name",user.getFirstname(),3,20);
        stringNotBlankMinMaxFieldValidator("last name",user.getLastname(),3,20);
        if(user.getPassword() != null && !user.getPassword().isEmpty()) {
            stringNotBlankMinMaxFieldValidator("password", user.getPassword(), 6, 20);
            confPasswordValidator(user);
        }
    }

    public void signinValidator(UserCredentials credentials){
        notNullFieldValidator("login",credentials.getLogin());
        notNullFieldValidator("password",credentials.getPassword());
        authCredentialValidator(credentials);
    }
    private void loginNotExistsValidator(String login){
        User user = userDAO.findOneByLogin(login);
        if(user != null){
            throw new BadRequestException("Login already exists.");
        }
    }
    private void authCredentialValidator(UserCredentials credentials){
        if(!authService.isAuthenticate(credentials)){
            throw new BadRequestException("Username or password incorrect.");
        }
    }
    private void confPasswordValidator(UserRegister register){
        if(!register.getPassword().equals(register.getConfpassword())){
            throw new BadRequestException("Passwords must be the same.");
        }
    }

}
