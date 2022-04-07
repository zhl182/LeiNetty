package com.zhl.netty.server.config;

import com.zhl.netty.server.LeiNettyWebSocketServer;
import com.zhl.netty.server.handler.TextWebSocketFrameHandler;
import com.zhl.netty.server.properties.WebSocketServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@EnableConfigurationProperties({WebSocketServerProperties.class})
@ConditionalOnProperty(value = {"lei.netty.websocket.enabled"},matchIfMissing = true)
public class NettyWebSocketConfig {
    @Bean
    public LeiNettyWebSocketServer leiNettyServer(){
        return new LeiNettyWebSocketServer();
    }
    //websocekt每个socketChannel的handler不应该共享
    @Bean("simpleTextWebSocketFrameHandler")
    @Scope("prototype")
    @ConditionalOnMissingBean(name={"simpleTextWebSocketFrameHandler"})
    public TextWebSocketFrameHandler textWebSocketFrameHandler(){
        return new TextWebSocketFrameHandler();
    }
}
