package com.example.thomas.p54_mobile.model;

import java.util.ArrayList;
import java.util.List;


public class User
{
    private Long id;
    private String login;
    private String firstname;
    private String lastname;

    private List<String> roles;

    public User() {	roles = new ArrayList<String>(); }

    public User(String login, String firstname, String lastname) {
        this();
        this.login = login;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(User user){
        this(user.getLogin(),user.getFirstname(),user.getLastname());
    }

    public Long getId()
    {
        return this.id;
    }

    private void setId(Long id)
    {
        this.id = id;
    }

    public String getLogin()
    {
        return this.login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getFirstname()
    {
        return this.firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getLastname()
    {
        return this.lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public void addRole(String role) {
        if(!hasRole(role))
        {
            this.roles.add(role.toUpperCase());
        }
    }

    public void removeRole(String role){
        this.roles.remove(role.toUpperCase());
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean hasRole(String role){
        return this.roles.contains(role.toUpperCase());
    }
}

