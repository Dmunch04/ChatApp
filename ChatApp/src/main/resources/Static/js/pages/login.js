'use strict';
import React from "react";
import { hash } from "./helpers/hash";
import { login } from "../client";

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
    this.state = {username: "", password: ""};

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
    // TODO: send info to backend
    console.log(`Username: ${this.state.username}\nPassword: ${this.state.password}`);
    let user = login(this.state.username, this.state.password);
    event.preventDefault();
  }

  render() {
    return (
      <form onSubmit={this.handleSubmit}>
        <label>
          Username:
          <input type="text" value={this.state.value} onChange={this.handleChangeUsername} />
          <br/><br/>
          Password:
          <input type="text" value={this.state.value} onChange={this.handleChangePassword} />
        </label>
        <br/><br/><br/>
        <input type="submit" value="Login" />
      </form>
    );
  }
}

