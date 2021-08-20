import React, {useState} from 'react';
import './LogIn.css';
import {Alert} from "react-bootstrap";
import {useHistory, Link} from 'react-router-dom';
import {useAuth} from "./auth";

export default function LogInPage(props) {

    // these are states for form, the actual user authentication state is stored
    //   globally in useAuth hook
    const [userName, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState(null);
    const auth = useAuth();
    let history = useHistory();

    async function submitForm () {
        let url = "http://localhost:8080/user/login?identifier="+userName+"&password="+password;
        let token = '';
        let publicKey = null;
        const response = await fetch(url,{
            method:"GET",
        })
            .then(response => {
                if (response.ok) {
                    token = response.headers.get("token");
                    publicKey = response.headers.get("public-key");
                    return response.json();
                } else {
                    // response body is text
                    return response.text();
                }
            })
            .then(response => {
                if (typeof response === "string") {
                    setErrorMessage(response);
                    return null;
                }
                setErrorMessage(null);
                return response;
            })
            .catch(error=>console.log(error));
        if (response != null) {
            auth.logIn(response, token, publicKey);
            history.push("chatroom");
        }
    }

    return (
        <div className="login-component-box">
            <div>
                <label>
                    Username
                    <input
                        type="text"
                        placeholder="Please enter your username/email"
                        value={userName}
                        onChange={(username)=>setUserName(username.target.value)}
                    />
                </label>
                <br/>
                <label>Password</label>
                <input
                    type="text"
                    placeholder="Please enter your password"
                    value={password}
                    onChange={(password)=>setPassword(password.target.value)}
                />
                <br/>
                <button onClick={submitForm}>Log In</button>
            </div>
            <div>
                <Link to="/register">Register here!</Link>
            </div>
            <div className="error-msg-box">{errorMessage == null ? "": <Alert variant="danger">{errorMessage}</Alert>}</div>
        </div>

    );
}