package com.lg.nettystudy.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * Created by liuguo on 2017/7/21.
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    /**
     * 覆写userEventTriggered() 方法来处理自定义事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE){
            //如果接收的事件表明握手成功,就从 ChannelPipeline 中删除HttpRequestHandler ，因为接下来不会接受 HTTP 消息了
            ctx.pipeline().remove(HttpRequestHandler.class);
            //写一条消息给所有的已连接 WebSocket 客户端，通知它们建立了一个新的 Channel 连接
            group.writeAndFlush(new TextWebSocketFrame(
                    "Client " + ctx.channel() + " joined"));
            //添加新连接的 WebSocket Channel 到 ChannelGroup 中，这样它就能收到所有的信息
            group.add(ctx.channel());
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
        //保留收到的消息，并通过 writeAndFlush() 传递给所有连接的客户端。
        /**
         * 如果接收到 TextWebSocketFrame，调用 retain() ，并将其写、刷新到 ChannelGroup，使所有连接的 WebSocket Channel 都能接收到它。
         * 和以前一样，retain() 是必需的，因为当 channelRead0（）返回时，TextWebSocketFrame 的引用计数将递减。
         * 由于所有操作都是异步的，writeAndFlush() 可能会在以后完成，我们不希望它访问无效的引用。
         */
        group.writeAndFlush(msg.retain());
    }
}
