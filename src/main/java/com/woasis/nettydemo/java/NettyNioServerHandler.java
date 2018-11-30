package com.woasis.nettydemo.java;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Date;

public class NettyNioServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyNioServerHandler-channelRegistered"+new Date());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyNioServerHandler-channelUnregistered"+new Date());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyNioServerHandler-channelActive"+new Date());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyNioServerHandler-channelInactive"+new Date());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("NettyNioServerHandler-channelRead"+new Date());
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("read:"+byteBuf.toString(CharsetUtil.UTF_8));

        ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("I am netty nio server", CharsetUtil.UTF_8));
        ctx.channel().writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE).sync();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyNioServerHandler-channelReadComplete"+new Date());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("NettyNioServerHandler-userEventTriggered"+new Date());
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyNioServerHandler-channelWritabilityChanged"+new Date());
    }
}
