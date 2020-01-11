"use strict";

import React from "react";
import {Redirect, withRouter} from "react-router-dom";
import Select from 'react-select';
import { getRoomName, getRoom, createRoom, getUsername, sendMessage} from "../client";
import "core-js/stable";
import "regenerator-runtime/runtime";

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
  _isMounted = false;

  constructor(props) {
    super(props);
    this.user = this.props.location.user_obj;
    this.rooms_id_list = this.user.Rooms === "." ? [] : this.user.Rooms.split(",");
    this.state = {
      rooms: {},
      selectedRoom: "",
      redirectCreateRoom: false,
      message: "",
      doneRendering: false,
      messagesList: []
    };
  }

  handleChange = selectedOption => {
    this.setState({selectedRoom: selectedOption.value});
    if (this.state.rooms[selectedOption.value].room_obj === null) {
      this.setState(prevState => ({
        rooms: {...prevState.rooms, [selectedOption.value]: {
            ...prevState.rooms[selectedOption.value],
            room_obj: getRoom(selectedOption.value).then((room_obj) => { return room_obj; })
          }}
      }));
    }
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

  handleSendMessage = (event) => {
    sendMessage(this.state.selectedRoom, this.user.ID, this.state.message).then((message_obj) => {
      this.setState(prevState => ({
        rooms: {...prevState.rooms, [this.state.selectedRoom]: {
            ...prevState.rooms[this.state.selectedRoom],
            room_obj: {...prevState.rooms[this.state.selectedRoom].room_obj, Messages: prevState.rooms[this.state.selectedRoom].room_obj.Messages.concat(message_obj)}
          }}
      }));
    });
    this.renderRoomMessages();
    event.preventDefault();
  };

  handleChangeName = (event) => {
    this.setState({message: event.target.value})
  };

  renderRoomMessages = () => {
    const messages = this.state.rooms[this.state.selectedRoom] && this.state.rooms[this.state.selectedRoom].room_obj && this.state.rooms[this.state.selectedRoom].room_obj.Messages;
    console.log(messages);
    if (typeof messages !== "undefined") {
      const messages_list = messages.map((message_obj, index) => {
        return getUsername(message_obj.SenderID).then((username) => <div key={index}>{username}:{message_obj.Message}</div>)
      });
      this.setState({messagesList: messages_list, doneRendering: true}, () => {
        console.log("state: ", this.state);
      });
    }
  };

  updateRooms() {
    this.rooms_id_list.forEach((uuid, _) => {
      getRoomName(uuid).then((values) => {this.setState((previousState) => {
        return {rooms: {...previousState.rooms, [values.uuid]: {
            label: values.roomName,
            value: values.uuid,
            room_obj: null
          }}}
      })});
    });
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
      return <CreateRoom uuid={this.user.ID} addRoom={this.addRoom} toggleRedirectCreateRoom={this.toggleRedirectCreateRoom}/>
    } else {
      return <div>
        <h1>Rooms</h1>
        <button onClick={() => this.setState({redirectCreateRoom: true})}>Create new room</button>
        <Select options={Object.values(this.state.rooms)} onChange={this.handleChange}/>
        <div style={{overflowY: "scroll", height:400, border: "1px solid black"}}>
          {this.state.doneRendering? this.state.messagesList : ""}
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
