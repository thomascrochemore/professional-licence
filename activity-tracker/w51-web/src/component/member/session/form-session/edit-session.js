import React, { Component } from 'react';
import SessionForm from "./session-form";
import BaseMember from '../../base-member';
import ActivityService from "../../../../service/activity";
import SessionService from "../../../../service/session";
import NotFound from '../../../common/error/notfound';

export default class EditSession extends BaseMember {
  constructor(props){
    super(props);
    this.state = Object.assign(this.state,{
      error: null,
      session: {
        date: new Date(),
        activity: {},
        user: {},
        sessionProperties: []
      }
    });
  }
  onUserLoad = (user) => {
    SessionService.findOne(this.props.match.params.sessionId).then((session) =>{
      console.log(session);
      if(session.user.id != user.id){
        this.setState({
          notFound: true
        });
        return;
      }
      this.setState({
        session: session
      });
    }).catch((err) => {
      if(err.status == 404){
        this.setState({
          notFound: true
        });
      }
    });
  }
  onSessionFormSubmit = (session) => {
    let mySession = Object.assign(session,{
      id: this.props.match.params.sessionId
    });
   SessionService.updateSession(mySession).then((sess) => {
     this.props.history.push(`/s/session/${sess.id}`);
   }).catch((err) =>{
    this.props.history.push(`/s/session/${mySession.id}`);
   })
  }
  render() {
    return this.state.notFound ? (<NotFound/>) : (
      <div className="row">
        <h1>Edit Session</h1>
        <SessionForm session={this.state.session} error={this.state.error} onSubmit={this.onSessionFormSubmit} />
      </div>
    )
  }
}
