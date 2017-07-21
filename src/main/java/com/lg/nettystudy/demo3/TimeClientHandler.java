package com.lg.nettystudy.demo3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by liuguo on 2017/7/19.
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf buf;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        UnixTime unixTime = (UnixTime) msg;
        System.out.println(unixTime);
        ctx.close();
       /* ByteBuf in = (ByteBuf) msg;
        buf.writeBytes(in);
        in.release();

        try {
            if(buf.readableBytes() >= 4){
                long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss");
                System.out.println(simpleDateFormat.format(new Date(currentTimeMillis)));
                ctx.close();
            }
        } finally {
           // buf.release();
        }*/
    }

  /*  @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        buf = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        buf.release();
        buf = null;
    }
*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
