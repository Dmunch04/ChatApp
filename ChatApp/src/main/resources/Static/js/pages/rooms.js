"use strict";

import React from "react";
import {Redirect, withRouter} from "react-router-dom";
import Select from 'react-select';
import { getRoomName, getRoom, createRoom, getUsername, sendMessage} from "../client";

export let Rooms = withRouter(({ location }) => {
  return <RoomsComp location={location}/>
});

class CreateRoom extends React.Component {
  constructor(props) {
    super(props);
    this.state = {name: ""};
  }

  handleSubmit = (event) => {
    createRoom(this.state.name, this.props.uuid).then(
      (room) => {
        console.log(room);
        this.props.addRoom(room.Display, room.ID, room);
      }
    );
    this.props.toggleRedirectCreateRoom();
    event.preventDefault();
  };

  handleChangeName = (event) => {
    this.setState({name: event.target.value});
  };

  render() {
    return <form onSubmit={this.handleSubmit}>
      <label>
        Chat Room Name:
        <input type="text" value={this.state.value} onChange={this.handleChangeName} />
      </label>
      <br/>
      {this.state.error}
      <br/>
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
      rooms: {},
      selectedRoom: "",
      redirectCreateRoom: false
    };

    rooms_id_list.forEach((uuid, _) => {
      this.setState(prevState => ({
        rooms: {...prevState.rooms, [uuid]: {
          label: getRoomName(uuid).then((room_name) => {
            return room_name
          }),
          value: uuid,
          room_obj: null
        }}
      }));
    });
  }

  handleChange = selectedOption => {
    console.log(`Option selected:`, selectedOption);
    this.setState({selectedRoom: selectedOption.value});
    if (this.state.rooms[selectedOption.value].room_obj === null) {
      this.setState(prevState => ({
        rooms: {...prevState.rooms, [selectedOption.value]: {
            ...prevState.rooms[selectedOption.value],
            room_obj: getRoom(selectedOption.value).then((room_obj) => { return room_obj; })
          }}
      }));
    }
  };

  addRoom = (room_name, uuid, room_obj) => {
    this.setState(prevState => ({
      rooms: {...prevState.rooms, [uuid]: {
        label: room_name,
        value: uuid,
        room_obj: room_obj
      }}
    }));
  };

  toggleRedirectCreateRoom = () => {
    this.setState({ redirectCreateRoom: !this.state.redirectCreateRoom})
  };

  handleSendMessage = (event) => {
    sendMessage(this.state.selectedRoom, this.user.ID, event.target.value).then((message_obj) => {
      this.setState(prevState => ({
        rooms: {...prevState.rooms, [this.state.selectedRoom]: {
            ...prevState.rooms[this.state.selectedRoom],
            room_obj: {...prevState.rooms[this.state.selectedRoom].room_obj, Messages: prevState.rooms[this.state.selectedRoom].room_obj.messages.concat(message_obj)}
          }}
      }));
    });
  };

  render = () => {
    const messages = this.state.rooms[this.state.selectedRoom] && this.state.rooms[this.state.selectedRoom].room_obj && this.state.rooms[this.state.selectedRoom].room_obj.Messages;
    let messages_list = [];

    if (typeof messages !== 'undefined') {
      messages.forEach(
        (message_obj, _) => {
          messages_list.push(
            <div>
              <b>{getUsername(message_obj.SenderID).then((username) => {
                return username
              })}</b>:
              <body>{message_obj.Content}</body>
            </div>
          )
        }
      );
    }

    const list_rooms = Object.values(this.state.rooms);

    if (this.state.redirectCreateRoom) {
      return <CreateRoom uuid={this.user.ID} addRoom={this.addRoom} toggleRedirectCreateRoom={this.toggleRedirectCreateRoom}/>
    } else {
      return <div>
        <h1>Rooms</h1>
        <button onClick={() => this.setState({redirectCreateRoom: true})}>Create new room</button>
        <Select options={list_rooms} onChange={this.handleChange}/>
        <div style={{overflowY: "scroll", height:400, border: "1px solid black"}}>
          {messages_list}
        </div>
        <form onSubmit={this.handleSendMessage}>
          <label>
            <input type="text" value={this.state.value} onChange={this.handleChangeName} />
          </label>
          <input type="submit" value="Send" />
        </form>
      </div>
    }
  }
}
