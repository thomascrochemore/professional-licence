import AppRoutes from '../component/routes';

const RouterService = {
  getRouteOf: (location) => {
    return AppRoutes.find((route) => route.path === location.pathname);
  }
};

export default RouterService;
