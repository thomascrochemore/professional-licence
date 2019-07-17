package com.api.p52.model;

import javax.persistence.Transient;

public class UserRegister extends User{
    @Transient
    private String password;
    @Transient
    private String confpassword;

    public UserRegister(){
        super();
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
