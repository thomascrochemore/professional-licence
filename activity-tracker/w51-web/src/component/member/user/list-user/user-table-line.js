import React, { Component } from 'react';
import { Link } from 'react-router-dom'

export default class UserTableLine extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <tr>
        <td><Link to={`/s/profile/${this.props.user.id}`}>{this.props.user.login}</Link></td>
        <td>{this.props.user.firstname}</td>
        <td>{this.props.user.lastname}</td>
        <td><Link to={`/s/user-sessions/${this.props.user.id}`}>Sessions</Link></td>
      </tr>
    )
  }
}
