package com.woasis.nettydemo.java;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class PlainNioServer {

    private final int port;

    public PlainNioServer(int port) {
        this.port = port;
    }

    public void server() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        serverSocket.bind(address);//绑定服务器到指定端口

        Selector selector = Selector.open();//打开selector处理channel
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        final ByteBuffer byteBuffer = ByteBuffer.wrap("Hi i am nio server\r\n".getBytes());

        for (;;){
            selector.select();

            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                try {
                    if (selectionKey.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel channel = server.accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_WRITE|SelectionKey.OP_READ, byteBuffer.duplicate());
                        System.out.println("accept from client :"+channel);
                    }
                    if (selectionKey.isWritable()){
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        while (buffer.hasRemaining()){
                            if (client.write(buffer) ==0 ){
                                break;
                            }
                        }
                        client.close();
                    }
                }catch (IOException e){
                    selectionKey.cancel();
                    selectionKey.channel().close();
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        PlainNioServer nioServer = new PlainNioServer(7701);
        nioServer.server();
    }
}
