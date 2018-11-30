package com.woasis.nettydemo.java;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class PlainOioServer {

    private final int port;

    public PlainOioServer(int port) {
        this.port = port;
    }

    public void server() throws  Exception{

        final ServerSocket serverSocket = new ServerSocket(port);

        try {
            for (;;){
                final Socket clientSocket = serverSocket.accept();
                System.out.println("Accept conneted form "+clientSocket);
                new Thread(
                        new Runnable() {
                            public void run() {
                                try {
                                    OutputStream outputStream = clientSocket.getOutputStream();
                                    outputStream.write("Hi I am a client\r\n".getBytes(Charset.forName("UTF-8")));
                                    outputStream.flush();
                                    clientSocket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    try {
                                        clientSocket.close();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }
                ).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new PlainOioServer(7700).server();
    }

}
