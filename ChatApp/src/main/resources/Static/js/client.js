"use strict";

import io from "socket.io-client";
import { hash } from "./pages/helpers/hash";

/*
  client.js: a file with alot of bits and pieces for diffe-
  rent tasks requiring access to the backend.
*/

var sock = io("http://localhost:7089");
var token = "";


export function setToken(tokenSet) {
  /*
  * setToken: set the token used by the client
  */
  token = tokenSet;
}


export function getRoomName(uuid) {
  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("get-room-name", uuid);

    function responseHandler(user_obj) {
      resolve(user_obj);
      clearTimeout(timer);
    }

    sock.once('get-room-name', responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout waiting for room name"));
      sock.removeListener('register', responseHandler);
    }, timeout);
  });
}

export function login(username, password, timeout = 30000) {
  /*
  * login: a function responsible for getting from backend,
  * hashing it with that salt, then sending back to backend
  * to check if equal, then receives the user object with a
  * token. The actual structure is something like this:
  * Client -> (Username) -> Server -> (Passwords's salt) ->
  * Client -> (Password) -> Server -> (User Object / Error
  * Object) -> Client
  */

  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("get-salt", username);

    function responseHandler(salt) {
      resolve(salt);
      clearTimeout(timer);
    }

    sock.once('get-salt', responseHandler);

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

      sock.once('login', responseHandler);

      timer = setTimeout(() => {
        reject(alert("timeout waiting for token"));
        sock.removeListener('login', responseHandler);
      }, timeout);
    })
  });
}

export function register(username, password, timeout = 30000) {
  /*
  * register: a function responsible for sending the backe-
  * nd the username and password (hashed). The backend then
  * generates a token which is stored. The structure is so-
  * mething like this:
  * Client -> (Username + Hashed Password) -> Server -> (U-
  * ser Object / Error Object)
  *
  * Returns a promise
  */

  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("register", {Username: username, Password: hash(password)});

    function responseHandler(user_obj) {
      resolve(user_obj);
      clearTimeout(timer);
    }

    sock.once('register', responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout waiting for token"));
      sock.removeListener('register', responseHandler);
    }, timeout);
  });
}

export function remove_user(uuid, room) {
  return sock.emit("remove-user", {UserID: uuid, RoomID: room, Token: token});
}
