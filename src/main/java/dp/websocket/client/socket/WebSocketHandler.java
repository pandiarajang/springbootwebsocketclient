package dp.websocket.client.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static int counter = 1;

    protected Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Lazy
    @Autowired
    private SocketConnector connector;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Connection has been established with websocket server. {}", session);
        session.sendMessage(new TextMessage("Great! I am now connected!"));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("Message received : {} ", message.getPayload());
        if (counter < 2) {
            session.sendMessage(new TextMessage("Message number " + counter));
            counter++;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        if (status.getCode() == 1006 || status.getCode() == 1011 || status.getCode() == 1012 ||  status.getCode() == 1001 ) {
            connector.reconnect();
        }
    }
}
