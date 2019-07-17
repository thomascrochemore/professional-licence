import React, { Component } from 'react';
import BaseMember from '../../base-member';

import { Link } from 'react-router-dom';

import SessionDetails from "./session-details";
import SessionService from "../../../../service/session";
import NotFound from '../../../common/error/notfound';

export default class Session extends BaseMember {

  constructor(props){
    super(props);
    this.state = Object.assign(this.state,{
      session: {
        activity: {},
        user: {},
        sessionProperties: []
      },
      notFound: false
    });
  }
  onUserLoad = (user) => {
    SessionService.findOne(this.props.match.params.sessionId).then((session) => {
      this.setState({
        session: session,
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
        <h1>Session</h1>
        {this.state.user.id == this.state.session.user.id ? (
          <h2 className="col-md-offset-1 col-md-10 text-right">
            <Link to={`/s/edit-session/${this.state.session.id}`}>
              <span className="glyphicon glyphicon-edit"></span>
            </Link>
          </h2>
        ) : null}
        <SessionDetails session={this.state.session}/>
      </div>
    );
  }
}
