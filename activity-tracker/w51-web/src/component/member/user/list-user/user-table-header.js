import React, { Component } from 'react';

export default class UserTableHeader extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <thead>
        <tr>
          <th>Login</th>
          <th>First Name</th>
          <th>Last Name</th>
          <th>Sessions</th>
        </tr>
      </thead>
    )
  }
}
