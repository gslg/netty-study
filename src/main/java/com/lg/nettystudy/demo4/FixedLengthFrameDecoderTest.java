package com.lg.nettystudy.demo4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by liuguo on 2017/7/20.
 */
public class FixedLengthFrameDecoderTest {

    @Test
    public void testFramesDecoded(){
        ByteBuf buf = Unpooled.buffer();

        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        //复制一份
        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        //写入两个直接不满足FixedLengthFrameDecoder中要求的3个byte,所以实际没有写入channel,返回false
        Assert.assertFalse(channel.writeInbound(input.readBytes(2)));

        //上面已经写入了2个byte到ByteBuf中,这里在写入一个byte满足了ByteBuf中有3个可读byte,因此写入
        Assert.assertTrue(channel.writeInbound(input.readBytes(1)));

        //channel.finish() Mark this Channel as finished. Any further try to write data to it will fail
        Assert.assertTrue(channel.finish());

       // channel.writeInbound(input.readBytes(2));

        ByteBuf read = channel.readInbound();

        ByteBuf slice = buf.readSlice(3);
        Assert.assertEquals(slice,read);

        while (read.isReadable()){
            System.out.println(read.readByte());
        }
        read.release();

        read = channel.readInbound();
        Assert.assertNull(read);
    }
}
