package com.lg.nettystudy.demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by liuguo on 2017/7/14.
 */
public class EchoClient {
    private final String host;
    private final int port;

    EchoClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();//创建bootstrap
            bootstrap.group(eventLoopGroup) //指定EventLoopGroup来处理客户端事件.由于使用nio传输,使用NioEventLoopGroup实现类
                    .channel(NioSocketChannel.class) //指定用于nio传输的channel类型
                    .remoteAddress(new InetSocketAddress(host,port)) //设置服务器的InetSocketAddress
                    .handler(new ChannelInitializer<SocketChannel>() {
                        //当建立一个连接和一个新的通道时，创建添加到 EchoClientHandler 实例 到 channel pipeline
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            //连接到远程,等待连接完成
            ChannelFuture f = bootstrap.connect().sync();

            //阻塞知道channel关闭
            f.channel().closeFuture().sync();
        } finally {
            //调用 shutdownGracefully() 来关闭线程池和释放所有资源
            eventLoopGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(
                    "Usage: " + EchoClient.class.getSimpleName() +
                            " <host> <port>");
            return;
        }

        final String host = args[0];
        final int port = Integer.parseInt(args[1]);

        new EchoClient(host, port).start();
    }

}
