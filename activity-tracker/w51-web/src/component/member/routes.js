import Welcome from './welcome/welcome';
import MemberUserRoutes from './user/routes'
import MemberActivityRoutes from './activity/routes';
import MemberSessionRoutes from './session/routes';

const MemberRoutes = [
  {
    path: "/s/welcome",
    component: Welcome
  },
  ...MemberUserRoutes,
  ...MemberActivityRoutes,
  ...MemberSessionRoutes
];
export default MemberRoutes;
