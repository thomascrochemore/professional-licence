import React, { Component } from 'react';
import UserService from '../../service/user';

export default class BaseMember extends Component {
  state = {
    user: {
      firstname:'',
      lastname: ''
    }
  }

  constructor(props){
    super(props);
    UserService.identity().then((user) => {
      if(!user){
          return this.props.history.push("/signin?redirect=true");
      }
      this.setState({
        user: user
      });
      this.onUserLoad(user);
    });
  };
  onUserLoad = (user) =>{};
}
