import 'whatwg-fetch';

import ApiConfig from '../const/api';

const defaultHeaders = {
  Accept: 'application/json',
  'Content-Type': 'application/json; charset=utf-8'
};
const baseurl = ApiConfig.baseurl;
const RestService = {
  setDefaultHeader: (name,value) => {
    defaultHeaders[name] = value;
  },
  unsetDefaultHeader: (name) => {
    defaultHeaders[name] = undefined;
  },
  request: (path,method = 'GET',headers = {},body = undefined) =>{
    return fetch(baseurl+path,{
      method: method,
      headers: Object.assign(defaultHeaders,headers),
      body: body ? JSON.stringify(body) : undefined
    }).then((resp) => {
      let json = resp.json();
      if (resp.status >= 200 && resp.status < 300) {
        return json;
      } else {
        return json.then((err) => {
          throw err
        });
      }
    });
  },
  get: (path,headers = {}) => {
    return RestService.request(path,'GET',headers);
  },
  post: (path,body,headers = {}) => {
    return RestService.request(path,'POST',headers,body);
  },
  put: (path,body,headers = {}) => {
    return RestService.request(path,'PUT',headers,body);
  },
  delete: (path,headers = {}) => {
    return RestService.request(path,'DELETE',headers);
  }
};

export default RestService;
