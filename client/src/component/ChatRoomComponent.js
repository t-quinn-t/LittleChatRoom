import React, {useEffect, useState} from 'react'
import {Nav, Container, Row, Col} from 'react-bootstrap'

function ChatRoom() {
    const [chatRoomList, setChatRoomList] = useState([]);
    /* stub */
    useEffect(() => {
        setChatRoomList(orig => [...orig, "chatroom1", "chatroom2", "adminRoom"]);
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
                    <Col md={9}>dd</Col>
                </Row>
            </Container>
        </div>
    )
}

export default ChatRoom;