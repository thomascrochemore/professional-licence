import React, { Component } from 'react';
import PropertyItem from './property-item';


export default class PropertyList extends Component {

  nextId = 0;
  itemRefs = [];

  state = {
    propertyItems: []
  }
  constructor(props){
    super(props);
  }
  addItem = (e) => {
    e.preventDefault();
    this.setState({
      propertyItems: [
        ... this.state.propertyItems,
        (<PropertyItem ref={(elem) =>{ this.itemRefs.push(elem)}} key={this.nextId} id={this.nextId} onClose={this.onRemoveItem}/>)
      ]
    });
    this.nextId++;
  }
  onRemoveItem = (id) => {
    let itemRefs = this.itemRefs.filter((item) => {
      return item && item.props.id !== id
    });
    this.itemRefs = itemRefs;
    let newItems = this.state.propertyItems.filter((item) => {
      return item.props.id !== id
    });
    this.setState({
      propertyItems: newItems
    });
  };
  val = () => {
    return this.itemRefs.filter((item) => item !== null).map((item) => item.val());
  };
  render() {
    return (
      <div>
        <h4>Properties <a className="pull-right" href="#" onClick={this.addItem}><span className="glyphicon glyphicon-plus"></span></a></h4>
        <ul class="list-group">
        {this.state.propertyItems}
        </ul>
      </div>
    )
  }
}
