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
    console.log(this.user);
    let rooms_id_list = this.user.Rooms === "." ? [] : this.user.Rooms.split(",");
    this.state = {
      rooms: []
    };

    rooms_id_list.forEach((uuid, index) => {
      this.state.rooms[index] = {
        label: getRoomName(uuid).then( (room_name) => {
          return room_name
        }),
        value: uuid,
        room_obj: null
      }
    });
    console.log(this.props.rooms)
  }

  handleChange = selectedOption => {
    console.log(`Option selected:`, selectedOption)
  };

  render() {
    return <div>
      <h1>Rooms</h1>
      <Select options={this.state.rooms} onChange={this.handleChange}/>
    </div>
  }
}
