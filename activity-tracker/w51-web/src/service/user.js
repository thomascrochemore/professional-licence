import RestService from './rest'

let tokenExpire = localStorage.getItem('jwt-expire');
let token = localStorage.getItem('jwt-token');
if(!tokenExpire || Date.now() >= Number(tokenExpire)){
  token = null;
}
if(token){
  RestService.setDefaultHeader('Authorization','Bearer '+token);
}
const UserService = {
  identity: () => {
    let expire = localStorage.getItem('jwt-expire');
    if(!expire || Date.now() >= Number(expire)){
      return Promise.resolve(null);
    }
    return RestService.get("/member/user/whoami").then((user) =>{
      return user;
    }).catch((err) => {
      console.log(err);
      return Promise.resolve(null);
    });
  },
  signin: (credentials) => {
    return RestService.post("/user/signin",credentials).then((tokenInfo) => {
      localStorage.setItem('jwt-expire',tokenInfo.expire);
      localStorage.setItem('jwt-token',tokenInfo.token);
      RestService.setDefaultHeader('Authorization','Bearer '+tokenInfo.token);
      return UserService.identity();
    });
  },
  logout: () =>{
    localStorage.removeItem('jwt-expire');
    localStorage.removeItem('jwt-token');
    return new Promise((resolve,reject) =>{
      resolve();
    });
  },
  register: (register) => {
    return RestService.post("/user/register",register).then((user) => {
      return UserService.signin({
        login: register.login,
        password: register.password
      });
    })
  },
  updateMyProfile: (user) => {
    return RestService.put("/member/user",user);
  },
  findAll: () => {
    return RestService.get("/member/user");
  },
  findOne: (userId) =>{
    return RestService.get(`/member/user/${userId}`);
  }
};
export default UserService;
