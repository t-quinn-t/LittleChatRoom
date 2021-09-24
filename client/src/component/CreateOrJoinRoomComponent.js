/**
 * @author Quinn Tao
 * Date Created: Sep 23, 2021
 * Last Updated: Sep 23, 2021
 */

import React, {useState} from "react";
import {Button, Form} from "react-bootstrap";

function CreateOrJoinRoomPage(props) {
    /* ===== ===== ===== States & Hooks ===== ===== ===== */
    const [formPromptFlag, setFormPromptFlag] = useState(-1); // -1 - not shown, 0 - create room form, 1 - join room
    const [userEnteredRoomName, setUserEnteredUserName] = useState();

    // form

    /* ===== ===== ===== Inner Component ===== ===== ===== */
    function CJButton(props) {
        return (
            <div className="cjroom-btn-container">
                <Button variant="primary" value={props.flag} onClick={(event => setFormPromptFlag(event.target.value))}>
                    {props.buttonText}
                </Button>
            </div>
        )
    }

    function CJForm(props) {
        const placeholderFlagNameMapping = [
            "Name of the new room",
            "Name of the room to join"
        ]
        return (
            <Form>
                <Form.FloatingLabel controlId="room-name-input" label={placeholderFlagNameMapping[props.flag]}>
                    <Form.Control type="text" placeholder={placeholderFlagNameMapping[props.flag]} onChange={(event) => {props.handleInput(event.target.value)}}/>
                </Form.FloatingLabel>
            </Form>
        )
    }
    /* ===== ===== ===== Render ===== ===== ===== */
    return (
        <div className="cjroom-container">
            <CJButton flag={0} buttonText="Create a chatroom"/>
            <div>
                <p>OR</p>
            </div>
            <CJButton flag={1} buttonText="Join a chatroom"/>
            {formPromptFlag === -1 ? null : <CJForm flag={formPromptFlag} handleInput={event => setUserEnteredUserName(event.target.value)}/>}
        </div>
    )
}

export default CreateOrJoinRoomPage;
