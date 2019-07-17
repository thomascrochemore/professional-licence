package com.api.p52.model;

import com.api.p52.hibernate.ListStringConverter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "USERS" )
public class User implements Principal
{
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;
    private String login;
    private String firstname;
    private String lastname;

    @JsonIgnore
    private String passCrypt;
    @Column
    @Convert(converter = ListStringConverter.class)
    private List<String> roles;

    public User() {	 roles = new ArrayList<>(); }

    public User(String login, String firstname, String lastname)
    {
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

    public void setId(Long id)
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

    @Override
    @JsonIgnore
    public String getName() {
        return this.firstname + " " + this.lastname;
    }

    public void addRole(String role){
        if(!hasRole(role)) {
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

    public String getPassCrypt() {
        return passCrypt;
    }

    public void setPassCrypt(String passCrypt) {
        this.passCrypt = passCrypt;
    }
}

