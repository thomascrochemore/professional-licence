import React, { Component } from 'react';
import BaseMember from '../../base-member';

import SessionTable from "./session-table";
import SessionService from "../../../../service/session";

export default class ActivitySessions extends BaseMember {

  constructor(props){
    super(props);
    this.state = Object.assign(this.state,{
      sessions: []
    });
  }
  onUserLoad = (user) => {
    SessionService.findByUserAndActivity(user.id,this.props.match.params.activityId).then((sessions) => {
      this.setState({
        sessions: sessions
      });
    });
  };
  render() {
    return (
      <div className="row">
        <h1>My Sessions</h1>
        <SessionTable sessions={this.state.sessions}/>
      </div>
    )
  }
}
