package com.zhl.netty.server;


import com.zhl.netty.server.properties.WebSocketServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;

public class LeiNettyWebSocketServer implements InitializingBean, BeanFactoryAware {
    @Autowired
    private WebSocketServerProperties serverProperties;

    private BeanFactory beanFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(this::nettyStart);
        thread.setDaemon(true);
        thread.start();
    }
    public void nettyStart(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(serverProperties.getBossCount());
        EventLoopGroup workGroup = new NioEventLoopGroup(serverProperties.getWorkCount());
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            //http????????????
                            channelPipeline.addLast(new HttpServerCodec());
                            //??????????????????????????????
                            channelPipeline.addLast(new ChunkedWriteHandler());
                            //HttpObjectAggregator????????????????????????????????????,????????????????????????????????????
                            channelPipeline.addLast(new HttpObjectAggregator(serverProperties.getAggregatorMaxContentLength()));
                            //websocket???????????????
                            channelPipeline.addLast(new WebSocketServerProtocolHandler(serverProperties.getWsContextPath()));
                            channelPipeline.addLast(beanFactory.getBean("simpleTextWebSocketFrameHandler",ChannelHandler.class));
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap
                    .bind(new InetSocketAddress(serverProperties.getHost(),serverProperties.getPort()))
                    .sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
