"use strict";

import dispatcher from "../dispatcher";
import { CHANGED_TOKEN, GETTING_SALT, RECEIVED_SALT, CHANGED_ROOMS, GETTING_USER, RECEIVED_USER } from "../events";
import {hash} from "../pages/helpers/hash";
import io from "socket.io-client";

var sock = io("http://localhost:7089");

export function changeToken(token) {
  dispatcher.dispatch({
    type: CHANGED_TOKEN,
    token,
  });
}

export function changeRooms(rooms) {
  dispatcher.dispatch({
    type: CHANGED_ROOMS,
    rooms,
  });
}

export function login(password, username) {
  console.log("lol")
  dispatcher.dispatch({
    type: GETTING_SALT,
  });
  sock.emit("get-salt", username);
  sock.on("get-salt", (salt) => {
    console.log(salt);
    dispatcher.dispatch({
      type: RECEIVED_SALT
    });
    dispatcher.dispatch({
      type: GETTING_USER
    });
    sock.emit("login", {Username: username, Password: hash(password, {salt: salt})});
    sock.on("login", (user_obj) => {
      changeToken(user_obj.Token);
      changeRooms(user_obj.Rooms);
      dispatcher.dispatch({
        type: RECEIVED_USER
      });
    })
  })
}
