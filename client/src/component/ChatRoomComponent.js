import React, {useEffect, useState} from 'react'
import {Nav, Container, Row, Col, Button, Form} from 'react-bootstrap'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import './style/chatRoomStyle.css'

function ChatRoom(props) {

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

    /* ===== ===== ===== Chatrooms ===== ===== ===== */
    // TODO: close the correct list of chatrooms
    const [chatRoomList, setChatRoomList] = useState([]);
       /* stub */
    useEffect(() => {
        setChatRoomList(orig => ["chatroom1", "chatroom2", "adminRoom"]);
    }, []);

    /* ===== ===== ===== Messaging ===== ===== ===== */
    let [messageSet, setMessageSet] = useState(() => {
        // TODO: get list of init message to display
        return []
    });
    let [currentTypingMessage, setCurrentTypingMessage] = useState("");

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
                            Hello World
                        </div>
                        <Form onSubmit={() => {
                            stompClient.send(websocketConfig.echoUrl, {}, JSON.stringify({
                                'id': -1,
                                'senderId': 1,
                                'roomId': 1,
                                'content': currentTypingMessage
                            }));
                        }}>
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