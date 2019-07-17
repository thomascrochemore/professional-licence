import React, { Component } from 'react';
import { Link } from 'react-router-dom'

export default class ActivityTableLine extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <tr>
        <td><Link to={`/s/activity/${this.props.activity.id}`}>{this.props.activity.name}</Link></td>
        <td><Link to={`/s/activity-sessions/${this.props.activity.id}`}>My sessions</Link></td>
        <td><Link to={`/s/create-session/${this.props.activity.id}`}><span className="glyphicon glyphicon-plus"></span></Link></td>
      </tr>
    )
  }
}
