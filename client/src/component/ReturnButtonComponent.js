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
                        width="48" height="48"
                        viewBox="0 0 172 172"
                        style=" fill:#000000;"><g fill="none" fill-rule="nonzero" stroke="none" stroke-width="1" stroke-linecap="butt" stroke-linejoin="miter" stroke-miterlimit="10" stroke-dasharray="" stroke-dashoffset="0" font-family="none" font-weight="none" font-size="none" text-anchor="none" style="mix-blend-mode: normal"><path d="M0,172v-172h172v172z" fill="none"></path><g fill="#6a5acd"><path d="M86,14.33333c-39.5815,0 -71.66667,32.08517 -71.66667,71.66667c0,39.5815 32.08517,71.66667 71.66667,71.66667c39.5815,0 71.66667,-32.08517 71.66667,-71.66667c0,-39.5815 -32.08517,-71.66667 -71.66667,-71.66667zM121.83333,93.16667h-47.19967l16.43317,16.43317l-10.13367,10.13367l-33.7335,-33.7335l33.7335,-33.7335l10.13367,10.13367l-16.43317,16.43317h47.19967z"/></g></g></svg>);
    const handleGoBack = (event) => {
        // handle page navigation within "signed-in" status

        // handle user logout
    };

    return (
        <div className="return-btn-container">
            <button onClick={handleGoBack}>{btnSVG}</button>
        </div>
    );
}