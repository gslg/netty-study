package com.lg.nettystudy.demo3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by liuguo on 2017/7/19.
 */
public class TimeDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() <4){
            return;
        }
        //out.add(in.readBytes(4))
        out.add(new UnixTime(in.readUnsignedInt()));
    }
}
