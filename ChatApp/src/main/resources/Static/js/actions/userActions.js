"use strict";

import dispatcher from "../dispatcher";
import { CHANGED_TOKEN, GETTING_SALT, RECEIVED_SALT, CHANGED_ROOMS, GETTING_USER, RECEIVED_USER } from "../events";
import {hash} from "../pages/helpers/hash";
import io from "socket.io-client";

var sock = io("http://localhost:7089");

export function changeToken(token) {
  dispatcher.dispatch({
    type: CHANGED_TOKEN,
    token: token,
  });
}

export function changeRooms(rooms) {
  dispatcher.dispatch({
    type: CHANGED_ROOMS,
    rooms: rooms,
  });
}

export function login(password, username, timeout=30000) {
  dispatcher.dispatch({
    type: GETTING_USER
  });
  new Promise((resolve, reject) => {
    let timer;

    sock.emit("get-salt", username);

    function responseHandler(salt) {
      resolve(salt);
      clearTimeout(timer);
    }

    sock.once("get-salt", responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout waiting for salt"));
      sock.removeListener('get-salt', responseHandler);
    }, timeout);
  }).then( (salt) => {
    return new Promise((resolve, reject) => {
      let timer;

      sock.emit("login", {Username: username, Password: hash(password, {salt: salt})});

      function responseHandler(user_obj) {
        resolve(user_obj);
        clearTimeout(timer);
      }

      sock.once("login", responseHandler);

      timer = setTimeout(() => {
        reject(alert("timeout waiting for token"));
        sock.removeListener('login', responseHandler);
      }, timeout);
    }).then((user_obj) => {
      changeToken(user_obj.Token);
      changeRooms(user_obj.Rooms);
      dispatcher.dispatch({
        type: RECEIVED_USER
      });
    })
  });

}
