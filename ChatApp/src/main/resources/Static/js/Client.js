'use strict';

import React from "react";
import ReactDOM from "react-dom";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";

// let Socket = io ();

class Login extends React.Component {
  render() {
    return <h1>
      Login
    </h1>
  }
}


class App extends React.Component {
  render() {
    return <Router>
      <Switch>
        <Route path="/login">
          <Login/>
        </Route>
      </Switch>
    </Router>
  }
}


const e = React.createElement;

const domContainer = document.querySelector('#root');
ReactDOM.render(e(App), domContainer);
