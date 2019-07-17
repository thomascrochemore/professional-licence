import GuestRoutes from './guest/routes';
import MemberRoutes from './member/routes';

const AppRoutes = [
  ...GuestRoutes,
  ...MemberRoutes
];
export default AppRoutes;
