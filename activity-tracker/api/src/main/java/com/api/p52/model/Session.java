package com.api.p52.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table( name = "SESSIONS" )
public class Session
{
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;
    private Date date;

    @JoinColumn(name="user_id",nullable=false)
    @ManyToOne
    @JsonIgnore
    private User user;

    @JoinColumn(name="activity_id",nullable=false)
    @ManyToOne
    @JsonIgnore
    private Activity activity;

    public Session() {	  }

    public Session(Date date,User user,Activity activity)
    {
        this.user = user;
        this.activity = activity;
        this.date = date;
    }

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getDate()
    {
        return this.date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Long getUserId(){
        return  user == null ? null : user.getId();
    }

    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity){
        this.activity = activity;
    }
    public Long getActivityId(){
        return activity == null ? null : activity.getId();
    }
}
