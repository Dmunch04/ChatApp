"use strict";

import React from "react";
import { withRouter } from "react-router-dom";
import Select from 'react-select';
import { getRoomName } from "../client";

export let Rooms = withRouter(({ location }) => {
  return <RoomsComp location={location}/>
});

class RoomsComp extends React.Component {
  constructor(props) {
    super(props);
    this.user = this.props.location.user_obj;
    this.props = {
      rooms: this.user === "." ? [] : this.user.split(",")
    }
  }

  getRoomNameMet(uuid) {
    return getRoomName(uuid).then( (room_name) => {
      return room_name
    } )
  }

  render() {
    return <div>
      <h1>Rooms</h1>
      <Select/>
    </div>
  }
}
