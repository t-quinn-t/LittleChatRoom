import React, {useState} from 'react'
import {BrowserRouter as Router, Switch, Route} from "react-router-dom"
import 'bootstrap/dist/css/bootstrap.min.css';

import LogInPage from './component/auth/LogIn.js'
import ChatRoom from "./component/ChatRoomComponent";

function App() {
  let [isLoggedIn, setLoginStatus] = useState(false);

  let [currUser, setCurrentUser] = useState({
      uname: null,
      uid: -1,
      email: null
  })

  let handleUserLogin = function (userObject) {
      if (userObject == null) return
      setLoginStatus(true);
      setCurrentUser({...currUser, email: userObject.email, uid: userObject.uid, uname: userObject.uname});
  }

  return (
    <div className="App">
        <Router>
            <Switch>
                <Route path="/" component={() => <LogInPage userLoginHandler={handleUserLogin}/>} exact />
                <Route path="/chatroom" component={() => <ChatRoom/>}/>
            </Switch>
        </Router>
    </div>
  );
}

export default App;
