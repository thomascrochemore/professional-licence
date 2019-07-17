import React, { Component } from 'react';

import AvatarCircle from '../../../common/widgets/avatar-circle/avatar-circle';

import { Link } from 'react-router-dom'


export default class UserDetails extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <div className="col-md-offset-1 col-md-10">
        <div className="panel panel-info">
          <div className="panel-heading">
            <h3 className="panel-title">{`${this.props.profileUser.firstname} ${this.props.profileUser.lastname}`}</h3>
          </div>
          <div className="panel-body">
            <div className="row">
              <div className="visible-md visible-lg col-md-3" align="center">
                <AvatarCircle user={this.props.profileUser}/>
              </div>

              <div className=" col-md-9">
                <table className="table table-user-information">
                  <tbody>
                  <tr>
                    <td>Login:</td>
                    <td>{this.props.profileUser.login}</td>
                  </tr>
                    <tr>
                      <td>First name:</td>
                      <td>{this.props.profileUser.firstname}</td>
                    </tr>
                    <tr>
                      <td>Last name:</td>
                      <td>{this.props.profileUser.lastname}</td>
                    </tr>
                    <tr>
                      <td>Sessions:</td>
                      <td><Link to={`/s/user-sessions/${this.props.profileUser.id}`}>Sessions</Link></td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

        </div>
      </div>
    )
  }
}
