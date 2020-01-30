"use strict";

import {CHANGED_ROOMS, CHANGED_TOKEN, RECEIVED_USER} from "../events";
import dispatcher from "../dispatcher";

class userStore extends EventTarget {
  constructor() {
    super();
    this.token = "";
    this.rooms = [];
  }

  changeToken(token) {
    this.token = token;
    this.dispatchEvent(new Event(CHANGED_TOKEN))
  }

  getToken() {
    if (this.token === "") {
      throw "NO_TOKEN";
    }
    return this.token;
  }

  receivedUser() {
    this.dispatchEvent(new Event(RECEIVED_USER));
  }

  handleActions (action) {
    switch (action.type) {
      case CHANGED_TOKEN: {
        this.changeToken(action.token);
        break;
      }
      case CHANGED_ROOMS: {
        this.login(action.rooms);
        break;
      }
      case RECEIVED_USER: {
        this.receivedUser();
        break;
      }
    }
  }
}

const user = new userStore;
dispatcher.register(user.handleActions.bind(userStore));

export default user;
