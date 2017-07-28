package com.lg.nettystudy.factorial;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.math.BigInteger;

/**
 * Encodes a {@link Number} into the binary representation prepended with
 * a magic number ('F' or 0x46) and a 32-bit length prefix.  For example, 42
 * will be encoded to { 'F', 0, 0, 0, 1, 42 }.
 *
 * 编码一个Number类型的值到二进制,具体规则是一个魔法number 'F'或者 0x46 和一个32位长度前缀.
 * 例如42挥别编码为 {'F',0,0,0,1,42}
 */
public class NumberEncoder extends MessageToByteEncoder<Number> {
    protected void encode(ChannelHandlerContext channelHandlerContext, Number in, ByteBuf out) throws Exception {
        BigInteger b = null;
        if(in instanceof BigInteger){
            b = (BigInteger) in;
        }else {
            b = new BigInteger(String.valueOf(in));
        }

        byte[] bytes = b.toByteArray();
        int length = bytes.length;


        out.writeByte((byte)'F');
        out.writeInt(length);
        out.writeBytes(bytes);
    }
}
