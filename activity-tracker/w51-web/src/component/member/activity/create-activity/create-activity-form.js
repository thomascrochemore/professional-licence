import React, { Component } from 'react';
import ErrorForm from '../../../common/error/error-form';
import PropertyList from './property-list';

export default class CreateActivityForm extends Component {
  nameInput;
  propertiesInput;


  constructor(props){
    super(props);
  }
  onSubmit = (event) => {
    event.preventDefault();
    let name = this.nameInput.value;
    let properties = this.propertiesInput.val();
    this.props.onSubmit({
      name: name,
      properties: properties
    });
  };
  render() {
    return (
      <form onSubmit={this.onSubmit} action="#" className="col-md-offset-2 col-md-8">
        {this.props.error ? <ErrorForm error={this.props.error} /> : null }
        <div className="form-group">
          <label htmlFor="activity.form.name">Name</label>
          <div className="input-group">
            <span className="input-group-addon"><span className="glyphicon glyphicon-user"></span></span>
            <input  ref={ (elem) => this.nameInput = elem } id="activity.form.name" type="text" className="form-control" placeholder="Name"/>
          </div>
        </div>
        <PropertyList ref={ (elem) => this.propertiesInput = elem}/>
        <button type="submit" className="btn btn-primary">Submit</button>
      </form>
    );
  }
}
