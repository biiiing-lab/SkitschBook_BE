package org.example.skitschbook.global.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
@RequiredArgsConstructor
public class RedisSubscriberHandler implements MessageListener {

    private SkitscheWebSocketHandler webSocketHandler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String canvasData = new String(message.getBody());
        try {
            webSocketHandler.broadcastMessage(new TextMessage(canvasData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
