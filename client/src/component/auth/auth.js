import React, {useContext, createContext, useEffect, useState} from 'react';
import {useCookies} from "react-cookie";

/**
 * This auth utility file provides a useAuth hook which makes the authentication state globally availble.
 *   It contains two parts:
 *      1. Create a Context for state to be globally available
 *      2. Management of the auth state itself
 *
 * Inspired and Edited from: https://usehooks.com/useAuth/
 * @author Quinn Tao
 * @date Aug 9, 2021
 */

/* ===== ===== ===== Generate Context ===== ===== ===== */
const authContext = createContext();

export function useAuth() {
    return useContext(authContext);
}

export function ProvideAuth({children}) {
    const auth = useProvideAuth(); // make the auth hook globally accessible
    return (
        <authContext.Provider value={auth}>{children}</authContext.Provider>
    )
}

/* ===== ===== ===== Authentication ===== ===== ===== */
function useProvideAuth() {
    const [isLoggedIn, setLoginStatus] = useState(false);
    const [currUser, setCurrentUser] = useState({
        uname: null,
        uid: -1,
        email: null
    });
    const [cookies, setCookies, removeCookies] = useCookies(['token']);

    let handleUserLogin = function (user, token) {
        if (user == null) return;
        setLoginStatus(true);
        setCurrentUser({...currUser, email: user.email, uid: user.uid, uname: user.uname});
        setCookies('token', token);
    }

    let handleUserLogout = function (user, token) {
        if (user == null) return;
        setLoginStatus(false);
        setCurrentUser({...currUser, uname: null, uid: -1, email: null})
        removeCookies('token');
    }

    let getToken = function () {
        return cookies.token;
    }

    return {
        status: isLoggedIn,
        user: currUser,
        token: getToken(),
        logIn: handleUserLogin,
        logOut: handleUserLogout
    }
}