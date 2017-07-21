package com.lg.nettystudy.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Created by liuguo on 2017/7/14.
 */
@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    /**
     * 与服务器建立连接后的调用.在建立连接后该方法会被调用一次.
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    /**
     * 处理从服务器接收的消息.
     * 注意，由服务器所发送的消息可以以块的形式被接收。即，当服务器发送 5 个字节是不是保证所有的 5 个字节会立刻收到 -
     * 即使是只有 5 个字节，channelRead0() 方法可被调用两次，第一次用一个ByteBuf（Netty的字节容器）装载3个字节和第二次一个 ByteBuf 装载 2 个字节。
     * 唯一要保证的是，该字节将按照它们发送的顺序分别被接收。 （注意，这是真实的，只有面向流的协议如TCP）
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("客户端收到消息:"+byteBuf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 异常捕获后的处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
