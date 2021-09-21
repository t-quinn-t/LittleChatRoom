import React, {useEffect, useState} from 'react'
import {Button, ListGroup, Col, Container, Form, Nav, Row, ListGroupItem} from 'react-bootstrap'
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
    const getPushUrl = () => "/chat/send-to/" + currentRoom.cid + '/';

    /**
     * Sets up Websocket Connection over stomp; returns a stomp client
     */
    const [isClientConnected, updateClientConnectionStatusTo] = useState(false);
    const [stompClient, setStompClient] = useState(() => {
        let socket = new SockJS(wsRootUrl);
        const tempClient = Stomp.over(socket);
        tempClient.connect({}, (frame)=>updateClientConnectionStatusTo(true));
        return tempClient;
    })


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
        if (currentRoom == null) return;

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

        if (stompClient.connected)
            stompClient.subscribe(getTopicUrl(), (message) => {
                setMessageSet((prevState => {
                    return [...prevState, JSON.parse(message.body)]
                }))
            }, {});

        return () => {
            if (currentRoomStompSubscription != null) currentRoomStompSubscription.unsubscribe();
            setCurrentRoomStompSubscription(null);
        }
    }, [currentRoom]);

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
                    <Col md={3} id="chatroom-list-container">
                        <ListGroup size="lg" variant="flush">
                            {chatRoomList.map((chatroom, index )=> {
                                return (
                                    <ListGroupItem key={"room-"+index} variant="light" action onClick={(e) => {
                                        setCurrentRoom(chatroom);
                                    }}>{chatroom.name}</ListGroupItem>
                                )
                            })}
                        </ListGroup>
                    </Col>
                    <Col md={9} id="chatroom-col">
                        <div className="chatroom-scroller">
                            <ul>
                                {messageSet.map((message, index) => {
                                    return  <li key={index}>
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
                        <div id="send-form-box">
                            <Form onSubmit={sendMessage}>
                                <Row>
                                    <Form.Group as={Col} md="2" controlId="send-button" className="send-message-btn-container">
                                        <Button variant="primary" type="submit" id="send-message-btn">
                                            Send
                                        </Button>
                                    </Form.Group>
                                    <Form.Group as={Col} md="10" controlId="get-message-body" className="message-text-input-container">
                                        <Form.Control type="text" placeholder="type something" className="message-text-input"
                                        onChange={(event) => {
                                            setCurrentTypingMessage(event.target.value);
                                        }}/>
                                    </Form.Group>
                                </Row>
                            </Form>
                        </div>

                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export default ChatRoom;