"use strict";

import React from "react";

export class Rooms extends React.Component {
  constructor(props) {
    super(props);
    this.token = this.props.token;
    console.log(this.props); // note: https://stackoverflow.com/questions/52064303/reactjs-pass-props-with-redirect-component
  }
  render() {
    return <h1>Rooms</h1>
  }
}
