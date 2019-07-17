import React, { Component } from 'react';

export default class UserTableHeader extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <thead>
        <tr>
          <th>Activity</th>
          <th>Show my sessions</th>
          <th>Add session</th>
        </tr>
      </thead>
    )
  }
}
