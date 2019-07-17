import React, { Component } from 'react';
import BaseMember from '../../base-member';

import ActivityDetails from "./activity-details";
import ActivityService from "../../../../service/activity";
import NotFound from '../../../common/error/notfound';

export default class Activity extends BaseMember {

  constructor(props){
    super(props);
    this.state = Object.assign(this.state,{
      activity: {
        name: '',
        properties: []
      },
      notFound: false
    });
  }
  onUserLoad = (user) => {
    ActivityService.findOneWithProperties(this.props.match.params.activityId).then((activity) => {
      this.setState({
        activity: activity,
        notFound: false
      });
    }).catch((err) => {
      if(err.status == 404){
        this.setState({
          notFound: true
        });
      }
    });
  };
  render() {
    return this.state.notFound ? (<NotFound/>) :  (
      <div className="row">
        <h1>Activity</h1>
        <ActivityDetails activity={this.state.activity}/>
      </div>
    );
  }
}
