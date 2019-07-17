import React, { Component } from 'react';

export default class SessionTableHeader extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <thead>
        <tr>
          <th>Activity</th>
          <th>Date</th>
          <th>User</th>
          <th>Show session</th>
        </tr>
      </thead>
    )
  }
}
