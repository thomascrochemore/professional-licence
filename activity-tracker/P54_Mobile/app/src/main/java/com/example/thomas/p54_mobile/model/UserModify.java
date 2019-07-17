package com.example.thomas.p54_mobile.model;

/**
 * Created by thomas on 11/01/2018.
 */

public class UserModify extends User
{
    private String password;
    private String confpassword;

    public UserModify(){
        super();
    }

    public UserModify(String login,String firstname,String lastname,String password,String confpassword){
        setLogin(login);
        setFirstname(firstname);
        setLastname(lastname);
        setPassword(password);
        setConfpassword(confpassword);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfpassword() {
        return confpassword;
    }

    public void setConfpassword(String confpassword) {
        this.confpassword = confpassword;
    }
}
