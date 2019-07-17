import React, { Component } from 'react';
import ErrorForm from '../../common/error/error-form';

export default class RegisterForm extends Component {
  loginInput;
  passwordInput;
  confpasswordInput;
  firstnameInput;
  lastnameInput;


  constructor(props){
    super(props);
  }
  onSubmit = (event) => {
    event.preventDefault();
    let login = this.loginInput.value;
    let password = this.passwordInput.value;
    let confpassword = this.confpasswordInput.value;
    let firstname = this.firstnameInput.value;
    let lastname = this.lastnameInput.value;
    this.props.onSubmit({
      login: login,
      password: password,
      confpassword: confpassword,
      firstname: firstname,
      lastname: lastname
    });
  };
  render() {
    return (
      <form onSubmit={this.onSubmit} action="#" className="col-md-offset-2 col-md-8">
        {this.props.error ? <ErrorForm error={this.props.error} /> : null }
        <div className="form-group">
          <label htmlFor="register.form.login">Login</label>
          <div className="input-group">
            <span className="input-group-addon"><span className="glyphicon glyphicon-user"></span></span>
            <input  ref={ (elem) => this.loginInput = elem } id="register.form.login" type="text" className="form-control" placeholder="Login"/>
          </div>
        </div>
        <div className="form-group">
          <label htmlFor="register.form.password">Password</label>
          <div className="input-group">
            <span className="input-group-addon"><span className="glyphicon glyphicon-lock"></span></span>
            <input  ref={ (elem) => this.passwordInput = elem } id="register.form.password" type="password" className="form-control" placeholder="Password"/>
          </div>
        </div>
        <div className="form-group">
          <label htmlFor="register.form.confpassword">Confirm password</label>
          <div className="input-group">
            <span className="input-group-addon"><span className="glyphicon glyphicon-lock"></span></span>
            <input  ref={ (elem) => this.confpasswordInput = elem } id="register.form.confpassword" type="password" className="form-control" placeholder="Confirm password"/>
          </div>
        </div>
        <div className="form-group">
          <label htmlFor="register.form.firstname">First name</label>
          <input  ref={ (elem) => this.firstnameInput = elem } id="register.form.firstname" type="text" className="form-control" placeholder="First name"/>
        </div>
        <div className="form-group">
          <label htmlFor="register.form.lastname">Last name</label>
          <input  ref={ (elem) => this.lastnameInput = elem } id="register.form.lastname" type="text" className="form-control" placeholder="Last name"/>
        </div>
        <button type="submit" className="btn btn-primary">Submit</button>
      </form>
    )
  }
}
