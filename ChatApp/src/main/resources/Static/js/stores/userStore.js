"use strict";

import { CHANGED_TOKEN } from "../events";
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

  login() {

  }

  handleActions (action) {
    switch (action.type) {
      case CHANGED_TOKEN: {
        this.changeToken(action.token);
        break;
      }
      case
    }
  }
}

const user = new userStore;
dispatcher.register(user.handleActions.bind(userStore));

export default user;
