package com.zhl.netty.server;


import com.zhl.netty.server.handler.TextWebSocketFrameHandler;
import com.zhl.netty.server.properties.ServerProperties;
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

public class LeiNettyServer implements InitializingBean, BeanFactoryAware {
    @Autowired
    private ServerProperties serverProperties;

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
                            //http解编码器
                            channelPipeline.addLast(new HttpServerCodec());
                            //块的方式来写的处理器
                            channelPipeline.addLast(new ChunkedWriteHandler());
                            //HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
                            channelPipeline.addLast(new HttpObjectAggregator(serverProperties.getAggregatorMaxContentLength()));
                            //websocket的访问路径
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
        } finally{
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
