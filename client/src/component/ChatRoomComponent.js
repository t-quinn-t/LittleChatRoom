import React, {useEffect, useState} from 'react'
import {Button, Col, Container, Form, Nav, Row} from 'react-bootstrap'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import './style/chatRoomStyle.css'
import {useAuth} from "./auth/auth";

function ChatRoom(props) {

    const auth = useAuth();

    /* ===== ===== ===== Websocket ===== ===== ===== */
    const wsRootUrl = "http://localhost:8080/websocket";

    // Room subscription route
    const getTopicUrl = () => "/topic/" + currentRoom.cid + '/';

    // Message pushing route
    const getPushUrl = () => "/chat/send-to/" + currentRoom.cid;

    /**
     * Sets up Websocket Connection over stomp; returns a stomp client
     */
    const [stompClient, setStompClient] = useState(() => {
        let socket = new SockJS(wsRootUrl);
        return Stomp.over(socket);
    });

    /* ===== ===== ===== Chatrooms ===== ===== ===== */
    const [currentRoom, setCurrentRoom] = useState(null);
    const [chatRoomList, setChatRoomList] = useState([]);
    useEffect(() => {
        const url = "http://localhost:8080/chatroom/get-user-rooms?uid=" + auth.user.uid;
        fetch(url, {
            headers: {
                token: auth.token,
                publicKey: auth.publicKey
        }})
            .then(response => response.json())
            .then(res => {
                if (res._embedded.chatrooms.length !== 0) {
                    setChatRoomList(res._embedded.chatrooms);
                    setCurrentRoom(res._embedded.chatrooms[0]);
                }
            })
            .catch(reason => alert(reason))

    }, []);

    /**
     * When the status of current chatroom changed, triggers corresponding actions:
     *   1. if current room is null, unsubscribe from stomp client and disconnect ws
     *   2. if current room changed/initialized, start new subscription to chatroom,
     *          if ws is not already connected, initialize a new connection
     */
    let [currentRoomStompSubscription, setCurrentRoomStompSubscription] = useState(null);
    useEffect(() => {
        if (currentRoom == null) {
            // unsubscribe and disconnect
            if (currentRoomStompSubscription != null) {
                currentRoomStompSubscription.unsubscribe();
                stompClient.disconnect();
            }

            return;
        };

        // Load messages from destination chatroom
        const getMessagesReqURL = 'http://localhost:8080/get-messages/' + currentRoom.cid + '?uid=' + auth.user.uid;
        const headers = {
            token: auth.token,
            publicKey: auth.publicKey
        }
        fetch(getMessagesReqURL, {headers: headers})
            .then(response => {
                return response.json();
            })
            .then(
            res => {
                if (!res._embedded)
                    setMessageSet([])
                else {
                    const messages = res._embedded.messages;
                    setMessageSet(messages);
                    }
                }
            ).catch(reason => {
                alert(reason);
            })

        // Subscribe to the room
        stompClient.connect();
        if (currentRoomStompSubscription != null) currentRoomStompSubscription.unsubscribe();
        setCurrentRoomStompSubscription(stompClient.subscribe(getTopicUrl(), (message) => {
            console.log("message received" + message.body);
        }));

        return () => {
            if (currentRoomStompSubscription != null) currentRoomStompSubscription.unsubscribe();
            setCurrentRoomStompSubscription(null);
            stompClient.disconnect();
        }
    }, currentRoom);

    const switchChatroom = function (eventKey) {

    }

    /* ===== ===== ===== Messaging ===== ===== ===== */
    let [messageSet, setMessageSet] = useState([]);
    let [currentTypingMessage, setCurrentTypingMessage] = useState("");
    /**
     * Sends the message (json) over stomp;
     *   the header of this message is the token generated when user logged in
     */
    const sendMessage = function (event) {
        // pass the jwt as stomp header
        const headers = {
            token: auth.token,
            publicKey: auth.publicKey
        }
        stompClient.send(getPushUrl(), headers, JSON.stringify({
            'id': -1,
            'sender': auth.user.uname,
            'roomId': currentRoom.cid,
            'content': currentTypingMessage
        }));
    }

    return (
        <div className="chatroom-container">
            <Container fluid>
                <Row>
                    <Col md={3}>
                        <Nav defaultActiveKey="/home" className="flex-column" onSelect={switchChatroom}>
                            {chatRoomList.map(chatroom => {
                                return <Nav.Link eventKey={chatroom.cid}>{chatroom.name}</Nav.Link>
                    })}
                        </Nav>
                    </Col>
                    <Col md={9}>
                        <div className="chatroom-scroller">
                            <ul>
                                {messageSet.map((message) => {
                                    return  <li>
                                                <div className="message-row-wrapper">
                                                    <div className={message.sender === auth.user.uname ? "message-my-name-box": "message-user-name-box"}>
                                                        {message.sender}
                                                    </div>
                                                    <div className={
                                                        message.sender === auth.user.uname ? "chatroom-ours-message-box" : "chatroom-others-message-box"
                                                    }>
                                                        {message.content}
                                                    </div>
                                                </div>
                                            </li>
                                })}
                            </ul>
                        </div>
                        <Form onSubmit={sendMessage}>
                            <Row>
                                <Form.Group as={Col} md="1" controlId="send-button">
                                    <Button variant="primary" type="submit">
                                        Send
                                    </Button>
                                </Form.Group>
                                <Form.Group as={Col} md="10" controlId="get-message-body">
                                    <Form.Control type="text" placeholder="type something" className="message-text-input"
                                    onChange={(event) => {
                                        setCurrentTypingMessage(event.target.value);
                                    }}/>
                                </Form.Group>
                            </Row>
                        </Form>

                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export default ChatRoom;