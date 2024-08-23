package org.example.skitschbook.global.config;

import org.example.skitschbook.global.handler.RedisSubscriberHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisConfig {

    // redis 연결, 채널 및 패턴 메세지 수신, 비동기 처리, 채널 패턴 정의
    @Bean // 메세지 구독 관리
    RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                RedisSubscriberHandler redisSubscriberHandler) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisSubscriberHandler, new PatternTopic("skitsche")); // 특정 주제 구독
        return container;
    }

    @Bean // 서버와 상호작용 하기 위한 고수준의 api 제공, 데이터 CRUD, 댜양한 데이터 구조 지원, 데이터 직렬/역직렬 설정, 트랜잭션 지원
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean // 서버와의 연결을 생성하고 관리하는 인터페이스
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }
}
