package com.woasis.nettydemo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception{
        new EchoServer(7000).start();//设置端口并启动
    }

    public void start() throws Exception{

        //创建EventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建ServerBootstrap
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)//指定使用Nio的传输channel
                    .localAddress(new InetSocketAddress(port))//设置socke地址所选用的端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {//添加EchoServerHandler到Channel的ChannelPipeline
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind().sync();//绑定服务器
            channelFuture.channel().closeFuture().sync();//关闭channel和块
        }finally {
            group.shutdownGracefully().sync();//关闭EventLoopGroup，释放资源
        }
    }
}
