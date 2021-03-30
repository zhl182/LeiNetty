package com.zhl.netty.server.config;

import com.zhl.netty.server.LeiNettyUDPServer;
import com.zhl.netty.server.handler.UDPServerHandler;
import com.zhl.netty.server.properties.UDPServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

@Configuration
@EnableConfigurationProperties({UDPServerProperties.class})
@ConditionalOnProperty(value = {"lei.netty.udp.enabled"},matchIfMissing = true)
public class NettyUDPConfig {
    @Bean
    public LeiNettyUDPServer leiNettyUDPServer(){
        return new LeiNettyUDPServer();
    }
}
