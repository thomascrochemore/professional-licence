import React, { Component } from 'react';
import EditProfileForm from "./edit-profile-form";
import UserService from "../../../../service/user";
import BaseMember from '../../base-member';

export default class EditProfile extends BaseMember {
  constructor(props){
    super(props);
    this.state = Object.assign(this.state,{
      error: null
    });
  }
  onUserLoad = (user) => {
  };
  onEditFormSubmit = (user) => {
   console.log(user);
   UserService.updateMyProfile(user).then((user) => {
      this.props.history.push("/s/my-profile");
    })
    .catch((err) => {
      this.setState({
        error: err.message
      });
    });
  }
  render() {
    return (
      <div className="row">
        <h1>Edit Profile</h1>
        <EditProfileForm user={this.state.user} error={this.state.error} onSubmit={this.onEditFormSubmit} />
      </div>
    )
  }
}
