import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import moment from 'moment'

export default class SessionTableLine extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <tr>
        <td><Link to={`/s/activity/${this.props.session.activity.id}`}>{this.props.session.activity.name}</Link></td>
        <td>{moment(this.props.session.date).format(moment.HTML5_FMT.DATE) }</td>
        <td><Link to={`/s/profile/${this.props.session.user.id}`}>{this.props.session.user.login}</Link></td>
        <td><Link to={`/s/session/${this.props.session.id}`}><span className="glyphicon glyphicon-eye-open"></span></Link></td>
      </tr>
    )
  }
}
