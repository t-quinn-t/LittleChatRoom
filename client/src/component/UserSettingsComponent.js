/**
 * @author Quinn Tao
 * @last updated Oct 14
 */

import React from 'react';
import {Col, Form, FormLabel, Row, FormCheck} from "react-bootstrap";
import {useAuth} from "./auth/auth";

function UserSettingsForm(props) {
    let auth = useAuth();

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
                <Form.Group as={Row} className="mb-3" controlId="password-input-form-row">
                    <FormLabel column sm="1" className="settings-form-labels">
                        Accent color
                    </FormLabel>
                    <Col sm={"11"}>
                        <Form.Control type="color" placeholder="#fff">

                        </Form.Control>
                    </Col>
                </Form.Group>
                <Form.Group as={Row} className="mb-3" controlId="password-input-form-row">
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
            </Form>
        </div>
    )
}

export default UserSettingsForm;