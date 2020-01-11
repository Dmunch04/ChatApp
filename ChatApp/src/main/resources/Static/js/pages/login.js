'use strict';
import React from "react";
import { login, setToken } from "../client";
import { Rooms } from "./rooms";
import {Redirect} from "react-router-dom";

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
    login(this.state.username, this.state.password).then( user_obj => {
        setToken(user_obj.Token);
        this.setState({user: user_obj, redirect: true})
      }
    );
    event.preventDefault();
  }

  render() {
    if (this.state.redirect) {
      return <Redirect to={{pathname: '/rooms', user_obj: this.state.user}} component={Rooms}/>;
    } else {
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
}

