package com.zhl.netty.server.config;

import com.zhl.netty.server.LeiNettyServer;
import com.zhl.netty.server.handler.TextWebSocketFrameHandler;
import com.zhl.netty.server.properties.ServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@EnableConfigurationProperties({ServerProperties.class})
public class NettyConfig {
    @Bean
    public LeiNettyServer leiNettyServer(){
        return new LeiNettyServer();
    }
    @Bean("simpleTextWebSocketFrameHandler")
    @Scope("prototype")
    @ConditionalOnMissingBean(name={"simpleTextWebSocketFrameHandler"})
    public TextWebSocketFrameHandler textWebSocketFrameHandler(){
        return new TextWebSocketFrameHandler();
    }
}
