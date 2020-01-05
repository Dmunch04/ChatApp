"use strict";

import React from "react";

export class Rooms extends React.Component {
  constructor(props) {
    super(props);
    this.token = this.props.token;
  }
  render() {
    return <h1>Rooms</h1>
  }
}
