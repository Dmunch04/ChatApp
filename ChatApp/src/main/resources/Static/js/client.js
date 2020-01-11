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


export function getRoom(uuid, userID) {
  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("get-room", {RoomID: uuid, UserID: userID});

    function responseHandler(room) {
      resolve(room);
      clearTimeout(timer);
    }

    sock.once("get-room", responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout getting room obj"));
      sock.removeListener('get-room', responseHandler);
    }, timeout);
  });
}


export function kickUser(roomID, userID, targetID, timeout = 30000) {
  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("kick-user", {Token: token, RoomID: roomID, UserID: userID, TargetID: targetID});

    function responseHandler(worked) {
      resolve(worked);
      clearTimeout(timer);
    }

    sock.once("kick-user", responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout removing message"));
      sock.removeListener("kick-user", responseHandler);
    }, timeout);
  });
}

export function removeMessage(roomID, userID, messageID, timeout = 30000) {
  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("remove-message", {Token: token, RoomID: roomID, UserID: userID, MessageID: messageID});

    function responseHandler(worked) {
      resolve(worked);
      clearTimeout(timer);
    }

    sock.once("remove-message", responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout removing message"));
      sock.removeListener("remove-message", responseHandler);
    }, timeout);
  });
}


export function sendMessage(roomID, userID, message, timeout = 30000) {
  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("send-message", {Token: token, RoomID: roomID, UserID: userID, Content: message});

    function responseHandler(message_obj) {
      resolve(message_obj);
      clearTimeout(timer);
    }

    sock.once("send-message", responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout sending message"));
      sock.removeListener("send-message", responseHandler);
    }, timeout);
  });
}

export function leaveRoom(roomID, userID, timeout = 30000) {
  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("remove-room", {Token: token, RoomID: roomID, UserID: userID});

    function responseHandler(worked) {
      resolve(worked);
      clearTimeout(timer);
    }

    sock.once("remove-room", responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout leaving room"));
      sock.removeListener("remove-room", responseHandler);
    }, timeout);
  });
}

export function joinRoom(roomID, userID, timeout = 30000) {
  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("join-room", {Token: token, RoomID: roomID, UserID: userID});

    function responseHandler(room_obj) {
      resolve(room_obj);
      clearTimeout(timer);
    }

    sock.once("join-room", responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout joining room"));
      sock.removeListener("join-room", responseHandler);
    }, timeout);
  });
}

export function createRoom(display, creator, timeout = 30000) {
  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("create-room", {Token: token, Display: display, UserID: creator});

    function responseHandler(room_obj) {
      resolve(room_obj);
      clearTimeout(timer);
    }

    sock.once("create-room", responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout creating room"));
      sock.removeListener('create-room', responseHandler);
    }, timeout);
  });
}

export function getUser(uuid) {
  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("get-user", uuid);

    function responseHandler(user) {
      resolve(user);
      clearTimeout(timer);
    }

    sock.once("get-user", responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout getting user"));
      sock.removeListener("get-user", responseHandler);
    }, timeout);
  });
}

export function getMessage(roomID, messageID) {
  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("get-message", {RoomID: roomID, MessageID: messageID});

    function responseHandler(username) {
      resolve(username);
      clearTimeout(timer);
    }

    sock.once("get-message", responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout getting username"));
      sock.removeListener("get-message", responseHandler);
    }, timeout);
  });
}

export function getUsername(uuid) {
  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("get-user-name", uuid);

    function responseHandler(username) {
      resolve(username);
      clearTimeout(timer);
    }

    sock.once("get-user-name", responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout getting username"));
      sock.removeListener("get-user-name", responseHandler);
    }, timeout);
  });
}

export function getRoomName(uuid) {
  return new Promise((resolve, reject) => {
    let timer;

    sock.emit("get-room-name", uuid);

    function responseHandler(room_name) {
      resolve(room_name);
      clearTimeout(timer);
    }

    sock.once("get-room-name", responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout getting room name"));
      sock.removeListener('get-room-name', responseHandler);
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

    sock.once("register", responseHandler);

    timer = setTimeout(() => {
      reject(alert("timeout waiting for token"));
      sock.removeListener('register', responseHandler);
    }, timeout);
  });
}
