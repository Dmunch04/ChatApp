"use strict";

import io from "socket.io-client";
import { hash } from "./pages/helpers/hash";

/*
  client.js: a file with alot of bits and pieces for diffe-
  rent tasks requiring access to the backend.
*/

let sock = io("http://localhost:7089");
let token = "";


export function login(username, password) {
  /*
    login: a function responsible for getting from backend,
    hashing it with that salt, then sending back to backend
    to check if equal, then receives the user object with a
    token. The actual structure is something like this:
    Client -> (Username) -> Server -> (Passwords's salt) ->
    Client -> (Password) -> Server -> (User Object / Error
    Object) -> Client
  */

  sock.emit("get-salt", username);
  sock.on("get-salt", (data) => {
    sock.emit("login", {Username: username, Password: hash(password, {salt: data.salt})});
    sock.on("login", (data) => {
      token = data.hasOwnProperty("token") ? data.token : token;
      return data;
    });
  });
}

export function register(username, password) {
  /*
    register: a function responsible for sending the backe-
    nd the username and password (hashed). The backend then
    generates a token which is stored. The structure is so-
    mething like this:
    Client -> (Username + Hashed Password) -> Server -> (U-
    ser Object / Error Object)
  */

  sock.emit("register", {Username: username, Password: hash(password)});
  sock.on("register", (data) => {
    token = data.hasOwnProperty("token") ? data.token : token;
    return data;
  });
}

