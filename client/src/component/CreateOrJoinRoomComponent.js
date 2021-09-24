/**
 * @author Quinn Tao
 * Date Created: Sep 23, 2021
 * Last Updated: Sep 23, 2021
 */

import React, {useState} from "react";
import {Button} from "react-bootstrap";

function CreateOrJoinRoomPage(props) {
    /* ===== ===== ===== States & Hooks ===== ===== ===== */
    const [formPromptFlag, setFormPromptFlag] = useState(0); // 0 - not shown, 1 - create room form, 2 - join room form

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
    /* ===== ===== ===== Render ===== ===== ===== */
    return (
        <div className="cjroom-container">
            <CJButton flag={1} buttonText="Create a chatroom"/>
            <div>
                <p>OR</p>
            </div>
            <CJButton flag={2} buttonText="Join a chatroom"/>
            <p>{formPromptFlag}</p>
        </div>
    )
}

export default CreateOrJoinRoomPage;
