package com.api.p52.resource.member;

import com.api.p52.dao.*;
import com.api.p52.exception.ForbidenException;
import com.api.p52.exception.NotFoundException;
import com.api.p52.model.*;
import com.api.p52.resource.BaseResource;
import com.api.p52.validator.SessionValidator;
import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/member/session")
public class SessionResource extends BaseResource {
    @InjectParam
    UserDAO userDAO;
    @InjectParam
    ActivityDAO activityDAO;
    @InjectParam
    SessionDAO sessionDAO;
    @InjectParam
    SessionValidator sessionValidator;
    @InjectParam
    SessionPropertyDAO sessionPropertyDAO;
    @InjectParam
    PropertyDAO propertyDAO;

    /**
     * get yours sessions
     * , need authentication
     * @return sessions with id, date's timestamp, userId, activityId
     */
    @GET
    @Path("/")
    @Produces (MediaType.APPLICATION_JSON)
    public List<Session> getMySessions(){
        return sessionDAO.findByUser(getUser());
    }

    /**
     * get one session of {sessionId} id
     * , need authentication
     * @param sessionId id of session
     * @return session with id, date's timestamp, userId, activityId
     */
    @GET
    @Path("/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Session getOneSession(@PathParam("sessionId") Long sessionId){
        Session session = sessionDAO.findOne(sessionId);
        if(session == null){
            throw new NotFoundException("Session not found.");
        }
        return session;
    }

    /**
     * get session properties of session with {sessionId} id
     * , need authentication
     * @param sessionId id of session
     * @return session properties with id, value_bool, value_number, value_string, session and activityProperty
     */
    @GET
    @Path("/{sessionId}/property")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SessionProperty> getSessionProperties(@PathParam("sessionId") Long sessionId){
        Session session = sessionDAO.findOne(sessionId);
        if(session == null){
            throw new NotFoundException("Session not found.");
        }
        return sessionPropertyDAO.findBySession(session);
    }

    /**
     * create new session with properties
     * , need authentication
     * @param sessionRequest session with activityId, date's timestamp and properties
     * @return session created
     */
    @POST
    @PathParam("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Session createSession(SessionRequest sessionRequest){
        sessionValidator.createSessionValidator(sessionRequest);
        Activity activity = activityDAO.findOne(sessionRequest.getActivityId());
        Session session = new Session(sessionRequest.getDate(),getUser(),activity);
        List<SessionProperty> properties = new ArrayList<>();
        for(SessionPropertyRequest prop : sessionRequest.getProperties()){
            Property property = propertyDAO.findOne(prop.getPropertyId());
            SessionProperty sessionProperty = new SessionProperty();
            sessionProperty.setProperty(property);
            sessionProperty.setSession(session);
            sessionProperty.setValueBool(prop.getValue_bool());
            sessionProperty.setValueNumber(prop.getValue_number());
            sessionProperty.setValueString(prop.getValue_string());
            properties.add(sessionProperty);
        }
        sessionDAO.insert(session);
        sessionPropertyDAO.insertAll(properties);
        return session;
    }

    /**
     * update a session of {sessionId} id
     * , need authentication
     * @param sessionId id of session you want to update
     * @param request session with date's timestamp, activityId and properties
     * @return updated session
     */
    @PUT
    @Path("/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Session updateSession(@PathParam("sessionId") Long sessionId, SessionRequest request){
        Session session = sessionDAO.findOne(sessionId);
        if(session == null){
            throw new NotFoundException("Session not found");
        }
        if(!session.getUser().getId().equals(getUser().getId())){
            throw new ForbidenException("This session do not belong to you.");
        }
        request.setId(sessionId);
        sessionValidator.updateSessionValidator(getUser(),request);
        Activity activity = activityDAO.findOne(request.getActivityId());
        session.setActivity(activity);
        session.setDate(request.getDate());
        sessionDAO.update(session);
        return  session;
    }

    /**
     * update a sessionProperty with {sessionId} session's id and {propertyId} activity property's id
     * , need authentication
     * @param sessionId id of session
     * @param propertyId id of property
     * @param request session with date's timestamp, activityId and properties
     * @return updated session property
     */
    @PUT
    @Path("/{sessionId}/session/{propertyId}/property")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SessionProperty updateSessionProperty(@PathParam("sessionId")Long sessionId,
                                          @PathParam("propertyId")Long propertyId,
                                            SessionPropertyRequest request){
        Property property = propertyDAO.findOne(propertyId);
        if(property == null){
            throw new NotFoundException("Not found property");
        }
        Session session = sessionDAO.findOne(sessionId);
        if(session == null){
            throw new NotFoundException("Not found session");
        }
        if(!session.getUser().getId().equals(getUser().getId())){
            throw new ForbidenException("This session do not belong to you.");
        }
        SessionProperty sessionProperty = sessionPropertyDAO.findOneBySessionIdAndPropertyId(sessionId,propertyId);
        if(sessionProperty == null){
            throw new NotFoundException("Not found session property");
        }
        sessionValidator.sessionPropertyValidator(request,property);
        sessionProperty.setValueString(request.getValue_string());
        sessionProperty.setValueNumber(request.getValue_number());
        sessionProperty.setValueBool(request.getValue_bool());
        sessionPropertyDAO.update(sessionProperty);
        return sessionProperty;
    }

    /**
     * delete session of {sessionId} id
     * , need authentication
     * @param sessionId id of session
     * @return success message
     */
    @DELETE
    @Path("/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public SuccessMessage deleteSession(@PathParam("sessionId") Long sessionId){
        Session session = sessionDAO.findOne(sessionId);
        if(session == null){
            throw new NotFoundException("Session not found.");
        }
        List<SessionProperty> sessionProperties = sessionPropertyDAO.findBySession(session);
        sessionPropertyDAO.deleteAll(sessionProperties);
        sessionDAO.delete(session);
        return new SuccessMessage("Session deleted");
   }

    /**
     * get sessions by user with {userId} user id
     * , need authentication
     * @param userId user id
     * @return sessions
     */
    @GET
    @Path("/{userId}/user")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Session> getSessionsByUser(@PathParam("userId") Long userId){
        if(userDAO.findOne(userId) == null){
            throw new NotFoundException("User not found.");
        }
        return sessionDAO.findByUserId(userId);
    }

    /**
     * get sessions by user and activity with {userId} user id and {activityId} activity id
     * , need authentication
     * @param userId user id
     * @param activityId activity id
     * @return sessions
     */
    @GET
    @Path("/{userId}/user/{activityId}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Session> getSessionsByUserAndActivity(@PathParam("userId") Long userId,@PathParam("activityId") Long activityId){
        if(userDAO.findOne(userId) == null){
            throw new NotFoundException("User not found.");
        }
        if(activityDAO.findOne(activityId) == null){
            throw new NotFoundException("Activity not found.");
        }
        return sessionDAO.findByUserIdAndActivityId(userId,activityId);
    }
}
