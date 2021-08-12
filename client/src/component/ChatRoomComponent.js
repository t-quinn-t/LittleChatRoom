import React, {useEffect, useState} from 'react'
import {Nav, Container, Row, Col, Button, Form} from 'react-bootstrap'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import './style/chatRoomStyle.css'
import {useAuth} from "./auth/auth";

function ChatRoom(props) {

    const auth = useAuth();

    /* ===== ===== ===== Websocket ===== ===== ===== */
    const websocketConfig = {
        wsUrl: "http://localhost:8080/websocket",
        roomUrl: "/topic/" + props.roomId + '/',
        echoUrl: "/chat/send-to/1/"
    }
    const [isWSConnected, setWSConnectionStatus] = useState(false);
    const [stompClient, setStompClient] = useState(() => {
        let socket = new SockJS(websocketConfig.wsUrl)
        let stompClient = Stomp.over(socket)
        stompClient.connect({}, (frame) => {
            console.log("connected" + frame);
            setWSConnectionStatus(true);
        });
        return stompClient;
    })
    useEffect(() => {
        if (isWSConnected)

            stompClient.subscribe(websocketConfig.roomUrl, (message) => {
                console.log("message received" + message.body);
            });
    }, [isWSConnected]);

    // TODO: token + public key -> payload

    /* ===== ===== ===== Chatrooms ===== ===== ===== */
    // TODO: render the correct list of chatrooms
    const [chatRoomList, setChatRoomList] = useState([]);
       /* stub */
    useEffect(() => {

        setChatRoomList(orig => ["chatroom1", "chatroom2", "adminRoom"]);
    }, []);

    /* ===== ===== ===== Messaging ===== ===== ===== */
    let [messageSet, setMessageSet] = useState([]);
    let [currentTypingMessage, setCurrentTypingMessage] = useState("");
    useEffect(() => {
        const getMessagesReqURL = 'http://localhost:8080/get-messages/' + props.roomId;
        fetch(getMessagesReqURL).then(response => response.json()).then(
            res => {
                const messages = res._embedded.messages;
                setMessageSet(messages);
            }
        );
    }, [])

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
        stompClient.send(websocketConfig.echoUrl, headers, JSON.stringify({
            'id': -1,
            'senderId': props.uid,
            'roomId': props.roomId,
            'content': currentTypingMessage
        }));
    }

    return (
        <div className="chatroom-container">
            <Container fluid>
                <Row>
                    <Col md={3}>
                        <Nav defaultActiveKey="/home" className="flex-column">
                            {chatRoomList.map(chatroom => {
                                return <Nav.Link eventKey={chatroom}>{chatroom}</Nav.Link>
                    })}
                        </Nav>
                    </Col>
                    <Col md={9}>
                        <div className="chatroom-scroller">
                            <ul>
                                {messageSet.map((message) => {
                                    return <li><div className="chatroom-message-wrapper">{message.content}</div></li>
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