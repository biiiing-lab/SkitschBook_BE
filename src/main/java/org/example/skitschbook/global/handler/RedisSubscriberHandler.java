package org.example.skitschbook.global.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class RedisSubscriberHandler implements MessageListener {
    private SkitscheWebSocketHandler handler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = new String(message.getBody());
        try {
            handler.broadcastMessage(new TextMessage(payload));
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
