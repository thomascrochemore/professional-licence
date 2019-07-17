import React, { Component } from 'react';
import ActivityTableHeader from './activity-table-header';
import ActivityTableLine from './activity-table-line';


export default class ActivityTable extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <table className="table table-striped">
        <ActivityTableHeader/>
        <tbody>
          {
            this.props.activities.map((activity) =>{
              return (<ActivityTableLine key={activity.id} activity={activity}/>)
            })
          }
        </tbody>
      </table>
    )
  }
}
