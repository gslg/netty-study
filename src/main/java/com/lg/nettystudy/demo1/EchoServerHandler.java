package com.lg.nettystudy.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Created by liuguo on 2017/7/14.
 */
@Sharable //标识该类的实例可以在channel之间共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 接收到客户端的消息后调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;//接收到的消息

        System.out.println("服务器接收到消息:"+in.toString(CharsetUtil.UTF_8));//将接受到的消息打印到控制台



        ctx.write(in);//将消息写入到channel中,等待返回给client.此时还没有冲刷数据.
    }

    /**
     * 所有消息处理完毕后调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)//冲刷所有消息到远程客户端
                .addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if(channelFuture.isSuccess()){
                            System.out.println("write success");
                        }else {
                            System.out.println("write error");
                            channelFuture.cause().printStackTrace();
                        }
                    }
                })
                .addListener(ChannelFutureListener.CLOSE);//关闭通道后操作完成
    }

    /**
     * 发生异常时的处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close(); //捕获到异常时关闭通道.
    }
}
