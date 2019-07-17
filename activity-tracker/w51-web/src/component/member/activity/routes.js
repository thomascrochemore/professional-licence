import ListActivity from './list-activity/list-activity';
import Activity from './activity/activity';
import CreateActivity from './create-activity/create-activity';

const MemberActivityRoutes = [
  {
    path: "/s/activities",
    component: ListActivity
  },
  {
    path: `/s/activity/:activityId`,
    component: Activity
  },
  {
    path: '/s/create-activity',
    component: CreateActivity
  }
];
export default MemberActivityRoutes;
