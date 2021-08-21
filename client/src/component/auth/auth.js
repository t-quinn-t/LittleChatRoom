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

    const storage = window.sessionStorage;

    const [isLoggedIn, setLoginStatus] = useState(() => {
        const serializedUser = getSerializedUser();
        return serializedUser != null;
    });
    const [currUser, setCurrentUser] = useState(() => {
        // store username/uid in session storage
        const serializedUser = getSerializedUser();
        if (serializedUser) {
            return JSON.parse(serializedUser);
        } else {
            return {
                uname: null,
                uid: -1,
                email: null
            }
        }
    });
    const [cookies, setCookies, removeCookies] = useCookies(['token', 'publicKey']);

    let handleUserLogin = function (user, token, publicKey) {
        if (user == null) return;
        setLoginStatus(true);
        setCurrentUser({...currUser, email: user.email, uid: user.uid, uname: user.name});
        storage.setItem("user", serializeUser(user));
        setCookies('token', token);
        setCookies('publicKey', publicKey);
    }

    let handleUserLogout = function (user, token) {
        if (user == null) return;
        setLoginStatus(false);
        setCurrentUser({...currUser, uname: null, uid: -1, email: null})
        removeCookies('token');
        removeCookies('publicKey');
    }

    let getToken = function () {
        return cookies.token;
    }

    let getPublicKey = function () {
        return cookies.publicKey;
    }

    return {
        status: isLoggedIn,
        user: currUser,
        token: getToken(),
        publicKey: getPublicKey(),
        logIn: handleUserLogin,
        logOut: handleUserLogout
    }
}

function serializeUser(user) {
    return JSON.stringify({
        "uname": user.uname,
        "uid": user.uid,
        "email": user.email
    })
}

function getSerializedUser() {
    const storage = window.sessionStorage;
    return storage.getItem("user");
}
