import React, { Component } from 'react';

export default class ErrorForm extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <div className="alert alert-danger">
        <strong>Error : </strong> {this.props.error}
      </div>
    )
  }
}
