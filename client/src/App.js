import React, {useState} from 'react'
import {BrowserRouter as Router, Switch, Route} from "react-router-dom"
import 'bootstrap/dist/css/bootstrap.min.css';

import LogInPage from './component/auth/LogIn.js'
import ChatRoom from "./component/ChatRoomComponent";
import { useCookies } from "react-cookie";
import {ProvideAuth} from "./component/auth/auth";

function App() {
  return (
      <ProvideAuth>
          <div className="App">
              <Router>
                  <Switch>
                      <Route path="/" component={() => <LogInPage/>} exact />
                      {/*Todo: Change the chatroom id to proper chatroom */}
                      <Route path="/chatroom" component={() => <ChatRoom roomId={1} uid={1}/>}/>
                  </Switch>
              </Router>
          </div>
      </ProvideAuth>

  );
}

export default App;
