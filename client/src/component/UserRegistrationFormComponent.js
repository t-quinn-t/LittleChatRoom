import {Row, Col, Container, Form, Button, Alert} from "react-bootstrap";
import React, {useState} from "react";
import './style/userRegisterForm.css'

function UserRegistrationForm (props) {

    let [unameFromForm, setUnameFromForm] = useState(null);
    let [emailFromForm, setEmailFromForm] = useState(null);
    let [passwordFromForm, setPasswordFromForm] = useState(null);
    let [registrationStatus, setRegistrationStatus] = useState(0); // 0 - init, 1 - success, 2 - failure
    const isLoggedIn = true;
    const registrationMessageList = [
        (<div/>),
        (
            <Alert key={"reg-msg-0"} variant="success">
                Registration Succeeded! Welcome to Little Chat Room!
            </Alert>
        ),
        (
            <Alert key={"reg-msg-1"} variant="danger">
                Sorry! Something does not seem right...
            </Alert>
        )
    ]

    let registerUser = async function (event) {
        event.preventDefault();
        let url = "http://localhost:8080/user/register?" +
            "uname=" + unameFromForm + "&" +
            "email=" + emailFromForm + "&" +
            "password=" + passwordFromForm;
        await fetch(url, {
            method: 'POST'
        }).then(response => {
            if (response.ok) {
                handleRegistrationSuccess(response.json(), event.target);
            }
            else
                handleRegistrationFailure();
        }).catch(reason => {
            alert(reason + reason.getMessage())
        })
    }

    let handleRegistrationSuccess = function (registeredUser, form) {
        form.reset();
        setRegistrationStatus(1);
    }

    let handleRegistrationFailure = function () {
        document.getElementById("registration-form").reset();
        setRegistrationStatus(2);
    }

    return (
        <div className="form-container">
            <Form onSubmit={registerUser} id="registration-form">
                <Container fluid className="user-registration-form-box">
                    <div className="website-name-box">
                        <h1>LittleChatRoom</h1>
                    </div>
                    <Form.FloatingLabel controlId="name-input" label="Your name">
                        <Form.Control type="text" placeholder="Mr Unknown" onChange={(event) => {setUnameFromForm(event.target.value)}}/>
                    </Form.FloatingLabel>
                    <br/>
                    <Form.FloatingLabel controlId="floatingInput" label="Email address">
                        <Form.Control type="email" placeholder="name@example.com" onChange={(event) => {setEmailFromForm(event.target.value)}}/>
                    </Form.FloatingLabel>
                    <br/>
                    <Form.FloatingLabel controlId="floatingPassword" label="Password">
                        <Form.Control type="password" placeholder="Password" onChange={(event) => {setPasswordFromForm(event.target.value)}}/>
                    </Form.FloatingLabel>
                    <br/><br/>
                    <Row>
                        <Form.Group controlId="submit-button" id="register-btn-box" as={Col} xxl={{span:2, offset:5}}>
                            <Button type="submit" variant="primary">Register Now</Button>
                        </Form.Group>
                    </Row>
                    <br/><br/>
                    <Row style={{"margin-top:": "60px"}}>
                        {registrationMessageList[registrationStatus]}
                    </Row>
                </Container>
            </Form>
        </div>
    )
}

export default UserRegistrationForm;

