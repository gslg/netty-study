package com.lg.nettystudy.demo3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Created by liuguo on 2017/7/19.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        try {
            //System.out.println("before read:"+buf.toString(CharsetUtil.UTF_8));
           /* while (buf.isReadable()){
                System.out.println("收到的消息:"+(char)buf.readableBytes());
                System.out.flush();
            }*/
           System.out.println("收到的消息:"+buf.toString(CharsetUtil.UTF_8));
            ctx.writeAndFlush(msg);
        } finally {
            //释放资源,释放资源了如果在写入管道会报错
           // ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
