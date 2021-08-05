import React, {useEffect, useState} from 'react'
import {Nav, Container, Row, Col, Button} from 'react-bootstrap'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

function ChatRoom(props) {

    /* ===== ===== ===== Websocket Config ===== ===== ===== */
    const websocketConfig = {
        socketUrl: "http://localhost:8080/websocket",
        topicUrl: "/topic/" + props.roomId,
        destinationURL: "/chat/send-message"
    }
    useEffect(() => {
        let socket = new SockJS(websocketConfig.socketUrl)
        let stompClient = Stomp.over(socket)
        stompClient.connect({}, (frame) => {
            console.log("connected" + frame)
            stompClient.subscribe(websocketConfig.topicUrl, () => {
                console.log("message received")
            })
        })
    }, [])

    /* ===== ===== ===== chat room list state ===== ===== ===== */
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
                            console.log("haha")

                        }}/>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export default ChatRoom;