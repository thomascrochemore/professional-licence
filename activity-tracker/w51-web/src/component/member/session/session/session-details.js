import React, { Component } from 'react';


import { Link } from 'react-router-dom';

import SessionPropertyLine from './session-property-line';
import moment from 'moment'


export default class SessionDetails extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <div className="col-md-offset-1 col-md-10">
        <div className="panel panel-info">
          <div className="panel-heading">
            <h3 className="panel-title">
            <Link to={`/s/activity/${this.props.session.activity.id}`}>{this.props.session.activity.name}</Link> - {moment(this.props.session.date).format(moment.HTML5_FMT.DATE) }
            </h3>
            <h3>Owner : <Link to={`/s/profile/${this.props.session.user.id}`}>{this.props.session.user.login}</Link></h3>
          </div>
          <div className="panel-body">
            <div className="row">
              <table className="table table-user-information">
                <thead>
                  <tr>
                    <th colspan="2">Properties</th>
                  </tr>
                  <tr>
                    <th>Name</th>
                    <th>Value</th>
                  </tr>
                </thead>
                <tbody>
                  {this.props.session.sessionProperties.map((property) => (
                    <SessionPropertyLine key={property.id} sessionProperty={property} />
                  ))}
                </tbody>
              </table>
            </div>
          </div>

        </div>
      </div>
    )
  }
}
