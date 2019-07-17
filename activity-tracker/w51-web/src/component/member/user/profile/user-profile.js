import React, { Component } from 'react';
import BaseMember from '../../base-member';

import UserDetails from "./user-details";
import UserService from "../../../../service/user";
import NotFound from '../../../common/error/notfound';

export default class UserProfile extends BaseMember {

  constructor(props){
    super(props);
    this.state = Object.assign(this.state,{
      profileUser: {
        firstname: '',
        lastname: ''
      },
      notFound: false
    });
  }
  onUserLoad = (user) => {
    UserService.findOne(this.props.match.params.userId).then((profileUser) => {
      this.setState({
        profileUser: profileUser,
        notFound: false
      });
    }).catch((err) => {
      if(err.status == 404){
        this.setState({
          notFound: true
        });
      }
    })
  };
  render() {
    return this.state.notFound ? (<NotFound/>) :  (
      <div className="row">
        <h1>User Profile</h1>
        <UserDetails profileUser={this.state.profileUser}/>
      </div>
    );
  }
}
