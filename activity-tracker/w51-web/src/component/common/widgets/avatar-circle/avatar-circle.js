import React, { Component } from 'react';

import StringUtils from '../../../../service/string';


export default class AvatarCircle extends Component {
  classes;
  constructor(props){
    super(props);
    let custom = props.custom || [];
    custom = custom.map((suffix) => `avatar-circle-${suffix}`);
    this.classes = [
      "avatar-circle",
      ...custom
    ]
    if(props.small){
      this.classes.push("avatar-circle-small");
    }
  }
  render() {
    return (
      <div className={this.classes.join(' ')}>
        <span className="initials">{StringUtils.getInitials(this.props.user.firstname,this.props.user.lastname)}</span>
        </div>
      )
  }
}
//<img alt="User Pic" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTEKkWvXx8mhf7oQi5ZnhUepbrndtyzjdSfn_gKQPv2CsBTqSVb" className="img-circle img-responsive" />
