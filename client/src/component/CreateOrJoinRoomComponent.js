/**
 * @author Quinn Tao
 * Date Created: Sep 23, 2021
 * Last Updated: Oct 5, 2021
 */

import React, {useState} from "react";
import {Button, Form} from "react-bootstrap";
import {useAuth} from "./auth/auth";
import {useHistory} from 'react-router-dom';
import './style/createOrJoinRoomPage.css';
import ReturnButtonComponent from './ReturnButtonComponent'

function CreateOrJoinRoomPage(props) {
    /* ===== ===== ===== States & Hooks ===== ===== ===== */
    const [userOptionFlag, setUserOptionFlag] = useState(-1); // -1 - not shown, 0 - create room form, 1 - join room

    /* ===== ===== ===== Inner Component ===== ===== ===== */
    function CJButton(props) {
        return (
            <div className="cjroom-btn-container">
                <Button variant="primary" value={props.flag} onClick={(event => setUserOptionFlag(event.target.value))}>
                    {props.buttonText}
                </Button>
            </div>
        )
    }

    function CJForm(props) {
        const auth = useAuth();
        const history = useHistory();
        const [userEnteredRoomName, setUserEnteredUserName] = useState();

        const placeholderFlagNameMapping = [
            "Name of the new room",
            "Name of the room to join"
        ];

        /* ===== ===== ===== HTTP Requests ===== ===== ===== */
        const registerNewRoom = () => {
            fetch(
                "http://localhost:8080/chatroom/create-room?" +
                "uid=" + auth.user.uid + "&" +
                "newRoomName=" + userEnteredRoomName,
                {
                    method: 'POST',
                    headers: {
                        token: auth.token,
                        publicKey: auth.publicKey
                    }
                }
            )
                .then(response => {
                    if (response.ok) {
                        history.goBack();
                    }
                })
                .catch(reason => {
                    console.log(reason);
                });

        }

        const joinExistingRoom = () => {
            fetch(
                "http://localhost:8080/chatroom/add-user-to-room?" +
                "uid=" + auth.user.uid + "&" +
                "roomName=" + userEnteredRoomName,
                {
                    method: 'POST',
                    headers: {
                        token: auth.token,
                        publicKey: auth.publicKey
                    }
                }
            )
                .then(response => {
                    if (response.ok) {
                        history.goBack();
                    }
                })
                .catch(reason => {
                    console.log(reason);
                    alert("Something is wrong when entering the chatroom");
                });

        }

        return (
            <Form onSubmit={(event) => {
                event.preventDefault();
                if (props.flag == 0) registerNewRoom();
                else joinExistingRoom();
            }}
                  className="cjroom-form"
            >
                <Form.FloatingLabel controlId="room-name-input" label={placeholderFlagNameMapping[props.flag]}>
                    <Form.Control type="text" placeholder={placeholderFlagNameMapping[props.flag]} onChange={(event) => {setUserEnteredUserName(event.target.value)}}/>
                </Form.FloatingLabel>
                <Button type="submit" variant="primary">Submit</Button>
            </Form>
        );
    }

    /* ===== ===== ===== Render ===== ===== ===== */
    return (
        <div className="cjroom-container">
            <CJButton flag={0} buttonText="Create a chatroom"/>
            <div>
                <p>OR</p>
            </div>
            <CJButton flag={1} buttonText="Join a chatroom"/>
            {userOptionFlag === -1 ? null : <CJForm flag={userOptionFlag}/>}
            <ReturnButtonComponent top={10} left={10} color="slateblue"/>
        </div>
    );
}

export default CreateOrJoinRoomPage;
