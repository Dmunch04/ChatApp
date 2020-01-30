'use strict';
import React from "react";
import { Rooms } from "./rooms";
import { Redirect } from "react-router-dom";
import * as userActions from "../actions/userActions";
import userStore from "../stores/userStore";
import { RECEIVED_USER } from "../events";

export class Login extends React.Component {
  /*
    Login: super page of the login page; contains the `Login-
    Field` component.
  */
  render() {
    return <div>
      <h1>
        Login
      </h1>
      <LoginField/>
    </div>
  }
}


export class LoginField extends React.Component {
  /*
    LoginField: a `React.Component` for the login field. This
    class deals with entering a username (`handleChangeUsern-
    ame`), deals with hashing and adding salt to the password
    (`handleChangePassword` through the `hash` function), and
    deals with submitting the password hash and username usi-
    ng (`handleSubmit`) to the java backend using sockets.
  */
  constructor(props) {
    super(props);

    this.state = {username: "", password: "", error: "", redirect: false};

    this.handleChangeUsername = this.handleChangeUsername.bind(this);
    this.handleChangePassword = this.handleChangePassword.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChangeUsername(event) {
    this.setState({username: event.target.value});
  }

  handleChangePassword(event) {
    this.setState({password: event.target.value});
  }

  handleSubmit(event) {
    userActions.login(this.state.password, this.state.username);
    event.preventDefault();
  }

  onLogin() {
    console.log("LOADED");
  }

  componentWillMount() {
    userStore.addEventListener(RECEIVED_USER, this.onLogin);
  }

  componentWillUnmount() {
    userStore.removeEventListener(RECEIVED_USER, this.onLogin);
  }

  render() {
    return (
      <form onSubmit={this.handleSubmit}>
        <label>
          Username:
          <input type="text" autoComplete="username" value={this.state.value} onChange={this.handleChangeUsername} />
          <br/><br/>
          Password:
          <input type="password" autoComplete="password" value={this.state.value} onChange={this.handleChangePassword} required />
        </label>
        <br/><br/>
        {this.state.error}
        <br/><br/>
        <input type="submit" value="Login" />
      </form>
    );
  }
}

