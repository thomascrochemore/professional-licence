import React, { Component } from 'react';
import { Link } from 'react-router-dom'
import PropertyInput from './property-input';

export default class PropertyInputList extends Component {

  propertyInputs = [];

  constructor(props){
    super(props);
  }
  componentWillReceiveProps(nextProps){
  }
  val = () =>{
    console.log(this.propertyInputs);
    return this.propertyInputs.filter((input) => input !== null).map((input) => input.val());
  };
  render() {
    this.propertyInputs = [];
    return (<div>
      <h4>Properties</h4>
      {this.props.sessionProperties.map((sessionProperty) => {
          return (<PropertyInput key={sessionProperty.property.id}  id={sessionProperty.property.id} sessionProperty={sessionProperty} ref={(elem) =>{ this.propertyInputs.push(elem)}}/>);
        })}
    </div>)
  }
}
