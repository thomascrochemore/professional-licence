import ListUser from './list-user/list-user';
import UserProfile from './profile/user-profile';
import MyProfile from './profile/my-profile';
import EditProfile from './edit-profile/edit-profile';

const MemberUserRoutes = [
  {
    path: "/s/users",
    component: ListUser
  },
  {
    path: "/s/profile/:userId",
    component: UserProfile
  },
  {
    path: "/s/my-profile",
    component: MyProfile
  },
  {
    path: "/s/edit-profile",
    component: EditProfile
  }
];
export default MemberUserRoutes;
