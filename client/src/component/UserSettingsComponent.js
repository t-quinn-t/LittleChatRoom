/**
 * @author Quinn Tao
 * @last updated Oct 15
 */

import React, {useEffect, useState} from 'react';
import {Col, Form, FormLabel, Row, Button} from "react-bootstrap";
import {useAuth} from "./auth/auth";
import {useCookies} from "react-cookie";

function UserSettingsForm(props) {
    const auth = useAuth();
    const storage = window.sessionStorage;

    const [passwordInput, setPasswordInput] = useState("");
    const [unameInput, setUnameInput] = useState(auth.user.uname);
    const [emailInput, setEmailInput] = useState(auth.user.email);
    const [colorInput, setColorInput] = useState("#fff");
    const [fontSizeInput, setFontSizeInput] = useState(0);

    function assembleRequestURL() {
        return "http://localhost:8080/user/update?" +
            "uid=" + auth.user.uid +
            unameInput === auth.user.uname ? "" : ("&" + "uname=" + unameInput) +
            emailInput === auth.user.email ? "" : ("&" + "email=" + emailInput) +
            passwordInput === "" ? "" : ("&" + "newPassword=" + passwordInput) +
            JSON.stringify({
                "accentColor": colorInput,
                "fontSize": fontSizeInput
            })
    }

    function handleUserSettingsSubmission (event) {
        // Update Account
        fetch(assembleRequestURL())
            .then(res => {
                if (res.ok) {
                    storage.setItem("accentColor", colorInput);
                    storage.setItem("fontSize", fontSizeInput);

                }
            })
    }

    return (
        <div className="settings-form-container">
            <Form>
                <Form.Group as={Row} className="mb-3" controlId="password-input-form-row">
                    <FormLabel column sm="1" className="settings-form-labels">
                        Reset Password
                    </FormLabel>
                    <Col sm={"11"}>
                        <Form.Control type="password"/>
                    </Col>
                </Form.Group>
                <Form.Group as={Row} className="mb-3" controlId="uname-input-form-row">
                    <FormLabel column sm="1" className="settings-form-labels">
                        Rename yourself
                    </FormLabel>
                    <Col sm={"11"}>
                        <Form.Control type="text"/>
                    </Col>
                </Form.Group>
                <Form.Group as={Row} className="mb-3" controlId="email-input-form-row">
                    <FormLabel column sm="1" className="settings-form-labels">
                        Reset email address
                    </FormLabel>
                    <Col sm={"11"}>
                        <Form.Control type="password">

                        </Form.Control>
                    </Col>
                </Form.Group>
                <Form.Group as={Row} className="mb-3" controlId="color-input-form-row">
                    <FormLabel column sm="1" className="settings-form-labels">
                        Accent color
                    </FormLabel>
                    <Col sm={"11"}>
                        <Form.Control type="color" placeholder="#fff">

                        </Form.Control>
                    </Col>
                </Form.Group>
                <Form.Group as={Row} className="mb-3" controlId="fontsize-input-form-row">
                    <FormLabel column sm="1" className="settings-form-labels">
                        Accent color
                    </FormLabel>
                    <Col sm={"11"}>
                        <div className="mb-3">
                            <Form.Check inline type="radio" label="A" name="txt-size-opt" id="1"/>
                            <Form.Check inline type="radio" label="A" name="txt-size-opt" id="2"/>
                            <Form.Check inline type="radio" label="A" name="txt-size-opt" id="3"/>
                        </div>
                    </Col>
                </Form.Group>
                <Button type="submit" variant="primary">
                    Save
                </Button>
            </Form>
        </div>
    )
}

export default UserSettingsForm;