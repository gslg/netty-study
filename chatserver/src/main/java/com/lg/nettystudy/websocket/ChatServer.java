package com.lg.nettystudy.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

/**
 * Created by liuguo on 2017/7/21.
 */
public class ChatServer {

    private final ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture start(InetSocketAddress address){
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(createInitializer(group));
        ChannelFuture future = bootstrap.bind(address);
        future.syncUninterruptibly();
        channel = future.channel();
        return future;
    }

    protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) {
        return new com.lg.nettystudy.websocket.ChatServerInitializer(group);
    }

    public void destroy() {
        if (channel != null) {
            channel.close();
        }
        group.close();
        eventLoopGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception{
        int port = 8080;

        if(args.length > 0){
          port = new Integer(args[0]);
            //System.exit(1);
        }

        final ChatServer endpoint = new ChatServer();

        ChannelFuture future = endpoint.start(new InetSocketAddress(port));

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                endpoint.destroy();
            }
        });

        future.channel().closeFuture().syncUninterruptibly();
    }
}
