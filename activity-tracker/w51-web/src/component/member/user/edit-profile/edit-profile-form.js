import React, { Component } from 'react';
import ErrorForm from '../../../common/error/error-form';

export default class EditProfileForm extends Component {
  passwordInput;
  confpasswordInput;
  firstnameInput;
  lastnameInput;

  state;

  constructor(props){
    super(props);
    this.state = {
      confDisabled: true,
      user: props.user
    }
  }
  componentWillReceiveProps(nextProps){
    this.setState({
      confDisabled: true,
      user: nextProps.user
    });
  }
  onFirstnameChange = (e) =>{
    this.setState({
      user: Object.assign(this.state.user,{firstname: e.target.value})
    });
  }
  onLastnameChange = (e) => {
    this.setState({
      user: Object.assign(this.state.user,{lastname: e.target.value})
    })
  }
  onPasswordChange = () => {
    if(this.passwordInput.value){
      this.setState({
        confDisabled: false
      })
    }else{
      this.setState({
        confDisabled: true
      })
    }
  }
  onSubmit = (event) => {
    event.preventDefault();
    let password = this.passwordInput.value;
    let confpassword = this.confpasswordInput.value;
    let firstname = this.firstnameInput.value;
    let lastname = this.lastnameInput.value;
    this.props.onSubmit({
      password: password ? password : undefined,
      confpassword: password ? confpassword : undefined,
      firstname: firstname,
      lastname: lastname
    });
  };
  render() {
    return (
      <form onSubmit={this.onSubmit} action="#" className="col-md-offset-2 col-md-8">
        <h2 className="text-right">{this.props.user.login}</h2>
        {this.props.error ? <ErrorForm error={this.props.error} /> : null }
        <div className="form-group">
          <label htmlFor="profile.form.firstname">First name</label>
          <input onChange={this.onFirstnameChange} value={this.state.user.firstname} ref={ (elem) => this.firstnameInput = elem } id="profile.form.firstname" type="text" className="form-control" placeholder="First name"/>
        </div>
        <div className="form-group">
          <label htmlFor="profile.form.lastname">Last name</label>
          <input onChange={this.onLastnameChange} value={this.state.user.lastname}  ref={ (elem) => this.lastnameInput = elem } id="profile.form.lastname" type="text" className="form-control" placeholder="Last name"/>
        </div>
        <div className="form-group">
          <label htmlFor="profile.form.password">Password</label>
          <div className="input-group">
            <span className="input-group-addon"><span className="glyphicon glyphicon-lock"></span></span>
            <input onChange={this.onPasswordChange} ref={ (elem) => this.passwordInput = elem } id="profile.form.password" type="password" className="form-control" placeholder="Password"/>
          </div>
        </div>
        <div className="form-group">
          <label htmlFor="profile.form.confpassword">Confirm password</label>
          <div className="input-group">
            <span className="input-group-addon"><span className="glyphicon glyphicon-lock"></span></span>
            <input disabled={this.state.confDisabled}  ref={ (elem) => this.confpasswordInput = elem } id="profile.form.confpassword" type="password" className="form-control" placeholder="Confirm password"/>
          </div>
        </div>
        <button type="submit" className="btn btn-primary">Submit</button>
      </form>
    )
  }
}
