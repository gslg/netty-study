package com.lg.nettystudy.demo1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by liuguo on 2017/7/14.
 */
public class EchoServer {
    private int port;

    EchoServer(int port){
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        if(args.length < 1){
            System.err.println("Usage: "+EchoServer.class.getSimpleName()+" <port>");
        }

        int port = Integer.parseInt(args[0]);//设置端口

        new EchoServer(port).start();//启动服务
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();//创建EventLoopGroup

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();//创建ServerBootstrap
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioServerSocketChannel.class) //指定nio传输的Channel类型(信道类型)
                    .localAddress(new InetSocketAddress(port)) //指定socket地址使用的端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //添加 EchoServerHandler 到 Channel 的 ChannelPipeline
                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            //绑定的服务器;sync阻塞当前线程等待绑定完成
            ChannelFuture f = bootstrap.bind().sync();
            System.out.println(EchoServer.class.getSimpleName()+" started and listen on"+f.channel().localAddress());
            //阻塞直到服务器关闭channel
            f.channel().closeFuture().sync();
        } finally {
            //关机的 EventLoopGroup，释放所有资源。
            nioEventLoopGroup.shutdownGracefully().sync();
        }

    }
}
