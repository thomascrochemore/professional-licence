import React, { Component } from 'react';
import UserTableHeader from './user-table-header';
import UserTableLine from './user-table-line';


export default class UserTable extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <table className="table table-striped">
        <UserTableHeader/>
        <tbody>
          {
            this.props.users.map((user) =>{
              return (<UserTableLine key={user.id} user={user}/>)
            })
          }
        </tbody>
      </table>
    )
  }
}
