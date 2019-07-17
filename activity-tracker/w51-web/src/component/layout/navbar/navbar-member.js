import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';
import BaseMember from '../../member/base-member';
import AvatarCircle from '../../common/widgets/avatar-circle/avatar-circle';
import UserService from '../../../service/user';

export default class NavbarMember extends BaseMember {
  constructor(props){
    super(props);
  }
  logout = (e) =>{
    e.preventDefault();
    UserService.logout().then(() =>{
      this.props.history.push('/')
    });
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
        <div className="collapse navbar-collapse">
          <ul className="nav navbar-nav">
            <li className="active"><a>Member</a></li>
            <li><NavLink to="/s/users" activeClassName="active">Users</NavLink></li>
            <li className="dropdown">
              <a
                href="#"
                className="toogle-dropdown"
                data-toggle="dropdown"
                role="button"
                aria-haspopup="true"
                aria-expanded="false">
                Activity <span className="caret"></span>
              </a>
              <ul className="dropdown-menu">
                  <li><NavLink to="/s/activities" activeClassName="active">Activities</NavLink></li>
                  <li><NavLink to="/s/create-activity" activeClassName="active">Create Activity</NavLink></li>
              </ul>
            </li>
            <li><NavLink to="/s/my-sessions" activeClassName="active">My Sessions</NavLink></li>
          </ul>
          <ul className="nav navbar-nav navbar-right">
            <li className="visible-md visible-lg">
              <AvatarCircle user={this.state.user} custom={['small', 'white']}/>
            </li>
            <li className="dropdown">
              <a
                href="#"
                className="toogle-dropdown"
                data-toggle="dropdown"
                role="button"
                aria-haspopup="true"
                aria-expanded="false">
                <span className="caret"></span>
                {` ${this.state.user.login}`}
              </a>
              <ul className="dropdown-menu">
                  <li><NavLink to="/s/my-profile" activeClassName="active">My profile</NavLink></li>
                  <li><NavLink to="/s/edit-profile" activeClassName="active">Change informations</NavLink></li>
                  <li role="separator" className="divider"></li>
                  <li><a href="#" onClick={this.logout}>Logout</a></li>
                </ul>
            </li>
          </ul>
        </div>
      </nav>

    )
  }
}
