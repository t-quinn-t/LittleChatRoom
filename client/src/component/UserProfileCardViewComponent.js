import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {useAuth} from "./auth/auth";
import "./style/userProfileCardViewStyle.css"
import ReturnButton from "./ReturnButtonComponent";
import {getAccentColor, getFontSize} from "../util";
/* <a href="https://icons8.com/icon/UXWIv5G5mWsK/settings">Settings icon by Icons8</a> */

function UserProfileCardView(props) {
    const auth = useAuth();
    const [clock, setClock] = useState(() => new Date().toLocaleTimeString());
    const [clockIntervalID, setClockIntervalID] = useState();

    useEffect(() => {
        setInterval(updateClock, 1000);
        return clearInterval(clockIntervalID);
    }, clockIntervalID);
    let updateClock = function () {
        setClock(new Date().toLocaleTimeString());
    }

    return (
        <div className="user-profile-card-view-container" style={{fontSize: getFontSize(), backgroundColor: getAccentColor()}}>
            <div className="user-clock-container">
                <p id="user-clock">{clock}</p>
            </div>
            <div className="user-info-container">
                <h1>{auth.user.uname}</h1>
            </div>
            <div className="user-settings-btn-container">
                <Link to="settings">
                    <img src="https://img.icons8.com/ios-filled/36/ffffff/settings.png"/>
                </Link>
            </div>
            <ReturnButton top={70} right={10} color="white"/>
        </div>
    )
}

export default UserProfileCardView;