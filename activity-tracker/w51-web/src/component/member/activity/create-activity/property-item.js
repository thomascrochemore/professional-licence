import React, { Component } from 'react';
import { Link } from 'react-router-dom'

export default class PropertyItem extends Component {
  nameInput;
  typeInput;

  closeStyle = {
    color: "#000"
  };

  constructor(props){
    super(props);
  }

  onClose = (e) =>{
    e.preventDefault();
    this.props.onClose(this.props.id);
  }

  val = () =>{
    return {
      name: this.nameInput.value,
      value_type: this.typeInput.value
    };
  };
  render() {
    return (
      <li class="list-group-item">
        <h4 className="text-right"><a style={this.closeStyle} href="#" onClick={this.onClose}><span className="glyphicon glyphicon-remove"></span></a></h4>
        <div className="form-group">
          <label htmlFor={`activity.form.property.name.${this.props.id}`}>Name</label>
            <input  ref={ (elem) => this.nameInput = elem } id={`activity.form.property.name.${this.props.id}`} type="text" className="form-control" placeholder="Name"/>
          <label htmlFor={`activity.form.property.value_type.${this.props.id}`}>Type</label>
            <select  ref={ (elem) => this.typeInput = elem } id={`activity.form.property.value_type.${this.props.id}`} type="text" className="form-control">
              <option value="bool">True Or False</option>
              <option value="string">Text</option>
              <option value="number">Number</option>
            </select>
        </div>
      </li>
    )
  }
}
