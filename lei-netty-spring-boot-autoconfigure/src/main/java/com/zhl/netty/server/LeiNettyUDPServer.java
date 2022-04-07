package com.zhl.netty.server;

import com.zhl.netty.server.handler.UDPServerHandler;
import com.zhl.netty.server.properties.UDPServerProperties;
import com.zhl.netty.server.thread.factory.DaemonThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;

public class LeiNettyUDPServer implements InitializingBean, BeanFactoryAware {

    @Autowired
    private UDPServerProperties udpServerProperties;

    private BeanFactory beanFactory;

    @Override
    public void afterPropertiesSet(){
        ExecutorService executorService = new ThreadPoolExecutor(1,1,10,
                TimeUnit.SECONDS,new SynchronousQueue<>(), DaemonThreadFactory::newThread);
        executorService.execute(this::nettyUdpStart);
        executorService.shutdown();
    }

    public void nettyUdpStart(){
        EventLoopGroup group = new NioEventLoopGroup(1);
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)//指定传输数据包,支持UDP
                    .option(ChannelOption.SO_BROADCAST,udpServerProperties.isUseBroadcast())
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // 线程池复用缓冲区
                    .handler(beanFactory.containsBean("udpServerHandler")
                            ?beanFactory.getBean("udpServerHandler",ChannelHandler.class)
                            :new UDPServerHandler());
            ChannelFuture channelFuturel = bootstrap.bind(udpServerProperties.getPort()).sync();
            channelFuturel.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(group::shutdownGracefully));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
