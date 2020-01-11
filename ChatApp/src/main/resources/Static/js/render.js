'use strict';

// Imports
import React from "react";
import { render } from "react-dom";

import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
} from "react-router-dom";
import "../css/Style.css";
import { Login } from "./pages/login";
import { Register } from "./pages/register";
import { Home } from "./pages/home";
import { Rooms } from "./pages/rooms"


class App extends React.Component {
  /*
  * App: full app responsible for routing and rendering the
  * website. It also acts as the home page when needed.
  */
  render() {
    return <Router>
      <nav>
        <ul>
          <li>
            <Link to="/home/">Home</Link>
          </li>
          <li>
            <Link to="/login/">Login</Link>
          </li>
          <li>
            <Link to="/register/">Register</Link>
          </li>
        </ul>
      </nav>
      <Switch>
        <Route exact path="/login/">
          <Login/>
        </Route>
        <Route exact path="/register/">
          <Register/>
        </Route>
        <Route exact path="/home/">
          <Home/>
        </Route>
        <Route exact path="/rooms">
          <Rooms/>
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
*  Render the site on the clients "root" div
*/

const e = React.createElement;

const domContainer = document.getElementById('root');
render(e(App), domContainer);
