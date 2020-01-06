"use strict";

import React from "react";

export class Rooms extends React.Component {
  constructor(props) {
    super(props);
    this.user = this.props.location.state.user;
    console.log(this.user)
  }
  render() {
    return <h1>Rooms</h1>
  }
}
