/**
 * @author Quinn Tao
 * @last updated Oct 15
 */

import React, {useEffect, useState} from 'react';
import {Col, Form, FormLabel, Row, Button} from "react-bootstrap";
import {useAuth} from "./auth/auth";
import {getFontSize, getAccentColor} from "../util";
import ReturnButton from "./ReturnButtonComponent";
import "./style/userSettins.css";

function UserSettingsForm(props) {
    const auth = useAuth();
    const storage = window.sessionStorage;

    function handleUserSettingsSubmission (event) {

        event.preventDefault();

        // Capture a snapshot of all current state values
        const unameInput = _unameInput;
        const emailInput = _emailInput;
        const passwordInput = _passwordInput;
        const colorInput = _colorInput;
        const fontSizeInput = _fontSizeInput;

        const url = "http://localhost:8080/user/update?" +
            "uid=" + auth.user.uid +
            (unameInput === auth.user.uname || unameInput == "" ? "" : ("&" + "uname=" + unameInput)) +
            (emailInput === auth.user.email || emailInput == "" ? "" : ("&" + "email=" + emailInput)) +
            (passwordInput === "" ? "" : ("&" + "newPassword=" + passwordInput)) + "&" +
            "serializedUserSettings=" + encodeURIComponent(JSON.stringify({
                "accentColor": colorInput,
                "fontSize": fontSizeInput
            }));

        // Update backend
        fetch(url, {
            method: "POST",
            headers: {
                token: auth.token,
                publicKey: auth.publicKey
            }
        })
            .then(res => {
                if (res.ok) {
                    storage.setItem("accentColor", colorInput);
                    storage.setItem("fontSize", fontSizeInput);
                    auth.updateName(unameInput);
                    auth.updateEmail(emailInput);
                }
            })
            .catch(err => console.log("Error!!!!"))
    }

    // These are the states used for holding values from input fields temporarily
    const [_passwordInput, setPasswordInputT] = useState("");
    const [_unameInput, setUnameInputT] = useState(auth.user.uname);
    const [_emailInput, setEmailInputT] = useState(auth.user.email);
    const [_colorInput, setColorInputT] = useState("#fff");
    const [_fontSizeInput, setFontSizeInputT] = useState(0);
    return (
        <div className="settings-form-container" style={{fontSize: getFontSize()}}>
            <ReturnButton top={10} left={10} color={getAccentColor()}/>
            <Form onSubmit={handleUserSettingsSubmission}>
                <Form.Group as={Row} className="mb-3" controlId="password-input-form-row">
                    <FormLabel column sm="3" className="settings-form-labels">
                        Reset Password
                    </FormLabel>
                    <Col sm={"9"}>
                        <Form.Control type="password" onChange={e => setPasswordInputT(e.target.value)}/>
                    </Col>
                </Form.Group>
                <Form.Group as={Row} className="mb-3" controlId="uname-input-form-row">
                    <FormLabel column sm="3" className="settings-form-labels">
                        Rename yourself
                    </FormLabel>
                    <Col sm={"9"}>
                        <Form.Control type="text" onChange={e => setUnameInputT(e.target.value)}/>
                    </Col>
                </Form.Group>
                <Form.Group as={Row} className="mb-3" controlId="email-input-form-row">
                    <FormLabel column sm="3" className="settings-form-labels">
                        Reset email address
                    </FormLabel>
                    <Col sm={"9"}>
                        <Form.Control type="password" onChange={e => setEmailInputT(e.target.value)}/>
                    </Col>
                </Form.Group>
                <Form.Group as={Row} className="mb-3" controlId="color-input-form-row">
                    <FormLabel column sm="3" className="settings-form-labels">
                        Accent color
                    </FormLabel>
                    <Col sm={"9"}>
                        <Form.Control type="color" placeholder="#fff" onChange={e => setColorInputT(e.target.value)}/>
                    </Col>
                </Form.Group>
                <Form.Group as={Row} className="mb-3" controlId="fontsize-input-form-row">
                    <FormLabel column sm="3" className="settings-form-labels">
                        Text size
                    </FormLabel>
                    <Col sm={"9"}>
                        <div className="mb-3">
                            <Form.Check inline type="radio" label="A" name="txt-size-opt" id="1"
                                        onClick={e => setFontSizeInputT(0)}
                                        style={{fontSize: "16px"}}
                            />
                            <Form.Check inline type="radio" label="A" name="txt-size-opt" id="2"
                                        onClick={e => setFontSizeInputT(1)}
                                        style={{fontSize: "18px"}}
                            />
                            <Form.Check inline type="radio" label="A" name="txt-size-opt" id="3"
                                        onClick={e => setFontSizeInputT(2)}
                                        style={{fontSize: "20px"}}
                            />
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