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
        alert("sending fetch")
        let url = "http://localhost:8080/user/register?" +
            "uname=" + unameFromForm + "&" +
            "email=" + emailFromForm + "&" +
            "password=" + passwordFromForm;
        await fetch(url, {
            method: 'POST'
        }).then(response => {
            alert("finish fetch")
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
                <div style={{"margin": "auto", "text-align": "center", "margin-bottom": "30px"}}>
                    <h1>Little Chat Room</h1>
                </div>
                <Container fluid>
                    <Form.FloatingLabel controlId="name-input" label="Your name">
                        <Form.Control type="text" placeholder="Mr Unknown" onChange={(event) => {setUnameFromForm(event.target.value)}}/>
                    </Form.FloatingLabel>
                    <Form.FloatingLabel controlId="floatingInput" label="Email address">
                        <Form.Control type="email" placeholder="name@example.com" onChange={(event) => {setEmailFromForm(event.target.value)}}/>
                    </Form.FloatingLabel>
                    <Form.FloatingLabel controlId="floatingPassword" label="Password">
                        <Form.Control type="password" placeholder="Password" onChange={(event) => {setPasswordFromForm(event.target.value)}}/>
                    </Form.FloatingLabel>
                    <Row>
                        <Form.Group controlId="submit-button" as={Col} xxl={{span:2, offset:5}}>
                            <Button type="submit" variant="primary" style={{"margin-top": "20px", "width": "100%", "height": "50px"}}>Register Now</Button>
                        </Form.Group>
                    </Row>
                    <br/><br/><br/>
                    <Row style={{"margin-top:": "60px"}}>
                        {registrationMessageList[registrationStatus]}
                    </Row>
                </Container>
            </Form>
        </div>
    )
}

export default UserRegistrationForm;

