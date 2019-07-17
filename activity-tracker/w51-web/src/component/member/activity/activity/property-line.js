import React, { Component } from 'react';
import { Link } from 'react-router-dom'

export default class PropertyLine extends Component {
  types = {
    bool: "True or False",
    number: "Number",
    string: "Text"
  }
  constructor(props){
    super(props);
  }
  render() {
    return (
      <tr>
        <td>{this.props.property.name}</td>
        <td>{this.types[this.props.property.valueType]}</td>
      </tr>
    )
  }
}
