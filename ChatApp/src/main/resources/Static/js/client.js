'use strict';

// Imports
import React from "react";
import { render } from "react-dom";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import "../css/Style.css"
import { LoginField } from "./login"
import io from 'socket.io-client';

window.sock = io('http://localhost:7089');

class Login extends React.Component {
  /*
    Home: home page of the website; should have a why to use
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


class Home extends React.Component {
  /*
    Home: home page of the website; should have a why to use
    this app, as well as a login link and a "register now" b-
    utton.
  */
  render() {
    return <h1>
      Welcome to chat app!
    </h1>
  }
}


class App extends React.Component {
  /*
    App: full app responsible for routing and rendering the
    website. It also acts as the home page when needed.
  */
  render() {
    return <Router>
      <nav>
        <ul>
          <li>
            <Link to="/home">Home</Link>
          </li>
          <li>
            <Link to="/login">Login</Link>
          </li>
        </ul>
      </nav>
      <Switch>
        <Route exact path="/login">
          <Login/>
        </Route>
        <Route exact path="/home">
          <Home/>
        </Route>
        <Route exact path="/">
          <Home/>
        </Route>
      </Switch>
    </Router>
  }
}

/*---------------------------------------------------------------*/

/*
  Render the site on the clients "root" div
*/

const e = React.createElement;

const domContainer = document.querySelector('#root');
render(e(App), domContainer);
