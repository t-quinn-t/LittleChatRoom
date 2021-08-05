import React, {useEffect, useState} from 'react'
import {Nav, Container, Row, Col, Button} from 'react-bootstrap'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

function ChatRoom(props) {

    /* ===== ===== ===== Websocket ===== ===== ===== */
    const websocketConfig = {
        socketUrl: "http://localhost:8080/websocket",
        roomUrl: "/topic/" + props.roomId,
        echoUrl: "/chat/send-message"
    }
    const [stompClient, setStompClient] = useState(() => {
        let socket = new SockJS(websocketConfig.socketUrl)
        let stompClient = Stomp.over(socket)
        stompClient.connect({}, (frame) => {
            console.log("connected" + frame)
            stompClient.subscribe(websocketConfig.roomUrl, () => {
                console.log("message received")
            })
        });
        return stompClient;
    })

    /* ===== ===== ===== Chatrooms ===== ===== ===== */
    // TODO: close the correct list of chatrooms
    const [chatRoomList, setChatRoomList] = useState([]);
       /* stub */
    useEffect(() => {
        setChatRoomList(orig => ["chatroom1", "chatroom2", "adminRoom"]);
    }, []);

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
                        <Button onClick={() => {
                            stompClient.send(websocketConfig.echoUrl, {}, {
                                'from_user': "default"
                            });
                        }}>
                            Send
                        </Button>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export default ChatRoom;