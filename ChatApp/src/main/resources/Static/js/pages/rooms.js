"use strict";

import React from "react";
import {Redirect, withRouter} from "react-router-dom";
import Select from 'react-select';
import { getRoomName, getRoom, createRoom, getUsername, sendMessage} from "../client";
import "core-js/stable";
import "regenerator-runtime/runtime";

var user = {};

export let Rooms = withRouter(({ location }) => {
  return <RoomsComp location={location}/>
});

function getSafe(fn, defaultVal) {
  try {
    return fn();
  } catch (e) {
    return defaultVal;
  }
}

class CreateRoom extends React.Component {
  constructor(props) {
    super(props);
    this.state = {name: ""};
  }

  handleSubmit = (event) => {
    createRoom(this.state.name).then(
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
  _isMounted = false;

  constructor(props) {
    super(props);
    if (this.props.location.user_obj) {
      user = this.props.location.user_obj;
    }
    this.rooms_id_list = user.Rooms === "." ? [] : user.Rooms.split(",");
    this.state = {
      rooms: {},
      selectedRoom: "",
      redirectCreateRoom: false,
      message: "",
      doneRendering: false,
      messagesList: []
    };
  }

  handleChange = async(selectedOption) => {
    if (this.state.rooms[selectedOption.value].room_obj === null) {
      const room_obj = await getRoom(selectedOption.value);
      let rooms = {...this.state.rooms, [selectedOption.value]: {
          ...this.state.rooms[selectedOption.value], room_obj: room_obj
        }};
      this.setState({rooms: rooms});
    }
    this.setState({selectedRoom: selectedOption.value});
    this.renderRoomMessages();
  };

  addRoom = (room_name, uuid, room_obj) => {
    this.setState(prevState => ({
      rooms: {...prevState.rooms, [uuid]: {
        label: room_name,
        value: uuid,
        room_obj: room_obj
      }}
    }));
    this.rooms_id_list.push(uuid);
    this.updateRooms();
  };

  toggleRedirectCreateRoom = () => {
    this.setState({ redirectCreateRoom: !this.state.redirectCreateRoom})
  };

  handleSendMessage = async(event) => {
    let message_obj = await sendMessage(this.state.selectedRoom, this.state.message)
    this.setState(prevState => ({
     rooms: {...prevState.rooms, [this.state.selectedRoom]: {
         ...prevState.rooms[this.state.selectedRoom],
         room_obj: {...prevState.rooms[this.state.selectedRoom].room_obj, Messages: prevState.rooms[this.state.selectedRoom].room_obj.Messages.concat(message_obj)}
       }}
    }));
    this.renderRoomMessages();
    event.preventDefault();
  };

  handleChangeName = (event) => {
    this.setState({message: event.target.value})
  };

  renderRoomMessages = async() => {
    const messages = getSafe(() => this.state.rooms[this.state.selectedRoom].room_obj.Messages, []);
    if (messages.length > 0 && Object.entries(messages[0]).length !== 0) {
      let messages_list = [];
      console.log("messages:", messages);
      for (const [i, message] of messages.entries()) {
        const username = await getUsername(message.SenderID);
        messages_list.push(<div key={i}>{username}:{message.Message}</div>);
      }
      this.setState({messagesList: messages_list, doneRendering: true}, () => {
        console.log("state: ", this.state);
      });
    }
  };

  async updateRooms() {
    let roomsState = {...this.state.rooms};
    for (const uuid of this.rooms_id_list) {
      const roomName = await getRoomName(uuid);
      roomsState[uuid] = {label: roomName, value: uuid, room_obj: null};
    }
    this.setState({rooms: roomsState});
  }

  componentDidMount() {
    this._isMounted = true;
    this.updateRooms();
  }

  componentWillUnmount() {
    this._isMounted = false;
  }

  render = () => {
    if (this.state.doneRendering){
      console.log(this.state.messagesList);
    }
    if (this.state.redirectCreateRoom) {
      return <CreateRoom addRoom={this.addRoom} toggleRedirectCreateRoom={this.toggleRedirectCreateRoom}/>
    } else {
      return <div>
        <h1>Rooms</h1>
        <button onClick={() => this.setState({redirectCreateRoom: true})}>Create new room</button>
        <Select options={Object.values(this.state.rooms)} onChange={this.handleChange}/>
        <div style={{overflowY: "scroll", height:400, border: "1px solid black"}}>
          {this.state.doneRendering? this.state.messagesList : "Loading"}
        </div>
        <form onSubmit={this.handleSendMessage}>
          <label>
            <input type="text" onChange={this.handleChangeName} />
          </label>
          <input type="submit" value="Send" />
        </form>
      </div>
    }
  }
}
