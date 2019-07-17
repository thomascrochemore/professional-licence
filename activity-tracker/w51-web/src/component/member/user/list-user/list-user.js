import React, { Component } from 'react';
import BaseMember from '../../base-member';

import UserTable from "./user-table";
import UserService from "../../../../service/user";

export default class ListUser extends BaseMember {

  constructor(props){
    super(props);
    this.state = Object.assign(this.state,{
      users: []
    });
  }
  onUserLoad = (user) => {
    UserService.findAll().then((users) => {
      this.setState({
        users: users
      });
    });
  };
  render() {
    return (
      <div className="row">
        <h1>User List</h1>
        <UserTable users={this.state.users}/>
      </div>
    )
  }
}
