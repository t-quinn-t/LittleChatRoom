import React from 'react';
import {useState} from 'react';
import './LogIn.css';


export default function LogInPage() {
    const [userName, setUserName] = useState('');
    const [password, setPassword] = useState('');

    async function submitForm () {
        let url = "http://localhost:8080/user/login?identifier="+userName+"&password="+password;
        const response = await fetch(url,{
            method:"GET",
        }).catch(error=>console.log(error));
    }

    return (
        <div>
            <label>
                Username
                <input
                    type="text"
                    placeholder="Please enter your username/email"
                    value={userName}
                    onChange={(username)=>setUserName(username.target.value)}
                />
            </label>
            <br/>
            <label>Password</label>
            <input
                type="text"
                placeholder="Please enter your password"
                value={password}
                onChange={(password)=>setPassword(password.target.value)}
            />
            <br/>
            <button onClick={submitForm}>Log In</button>
        </div>
    );
}