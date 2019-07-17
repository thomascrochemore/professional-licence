import MySessions from './list-session/my-sessions';
import UserSessions from './list-session/user-sessions';
import ActivitySessions from './list-session/activity-sessions';
import Session from './session/session';
import CreateSession from './form-session/create-session';
import EditSession from './form-session/edit-session';

const MemberSessionRoutes = [
  {
    path: "/s/my-sessions",
    component: MySessions
  },
  {
    path: "/s/user-sessions/:userId",
    component: UserSessions
  },
  {
    path: "/s/activity-sessions/:activityId",
    component: ActivitySessions
  },
  {
    path: "/s/session/:sessionId",
    component: Session
  },
  {
    path: "/s/create-session/:activityId",
    component: CreateSession
  },
  {
    path: "/s/edit-session/:sessionId",
    component: EditSession
  }
];
export default MemberSessionRoutes;
