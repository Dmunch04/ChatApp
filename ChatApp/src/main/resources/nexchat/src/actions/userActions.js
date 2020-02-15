import { SET_TOKEN } from "./actionNames";

export const setToken = (token) => {
  return {
    type: SET_TOKEN,
    payload: token
  }
};