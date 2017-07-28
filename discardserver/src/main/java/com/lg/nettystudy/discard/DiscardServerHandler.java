package com.lg.nettystudy.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Created by liuguo on 2017/7/28.
 */
public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> {

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        //忽略消息
        //简单打印看看
        ByteBuf in = (ByteBuf) o;
        System.out.println("收到的消息:"+in.toString(CharsetUtil.UTF_8)+",writeIndex:"+in.writerIndex());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
