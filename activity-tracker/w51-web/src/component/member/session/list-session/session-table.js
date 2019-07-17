import React, { Component } from 'react';
import SessionTableHeader from './session-table-header';
import SessionTableLine from './session-table-line';


export default class SessionTable extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <table className="table table-striped">
        <SessionTableHeader/>
        <tbody>
          {
            this.props.sessions.map((session) =>{
              return (<SessionTableLine key={session.id} session={session}/>)
            })
          }
        </tbody>
      </table>
    )
  }
}
