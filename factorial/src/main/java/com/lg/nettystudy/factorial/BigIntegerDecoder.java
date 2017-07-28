package com.lg.nettystudy.factorial;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.math.BigInteger;
import java.util.List;

/**
 * Decodes the binary representation of a {@link BigInteger} prepended
 * with a magic number ('F' or 0x46) and a 32-bit integer length prefix into a
 * {@link BigInteger} instance.  For example, { 'F', 0, 0, 0, 1, 42 } will be
 * decoded into new BigInteger("42").
 */
public class BigIntegerDecoder extends ByteToMessageDecoder {

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {

        // Wait until the length prefix is available.
        if(in.readableBytes() < 5){
            return;
        }
        // Marks the current readerIndex in this buffer.
        //You can reposition the current readerIndex to the marked readerIndex by calling resetReaderIndex().
        // The initial value of the marked readerIndex is 0
        in.markReaderIndex();

        // Check the magic number.

        int magicNumber = in.readUnsignedByte();
        if(magicNumber != 'F'){
            //重置readerIndex到标记点
            in.resetReaderIndex();
            throw new CorruptedFrameException("Invalid magic number: " + magicNumber);
        }

        // Wait until the whole data is available.
        int dataLength = in.readInt();

        if(in.readableBytes() < dataLength){
            System.out.println("可读bytes小于整个长度,重置readerIndex");
            in.resetReaderIndex();
            return;
        }

        // Convert the received data into a new BigInteger.
        byte[] decoded = new byte[dataLength];
        in.readBytes(decoded);

        out.add(new BigInteger(decoded));

    }
}
