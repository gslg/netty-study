package com.lg.nettystudy.websocket;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * Created by liuguo on 2017/7/21.
 */
public class SecureChatServerIntializer extends ChatServerInitializer {

    private SslContext context;

    public SecureChatServerIntializer(ChannelGroup group,SslContext context) {
        super(group);
        this.context = context;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        super.initChannel(channel);
        SSLEngine sslEngine = context.newEngine(channel.alloc());

        sslEngine.setUseClientMode(false);
        channel.pipeline().addFirst(new SslHandler(sslEngine));
    }
}
