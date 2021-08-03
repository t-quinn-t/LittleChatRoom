import React, {useEffect, useState} from 'react'
import {Nav, Container, Row, Col} from 'react-bootstrap'
import SockJsClient from 'react-stomp'

function ChatRoom(props) {

    /* ===== ===== ===== chat room list state ===== ===== ===== */
    const [chatRoomList, setChatRoomList] = useState([]);
       /* stub */
    useEffect(() => {
        setChatRoomList(orig => [...orig, "chatroom1", "chatroom2", "adminRoom"]);
    }, []);

    /* ===== ===== ===== Websocket Config ===== ===== ===== */
    const websocketConfig = {
        socketUrl: "localhost://8080/websocket",
        topicsUrl: "/topic/" + props.roomId
    }

    return (
        <div className="chatroom-container">
            <SockJsClient url={websocketConfig.socketUrl}
                          topics={websocketConfig.topicsUrl}
                          onMessage={(message) => {console.log(message)}}
                          ref={ (client) => { this.clientRef = client }}
            />
            <Container fluid>
                <Row>
                    <Col md={3}>
                        <Nav defaultActiveKey="/home" className="flex-column">
                            {chatRoomList.map(chatroom => {
                                return <Nav.Link eventKey={chatroom}>{chatroom}</Nav.Link>
                    })}
                        </Nav>
                    </Col>
                    <Col md={9}>dd</Col>
                </Row>
            </Container>
        </div>
    )
}

export default ChatRoom;