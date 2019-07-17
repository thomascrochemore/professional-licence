import React, { Component } from 'react';
import ErrorForm from '../../../common/error/error-form';

import moment from 'moment'

import PropertyInputList from './property-input-list';

export default class SessionForm extends Component {
  dateInput;
  propertiesInput;
  state;

  constructor(props){
    super(props);
    this.state = {
      session: props.session
    }
  }
  componentWillReceiveProps(nextProps){
    this.setState({
      session: nextProps.session
    });
  }
  onDateChange = (e) =>{
    this.setState({
      session: Object.assign(this.state.session,{date: moment(e.target.value).toDate()})
    });
  }
  onSubmit = (event) => {
    event.preventDefault();
    let activityId = this.state.session.activity.id;
    let date = this.state.session.date.getTime();
    let properties = this.propertiesInput.val().map((sessionProperty) =>{
      let prop = Object.assign(sessionProperty,{
        propertyId: sessionProperty.property.id
      });
      prop.property = undefined;
      return prop;
    });
    this.props.onSubmit({
        activityId: activityId,
        date: date,
        properties: properties
    })
  };
  render() {
    return (
      <form onSubmit={this.onSubmit} action="#" className="col-md-offset-2 col-md-8">
        <h2 className="text-right">{this.state.session.activity.name}</h2>
        {this.props.error ? <ErrorForm error={this.props.error} /> : null }
        <div className="form-group">
          <label htmlFor="session.form.date">Date</label>
          <input onChange={this.onDateChange} value={moment(this.state.session.date).format(moment.HTML5_FMT.DATE)} ref={ (elem) => this.dateInput = elem } id="session.form.date" type="date" className="form-control" placeholder="Date" required/>
        </div>
        <PropertyInputList ref={(elem) => {this.propertiesInput = elem}} sessionProperties={this.state.session.sessionProperties}/>
        <button type="submit" className="btn btn-primary">Submit</button>
      </form>
    )
  }
}
