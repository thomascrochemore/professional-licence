import React, { Component } from 'react';
import { Link } from 'react-router-dom'

export default class SessionPropertyLine extends Component {
  constructor(props){
    super(props);
  }
  render() {
    let value;
    if(this.props.sessionProperty.property.valueType == "bool"){
      if(this.props.sessionProperty.valueBool){
        value = (<span className="glyphicon glyphicon-ok"></span>);
      }
      else
      {
        value = (<span className="glyphicon glyphicon-remove"></span>)
      }
    } else if(this.props.sessionProperty.property.valueType == "number" ){
      value = this.props.sessionProperty.valueNumber;
    }else{
      value = this.props.sessionProperty.valueString;
    }
    return (
      <tr>
        <td>{this.props.sessionProperty.property.name}</td>
        <td>{value}</td>
      </tr>
    )
  }
}
