import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import RegisterForm from "./register-form";
import UserService from "../../../service/user";

export default class Register extends Component {
  state =  {
    error: null
  };
  constructor(props){
    super(props);
  }
  onRegisterFormSubmit = (register) => {
    UserService.register(register).then((user) => {
      this.props.history.push("/s/welcome");
    })
    .catch((err) => {
      this.setState({
        error: err.message
      });
    })

  }
  render() {
    return (
      <div className="row">
        <h1>Register</h1>
        <RegisterForm error={this.state.error} onSubmit={this.onRegisterFormSubmit} />
        <div style={{padding: '20px'}} className="col-md-offset-2 col-md-8">
          <p>You have already an account ? Please <Link activeClassName="active" to="/signin">signin</Link></p>
        </div>
      </div>
    )
  }
}
