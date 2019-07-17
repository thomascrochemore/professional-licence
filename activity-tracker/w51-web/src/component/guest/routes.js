import Home from './home/home';
import Register from './register/register';
import Signin from './signin/signin';

const GuestRoutes = [
  {
    path: "/",
    component: Home
  },
  {
    path: "/register",
    component: Register
  },
  {
    path: "/signin",
    component: Signin
  }
];
export default GuestRoutes;
