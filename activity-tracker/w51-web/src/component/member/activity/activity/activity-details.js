import React, { Component } from 'react';


import { Link } from 'react-router-dom';

import PropertyLine from './property-line';


export default class ActivityDetails extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <div className="col-md-offset-1 col-md-10">
        <div className="panel panel-info">
          <div className="panel-heading">
            <h3 className="panel-title">
              {this.props.activity.name}
              <Link to={`/s/create-session/${this.props.activity.id}`} className="pull-right"><span className="glyphicon glyphicon-plus"></span></Link>
            </h3>
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
                    <th>Type</th>
                  </tr>
                </thead>
                <tbody>
                  {this.props.activity.properties.map((property) => (
                    <PropertyLine key={property.id} property={property} />
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
