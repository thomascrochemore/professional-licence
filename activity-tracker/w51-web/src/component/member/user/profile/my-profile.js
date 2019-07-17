import React, { Component } from 'react';
import BaseMember from '../../base-member';
import { Link } from 'react-router-dom';

import UserDetails from "./user-details";
import UserService from "../../../../service/user";

export default class MyProfile extends BaseMember {

  constructor(props){
    super(props);
  }
  onUserLoad = (user) => {

  };
  render() {
    return  (
      <div className="row">
        <h1>My Profile <Link to="/s/edit-profile" className="pull-right"><span className="glyphicon glyphicon-edit"></span></Link></h1>
        <UserDetails profileUser={this.state.user}/>
      </div>
    );
  }
}
