"use strict";

import { SET_TOKEN } from "../actions/actionNames";

const userReducer = (state = {}, action) => {
  switch (action.type) {
    case SET_TOKEN:
      return {...state, token: action.payload};
    default:
      return state;
  }
};

export default userReducer;
