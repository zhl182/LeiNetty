package com.zhl.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class UDPServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        //读取数据
        ByteBuf byteBuf = datagramPacket.content();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        System.out.println("收到客户端消息:" + new String(bytes));
        String test = "server回复";
        byte[] resBytes = test.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(Unpooled.copiedBuffer(resBytes), datagramPacket.sender());
        channelHandlerContext.writeAndFlush(sendPacket);
    }
}
