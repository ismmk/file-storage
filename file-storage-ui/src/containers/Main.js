import React, { Component } from 'react'

class Main extends Component{
  render(){
    return (
      <div>
        <h2>Header</h2>
        <main>{this.props.children}</main>
        <h2>Footer</h2>
      </div>
    )
  }
}
export default Main
