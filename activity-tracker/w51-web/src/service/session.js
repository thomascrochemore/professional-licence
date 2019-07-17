import RestService from './rest';
import UserService from './user';
import ActivityService from './activity';

const SessionService = {
  findMySessions: () => {
    return RestService.get("/member/session").then((sessions) => {
      return SessionService.getSessionsWithActivitiesAndUser(sessions);
    });
  },
  findByUser: (userId) => {
    return RestService.get(`/member/session/${userId}/user`).then((sessions) => {
      return SessionService.getSessionsWithActivitiesAndUser(sessions);
    });
  },
  findByUserAndActivity: (userId,activityId) => {
    return RestService.get(`/member/session/${userId}/user/${activityId}/activity`).then((sessions) => {
      return SessionService.getSessionsWithActivitiesAndUser(sessions);
    });
  },
  findOne: (sessionId) => {
    let session;
    return RestService.get(`/member/session/${sessionId}`).then((sess) => {
      session = sess;
      return RestService.get(`/member/session/${sessionId}/property`);
    }).then((sessionProperties) => {
      session.sessionProperties = sessionProperties.map((prop) =>{
        return Object.assign(prop,{
          value_bool: prop.valueBool,
          value_string: prop.valueString,
          value_number: prop.valueNumber
        })
      });
      session.date = new Date(session.date);
      return UserService.findOne(session.userId);
    }).then((user) => {
      session.user = user;
      return ActivityService.findOne(session.activityId);
    }).then((activity) => {
      session.activity = activity;
      return session;
    });
  },
  getSessionsWithActivitiesAndUser: (sessions) => {
    return new Promise((resolve,reject) => {
      let isRejected = false;
      if(sessions.length == 0){
        resolve(sessions);
      }
      let nbSetSessions = 0;
      let next = Promise.resolve();
      sessions.forEach((session) => {
       session.date = new Date(session.date);
       if(!isRejected)
        next = next.then(() => { return UserService.findOne(session.userId).then((user) => {
         session.user = user;
         if(!isRejected)
         return ActivityService.findOne(session.activityId)
       }).then((activity) => {
         session.activity = activity;
         if(!isRejected)
         return ++nbSetSessions;
       }).then((nbSetSessions) =>{
         if(!isRejected)
         if(nbSetSessions >= sessions.length){
           resolve(sessions);
         }
       }).catch((err) => {
         if(!isRejected){
           isRejected = true;
           reject(err);
         }
       });
     });
     });
    });
  },
  createSession: (session) => {
    return RestService.post("/member/session",session);
  },
  updateSession: (session) =>{
    let mySession = Object.assign({},session);
    return new Promise((resolve,reject) =>{
      let sessionProperties = session.properties;
      mySession.properties = undefined;
      let sessionId = session.id;
      mySession.id = undefined
      RestService.put(`/member/session/${sessionId}`,mySession).then((sess) =>{
        let nbProps = 0;
        sessionProperties.forEach((prop) => {
          let myProp = {
            value_bool: prop.value_bool,
            value_string: prop.value_string,
            value_number: prop.value_number
          }
          RestService.put(`/member/session/${sessionId}/session/${prop.propertyId}/property`,myProp).then(() =>{
            return ++nbProps;
          })
          .then((nbProps) =>{
            if(nbProps >= sessionProperties.length){
              resolve(sess)
            }
          })
          .catch((err) => {
            reject((err));
          })
        })
      })
      .catch((err) =>{
        reject(err);
      });
    })
  }
};
export default SessionService;
