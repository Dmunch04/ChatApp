"use strict";

import React from 'react';
import { Home } from "./components/home";

import {
  HashRouter as Router,
  Switch,
  Route,
  Link,
} from "react-router-dom";

function App() {
  return <Router>
    <nav>
      <ul>
        <li>
          <Link to="/home/">Home</Link>
        </li>
      </ul>
    </nav>
    <Switch>
      <Route exact path="/home/">
        <Home/>
      </Route>
      <Route exact path="/">
        <Home/>
      </Route>
    </Switch>
  </Router>
}

export default App;
