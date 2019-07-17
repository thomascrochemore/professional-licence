import React, { Component } from 'react';
import { Link } from 'react-router-dom';

export default class Home extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <div className="row">
        <h1>Welcome to ActivityTracker</h1>
        <div style={{padding: '20px'}} className="col-md-offset-2 col-md-8">
          <p>Before begining, please <Link activeClassName="active" to="/signin">signin</Link>, or <Link activeClassName="active" to="/register">register your account</Link>.</p>
        </div>
      </div>
    )
  }
}
