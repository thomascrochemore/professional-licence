import React, { Component } from 'react';
import ErrorForm from '../../common/error/error-form';

export default class SigninForm extends Component {
  loginInput;
  passwordInput;

  constructor(props){
    super(props);
  }
  onSubmit = (event) => {
    event.preventDefault();
    let login = this.loginInput.value;
    let password = this.passwordInput.value;
    this.props.onSubmit({
      login: login,
      password: password
    });
  };
  render() {
    return (
      <form onSubmit={this.onSubmit} action="#" className="col-md-offset-2 col-md-8">
        {this.props.error ? <ErrorForm error={this.props.error} /> : null }
        <div className="form-group">
          <label htmlFor="signin.form.login">Login</label>
          <div className="input-group">
            <span className="input-group-addon"><span className="glyphicon glyphicon-user"></span></span>
            <input  ref={ (elem) => this.loginInput = elem } id="signin.form.login" type="text" className="form-control" placeholder="Login"/>
          </div>
        </div>
        <div className="form-group">
          <label htmlFor="signin.form.password">Password</label>
          <div className="input-group">
            <span className="input-group-addon"><span className="glyphicon glyphicon-lock"></span></span>
            <input  ref={ (elem) => this.passwordInput = elem } id="signin.form.password" type="password" className="form-control" placeholder="Password"/>
          </div>
        </div>
        <button type="submit" className="btn btn-primary">Submit</button>
      </form>
    )
  }
}
