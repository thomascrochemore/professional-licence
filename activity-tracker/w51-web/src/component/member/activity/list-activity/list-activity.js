import React, { Component } from 'react';
import BaseMember from '../../base-member';

import ActivityTable from "./activity-table";
import ActivityService from "../../../../service/activity";

export default class ListActivity extends BaseMember {

  constructor(props){
    super(props);
    this.state = Object.assign(this.state,{
      activities: []
    });
  }
  onUserLoad = (user) => {
    ActivityService.findAll().then((activities) => {
      this.setState({
        activities: activities
      });
    });
  };
  render() {
    return (
      <div className="row">
        <h1>Activities</h1>
        <ActivityTable activities={this.state.activities}/>
      </div>
    )
  }
}
