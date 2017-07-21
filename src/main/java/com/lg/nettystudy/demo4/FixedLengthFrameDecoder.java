package com.lg.nettystudy.demo4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by liuguo on 2017/7/20.
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private final int framelength;

    public FixedLengthFrameDecoder(int framelength) {
        if(framelength <= 0){
            throw new IllegalArgumentException("framelength 必须是正整数,实际传入的却是:"+framelength);
        }
        this.framelength = framelength;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() >= framelength){
            out.add(in.readBytes(framelength));
        }
    }
}
