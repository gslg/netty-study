package com.lg.nettystudy.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * Created by liuguo on 2017/7/28.
 */
public class DiscardServer {
    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "8009"));

    public static void main(String[] args) throws Exception {
        final SslContext sslContext;
        if(SSL){
            SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();

            sslContext = SslContextBuilder.forServer(selfSignedCertificate.certificate(),
                    selfSignedCertificate.privateKey()).build();
        }else {
            sslContext = null;
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workGroup)
             .channel(NioServerSocketChannel.class)
              .childHandler(new ChannelInitializer<SocketChannel>() {
                  protected void initChannel(SocketChannel serverSocketChannel) throws Exception {
                      ChannelPipeline pipeline = serverSocketChannel.pipeline();
                      if(sslContext != null){
                          pipeline.addLast(sslContext.newHandler(serverSocketChannel.alloc()));
                      }
                      pipeline.addLast(new DiscardServerHandler());
                  }
              });

            ChannelFuture f = b.bind(PORT).sync();
            f.channel().closeFuture().sync();

        }finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
