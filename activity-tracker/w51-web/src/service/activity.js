import RestService from './rest'
import UserService from './user';

const ActivityService = {
  findAll: () => {
    return RestService.get("/member/activity");
  },
  findOne: (activityId) => {
    return RestService.get(`/member/activity/${activityId}`);
  },
  findOneWithProperties: (activityId) => {
    let activityResult;
    return ActivityService.findOne(activityId)
    .then((activity) => {
      activityResult = activity;
      return ActivityService.findPropertiesOfActivity(activityId);
    })
    .then((properties) => {
      activityResult.properties = properties;
      return activityResult;
    });
  },
  findPropertiesOfActivity: (activityId) => {
    return RestService.get(`/member/activity/${activityId}/property`);
  },
  createActivity: (activity) => {
    return RestService.post("/member/activity",activity);
  }
};
export default ActivityService;
