import React, { Component } from 'react';
import BaseMember from '../../base-member';

import CreateActivityForm from "./create-activity-form";
import NotFound from '../../../common/error/notfound';
import ActivityService from "../../../../service/activity";

export default class CreateActivity extends BaseMember {

  constructor(props){
    super(props);
    this.state = Object.assign(this.state,{
      profileUser: {
        firstname: '',
        lastname: '',
      },
      error: null,
      notFound: false
    });
  }
  onUserLoad = (user) => {

  };
  onSubmitCreateActivity = (activity) => {
    ActivityService.createActivity(activity).then((activity) => {
      this.props.history.push(`/s/activity/${activity.id}`);
    })
    .catch((err) => {
      this.setState({
        error: err.message
      });
    })
  };
  render() {
    return this.state.notFound ? (<NotFound/>) :  (
      <div className="row">
        <h1>Create Activity</h1>
        <CreateActivityForm error={this.state.error} onSubmit={this.onSubmitCreateActivity}/>
      </div>
    );
  }
}
