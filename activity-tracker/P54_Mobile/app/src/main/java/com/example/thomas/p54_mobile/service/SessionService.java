package com.example.thomas.p54_mobile.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.Activity;
import com.example.thomas.p54_mobile.model.Session;
import com.example.thomas.p54_mobile.model.SessionProperty;
import com.example.thomas.p54_mobile.model.SessionRequest;
import com.example.thomas.p54_mobile.model.SessionWithProperties;

/**
 * Created by thomas on 09/01/2018.
 */

public class SessionService
{
    public static Session createSession(SessionRequest createSession) throws HttpException
    {
        return RestService.post("/member/session",createSession,Session.class);
    }
    
    public static Session updateSession(Long sessionId,SessionRequest updateSession) throws HttpException {
    	return RestService.put("/member/session/"+sessionId,updateSession,Session.class);
    }
    
    public static void deleteSession(Long sessionId) throws HttpException {
    	RestService.delete("/member/session/"+sessionId,String.class);
    }
    
    public static SessionWithProperties findOne(Long sessionId) throws HttpException {
    	SessionWithProperties session =  new SessionWithProperties(RestService.get("/member/session/"+sessionId,Session.class));
    	session.setProperties(findSessionProperties(sessionId));
    	return session;
    }
    public static List<Session> findMySessions() throws HttpException{
    	return getSessionsWithActivitiesAndUser(RestService.get("/member/session",Session[].class));
    }
    public static List<Session> findByUser(Long userId) throws HttpException {
    	return getSessionsWithActivitiesAndUser(RestService.get("/member/session/"+userId+"/user",Session[].class));
    }
    
    public static List<Session> findByUserAndActivity(Long userId,Long activityId) throws HttpException{
    	return getSessionsWithActivitiesAndUser(RestService.get("/member/session/"+userId+"/user/"+activityId+"/activity",Session[].class));
    }
    
    public static List<Session> getSessionsWithActivitiesAndUser(Session[] sessions) throws HttpException{
    	List<Session> result = new ArrayList(Arrays.asList(sessions));
    	for(Session session : result) {
    		setActivityAndUser(session);
    	}
    	return result;
    }
    
    public static List<SessionProperty> findSessionProperties(Long sessionId) throws HttpException{
    	return new ArrayList(Arrays.asList(RestService.get("/member/session/"+sessionId+"/property",SessionProperty[].class)));
    }
    
    public static Session setActivityAndUser(Session session) throws HttpException {
    	session.setActivity(ActivityService.findOne(session.getActivityId()));
    	return session;
    }
}
