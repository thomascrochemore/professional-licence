import React, { Component } from 'react';
import { Link } from 'react-router-dom'
import BaseMember from '../base-member';

export default class Welcome extends BaseMember {
  constructor(props){
    super(props);

  }
  onUserLoad = (user) => {
    console.log(user);
  }
  render() {
    return (
      <div className="row">
        <h1>Welcome {this.state.user.login}</h1>
        <div style={{padding: '20px'}} className="col-md-offset-2 col-md-8">
          <p>All users can <Link to="/s/create-activity">create his own sport activity</Link>, you can see <Link to="/s/activities">all activities</Link> and for each other, create yours sessions.</p>
          <p>You can see <Link to="/s/my-sessions">yours sessions</Link> and edit them. You can also see <Link to="/s/users">users</Link> and them sessions.</p>
          <p>Enjoy it !</p>
        </div>
      </div>
    )
  }
}
