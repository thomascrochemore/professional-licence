package com.example.thomas.p54_mobile.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;

public class Session
{
    private Long id;
    private Date date;
    private Long userId;
    private Long activityId;

    @JsonIgnore
    private User user;

    @JsonIgnore
    private Activity activity;

    public Session() {	  }

    public Session(Date date,User user,Activity activity) {
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

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
}
