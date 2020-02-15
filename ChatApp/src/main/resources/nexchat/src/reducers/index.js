"use strict";

import userReducer from "./user";
import {combineReducers} from "redux";

const reducers = combineReducers({
  user: userReducer
});

export default reducers;
