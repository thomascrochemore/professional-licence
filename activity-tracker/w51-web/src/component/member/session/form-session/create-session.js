import React, { Component } from 'react';
import SessionForm from "./session-form";
import BaseMember from '../../base-member';
import ActivityService from "../../../../service/activity";
import SessionService from "../../../../service/session";
import NotFound from '../../../common/error/notfound';

export default class CreateSession extends BaseMember {
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
    ActivityService.findOneWithProperties(this.props.match.params.activityId).then((activity) => {
      console.log(activity);
      let sessionProperties = activity.properties.map((property) =>{
        if(property.valueType == "bool"){
          return {
            property: property,
            value_bool: false,
          };
        }else if(property.valueType == "number"){
          return {
            property: property,
            value_number: 0
          };
        }else{
          return {
            property: property,
            value_string: ''
          }
        }
      });
      this.setState({
        session: Object.assign(this.state.session,{
          activity: activity,
          user: user,
          sessionProperties: sessionProperties
        }),
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
  onSessionFormSubmit = (session) => {
   SessionService.createSession(session).then((session) => {
     this.props.history.push(`/s/session/${session.id}`);
   }).catch((err) =>{
     this.setState({
       error: err.message
     });
   });
  }
  render() {
    return this.state.notFound ? (<NotFound/>) : (
      <div className="row">
        <h1>Create Session</h1>
        <SessionForm session={this.state.session} error={this.state.error} onSubmit={this.onSessionFormSubmit} />
      </div>
    )
  }
}
