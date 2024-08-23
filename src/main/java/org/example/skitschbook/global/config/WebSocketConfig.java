package org.example.skitschbook.global.config;

import lombok.RequiredArgsConstructor;
import org.example.skitschbook.global.handler.SkitscheWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final SkitscheWebSocketHandler skitscheWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(skitscheWebSocketHandler, "/ws/skitsche")
                .setAllowedOrigins("*");
    }
}
