import 'url-search-params-polyfill';

import React, { Component } from 'react';
import { Link } from 'react-router-dom';

import SigninForm from './signin-form';
import UserService from '../../../service/user';

export default class Signin extends Component {
  state =  {
    error: null
  };
  constructor(props){
    super(props);
  }
  onSigninFormSubmit = (credentials) => {
    UserService.signin(credentials)
    .then((user) => {
      let search = new URLSearchParams(this.props.location.search);
      if(search.has('redirect') && search.get('redirect') === 'true'){
        this.props.history.goBack();
      }else{
        this.props.history.push("/s/welcome");
      }
    })
    .catch((err) => {
      this.setState({
        error: err.message
      });
    });
  }
  render() {
    return (
      <div className="row">
        <h1>Signin</h1>
        <SigninForm error={this.state.error} onSubmit={this.onSigninFormSubmit} />
        <div style={{padding: '20px'}} className="col-md-offset-2 col-md-8">
          <p>You are not a member ? please <Link activeClassName="active" to="/register">register your account</Link></p>
        </div>
      </div>
    )
  }
}
