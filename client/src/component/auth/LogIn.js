import React, {useState} from 'react';
import './LogIn.css';
import {Alert, Form, Button} from "react-bootstrap";
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

    function submitForm (event) {
        event.preventDefault();
        let url = "http://localhost:8080/user/login?identifier="+userName+"&password="+password;
        let token = '';
        let publicKey = null;
        fetch(url,{
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
            .then(response => {
                if (response != null) {
                    auth.logIn(response, token, publicKey);
                    history.push("chatroom");
                }
            })
            .catch(error=>console.log(error))

    }

    return (
        <div className="login-component-box">
            <div id="website-name-box">
                <h1>LittleChatRoom</h1>
            </div>
            <Form onSubmit={submitForm}>
                <Form.Group controlId="username-field">
                    <Form.Label>
                        Username/Email
                    </Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Please enter your username/email"
                        value={userName}
                        onChange={(username)=>setUserName(username.target.value)}
                    />
                </Form.Group>
                <br/>
                <Form.Group controlId="password-field">
                    <Form.Label>
                        Password
                    </Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Please enter your password"
                        value={password}
                        onChange={(password)=>setPassword(password.target.value)}
                    />
                </Form.Group>
                <br/><br/>
                <Form.Group controlId="submit-btn" id="login-btn-box">
                    <Button variant="primary" type="submit" id="login-btn">Login</Button>
                </Form.Group>

            </Form>
            <div id="registration-btn-box">
                <Link to="/register" id="registration-btn">Register here!</Link>
            </div>
            <div className="error-msg-box">{errorMessage == null ? "": <Alert variant="danger">{errorMessage}</Alert>}</div>
        </div>

    );
}