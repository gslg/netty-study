package com.lg.nettystudy.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

/**
 * Created by liuguo on 2017/7/21.
 */
public class SecureChatServer extends ChatServer{

    private final SslContext sslContext;

    public SecureChatServer(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) {
        return new SecureChatServerIntializer(group,sslContext);
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if(args.length > 0){
            port = new Integer(args[0]);
        }

        SelfSignedCertificate cert = new SelfSignedCertificate();
        //SslContext context = SslContext.newServerContext(cert.certificate(),cert.privateKey());
       SslContext context = SslContextBuilder.forServer(cert.certificate(),cert.privateKey()).build();

        final SecureChatServer endpoint = new SecureChatServer(context);

        ChannelFuture future = endpoint.start(new InetSocketAddress(port));

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                endpoint.destroy();
            }
        });
        future.channel().closeFuture().syncUninterruptibly();
    }

}
