import React, {useState} from 'react'
import {BrowserRouter as Router, Switch, Route} from "react-router-dom"
import 'bootstrap/dist/css/bootstrap.min.css';

import LogInPage from './component/auth/LogIn.js'
import ChatRoom from "./component/ChatRoomComponent";
import { useCookies } from "react-cookie";

function App() {

  /* ===== ===== ===== User Login ===== ===== ===== */
  const [isLoggedIn, setLoginStatus] = useState(false);
  const [currUser, setCurrentUser] = useState({
      uname: null,
      uid: -1,
      email: null
  });
    const [cookies, setCookies, removeCookies] = useCookies(['jwt']);
    let handleUserLogin = function (responseObject) {
        if (responseObject == null) return
        setLoginStatus(true);
        setCurrentUser({...currUser, email: responseObject.email, uid: responseObject.uid, uname: responseObject.uname});

        console.log(responseObject)
        setCookies('jwt', responseObject.headers.get('text'));
  }


    return (
        <div className="App">
            <Router>
                <Switch>
                    <Route path="/" component={() => <LogInPage userLoginHandler={handleUserLogin}/>} exact />
                    {/*Todo: Change the chatroom id to proper chatroom */}
                    <Route path="/chatroom" component={() => <ChatRoom roomId={1} uid={currUser.uid}/>}/>
                </Switch>
            </Router>
        </div>

  );
}

export default App;
