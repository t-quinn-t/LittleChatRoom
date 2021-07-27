import React from 'react'
import {BrowserRouter as Router, Switch, Route} from "react-router-dom"

function App() {
  return (
    <div className="App">
        <Router>
            <Switch>
                <Route path="/dashboard">
                    <div>Hello world</div>
                </Route>
            </Switch>
        </Router>
    </div>
  );
}

export default App;
