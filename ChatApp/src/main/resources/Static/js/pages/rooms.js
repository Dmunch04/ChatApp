"use strict";

import React from "react";
import {Redirect, withRouter} from "react-router-dom";
import Select from 'react-select';
import { getRoomName, createRoom } from "../client";

export let Rooms = withRouter(({ location }) => {
  return <RoomsComp location={location}/>
});

class CreateRoom extends React.Component {
  constructor(props) {
    super(props);
    this.state = {name: ""}
  }

  handleSubmit(event) {
    createRoom(this.state.name, this.props.uuid).then(
      (room) => {
        console.log(room);
        //this.props.changeRoom()
      }
    );
    event.preventDefault();
  }

  handleChangeName(event) {
    this.setState({name: event.target.value});
  }

  render() {
    return <form onSubmit={this.handleSubmit}>
      <label>
        Chat Room Name:
        <input type="text" value={this.state.value} onChange={this.handleChangeName} />
      </label>
      <br/><br/>
      {this.state.error}
      <br/><br/>
      <input type="submit" value="Create" />
    </form>
  }
}

class RoomsComp extends React.Component {
  constructor(props) {
    super(props);
    this.user = this.props.location.user_obj;
    let rooms_id_list = this.user.Rooms === "." ? [] : this.user.Rooms.split(",");
    this.state = {
      rooms: [],
      redirect: false
    };

    rooms_id_list.forEach((uuid, index) => {
      this.setState(prevState => ({
        rooms: [...prevState.rooms, {
          label: getRoomName(uuid).then((room_name) => {
            return room_name
          }),
          value: uuid,
          room_obj: null
        }]
      }));
    });
  }

  handleChange = selectedOption => {
    console.log(`Option selected:`, selectedOption)
  };

  changeRoom(room_name, uuid, room_obj) {
    this.setState(prevState => ({
      rooms: [...prevState.rooms, {
        label: room_name,
        value: uuid,
        room_obj: room_obj
      }]
    }));
  }

  render() {
    if (this.state.newRoom) {
      return <CreateRoom uuid={this.user.ID} changeRoom={this.changeRoom}/>
    } else {
      return <div>
        <h1>Rooms</h1>
        <button onClick={() => this.state.newRoom = true}>Create new room</button>
        <Select options={this.state.rooms} onChange={this.handleChange}/>
      </div>
    }
  }
}
