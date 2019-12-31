"use strict";

import io from "socket.io-client";
import { hash } from "./pages/helpers/hash";

let sock = io("http://localhost:7089");

/*
  client.js: a file with alot of bits and pieces for diffe-
  rent tasks requiring access to the backend.
*/

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

  sock.emit("login-get-user-salt", username);
  sock.on("login-send-user-salt", (data) => {
    sock.emit("get-login", {Username: username, Password: hash(password, {salt: data.salt})});
    sock.on("login-send", (data) => {
      return data;
    });
  });
}
