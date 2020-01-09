'use strict';
import React from "react";
import {register, setToken} from "../client";
import { Redirect } from "react-router-dom";
import { checkIfError } from "./helpers/utils";
import {Rooms} from "./rooms";

export class Register extends React.Component {
  /*
    Register: super page of the register page; contains the
    `RegisterField` component.
  */
  render() {
    return <div>
      <h1>
        Register
      </h1>
      <RegisterField/>
    </div>
  }
}


class RegisterField extends React.Component {
  /*
    RegisterField: a `React.Component` for the register field
    . This class deals with entering a username (`handleChan-
    geUsername`), deals with hashing and adding salt to the
    password (`handleChangePassword` through the `hash` func-
    tion), and deals with submitting the password hash and u-
    sername using (`handleSubmit`) to the java backend using
    sockets. It also checks if the two passwords are the same
    and if the username already exists.
  */
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      password1: "",
      password2: "",
      error: "",
      redirect: false
    };

    this.handleChangeUsername = this.handleChangeUsername.bind(this);
    this.handleChangePassword1 = this.handleChangePassword1.bind(this);
    this.handleChangePassword2 = this.handleChangePassword2.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChangeUsername(event) {
    this.setState({username: event.target.value});
  }

  handleChangePassword1(event) {
    this.setState({password1: event.target.value});
  }

  handleChangePassword2(event) {
    this.setState({password2: event.target.value});
  }

  handleSubmit(event) {
    // TODO: send info to backend and check if username already exists
    if (this.state.password1 === this.state.password2) {
      register(this.state.username, this.state.password1).then(user_obj => {
        if (checkIfError(user_obj)) {
          this.setState({error: user_obj.Error});
        } else {
          setToken(user_obj.Token);
          this.setState({user: user_obj, redirect: true})
        }
      })
    } else {
      this.setState({error: "Passwords do not match!"});
    }
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
            <input type="text" value={this.state.value} onChange={this.handleChangeUsername} />
            <br/><br/>
            Password:
            <input type="password" value={this.state.value} onChange={this.handleChangePassword1} />
            Re-enter Password:
            <input type="password" value={this.state.value} onChange={this.handleChangePassword2} />
          </label>
          <br/><br/>
          {this.state.error}
          <br/><br/>
          <input type="submit" value="Login"/>
        </form>
      );
    }
  }
}

