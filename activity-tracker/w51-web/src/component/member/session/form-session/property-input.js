import React, { Component } from 'react';
import { Link } from 'react-router-dom'

export default class PropertyInput extends Component {
  state;

  constructor(props){
    super(props);
    this.state = {
      sessionProperty: props.sessionProperty
    };
  }
  componentWillReceiveProps(nextProps){
    this.setState({
      sessionProperty: nextProps.sessionProperty
    });
  }
  val = () =>{
    return this.state.sessionProperty
  };
  onBoolChange  = (e) =>{
    this.setState({
      sessionProperty: Object.assign(this.state.sessionProperty,{
        value_bool: e.target.checked
      })
    });
  }
  onNumberChange  = (e) =>{
    this.setState({
      sessionProperty: Object.assign(this.state.sessionProperty,{
        value_number: e.target.value
      })
    });
  }
  onStringChange  = (e) =>{
    this.setState({
      sessionProperty: Object.assign(this.state.sessionProperty,{
        value_string: e.target.value
      })
    });
  }
  render() {
    if(this.state.sessionProperty.property.valueType == "bool"){
      return (
        <div className="checkbox">
          <label htmlFor={`session.form.session.property.${this.props.id}`}>
            <input id={`session.form.session.property.${this.props.id}`} checked={this.state.sessionProperty.value_bool} onChange={this.onBoolChange} type="checkbox"/>
            {this.state.sessionProperty.property.name}
          </label>
        </div>
      );
    }else if (this.state.sessionProperty.property.valueType == "number"){
      return (
        <div className="form-group">
          <label htmlFor={`session.form.session.property.${this.props.id}`}>{this.state.sessionProperty.property.name}</label>
          <input id={`session.form.session.property.${this.props.id}`} value={this.state.sessionProperty.value_number} onChange={this.onNumberChange} type="number" className="form-control"/>
        </div>
      );
    }else{
      return (
        <div className="form-group">
          <label htmlFor={`session.form.session.property.${this.props.id}`}>{this.state.sessionProperty.property.name}</label>
          <input id={`session.form.session.property.${this.props.id}`} value={this.state.sessionProperty.value_string} onChange={this.onStringChange} type="text" className="form-control"/>
        </div>
      );
    }
  }
}
