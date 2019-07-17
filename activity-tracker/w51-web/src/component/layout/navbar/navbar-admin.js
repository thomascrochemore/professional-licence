import React, { Component } from 'react';

export default class NavbarAdmin extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <nav className="navbar navbar-inverse navbar-fixed-top">
        <div className="navbar-header">
          <button type="button" className="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span className="icon-bar"></span>
            <span className="icon-bar"></span>
            <span className="icon-bar"></span>
          </button>
          <a className="navbar-brand">ActivityTracker</a>
        </div>
        <div className="container-fluid">
          <ul className="nav navbar-nav">
            <li><a>Admin</a></li>
          </ul>
        </div>
      </nav>

    )
  }
}
