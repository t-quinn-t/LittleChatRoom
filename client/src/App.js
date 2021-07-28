import React from 'react'
import {BrowserRouter as Router, Switch, Route} from "react-router-dom"

import LogInPage from './component/auth/LogIn.js'

function App() {
  return (
    <div className="App">
        <Router>
            <Switch>
                <Route path="/login" component={LogInPage} exact />
                <Route path="/dashboard">
                    <div>Hello world</div>
                </Route>
            </Switch>
        </Router>
    </div>
  );
}

export default App;
