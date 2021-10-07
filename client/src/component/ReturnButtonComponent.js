/**
 * @author Quinn Tao
 * @creation date Oct 6
 * @last updated Oct 6
 */
import React from "react";
import {useAuth} from "./auth/auth";
import {useHistory} from 'react-router-dom';

function ReturnButton(props) {
    const auth = useAuth();
    const history = useHistory();
    const btnSVG = (<svg xmlns="http://www.w3.org/2000/svg" x="0px" y="0px"
                         width="40" height="40"
                         viewBox="0 0 172 172"
                         style={{fill:"#000000"}}><g fill="#000" fillRule="nonzero" stroke="none" strokeWidth="1" strokeLinecap="butt" strokeLinejoin="miter" strokeMiterlimit="10" strokeDasharray="" strokeDashoffset="0" fontFamily="none" fontWeight="none" fontSize="none" textAnchor="none" mixBlendMode="normal"><path d="M0,172v-172h172v172z" fill="none"/><g fill={props.color}><path d="M73.45833,139.75c-1.30433,0 -2.59075,-0.473 -3.59408,-1.37958l-53.79658,-48.375c-1.13233,-1.01767 -1.78092,-2.4725 -1.78092,-3.99542c0,-1.52292 0.64858,-2.97775 1.78092,-3.99542l53.79658,-48.375c1.57308,-1.419 3.83775,-1.78092 5.77992,-0.91375c1.93858,0.86358 3.18917,2.78783 3.18917,4.90917v26.875h73.45833c2.97058,0 5.375,2.40442 5.375,5.375v32.25c0,2.97058 -2.40442,5.375 -5.375,5.375h-73.45833v26.875c0,2.12133 -1.25058,4.04558 -3.18917,4.90917c-0.69875,0.31533 -1.44408,0.46583 -2.18583,0.46583z"/></g></g></svg>);
    const handleGoBack = () => {
        if (history.location.pathname === "/chatroom") {
            auth.logOut();
        }
        history.goBack();
    };

    return (
        <div className="return-btn-container" style={{
            position: "absolute",
            top: props.top,
            right: props.right,
            left: props.left,
            width: 48,
            height: 48,
        }}>
            <button onClick={handleGoBack} style={{
                background: "none",
                border: "none",
                padding: 0
            }}>
                {btnSVG}
            </button>
        </div>
    );
}

export default ReturnButton;