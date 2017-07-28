package com.lg.test.factorial;

import com.lg.nettystudy.factorial.BigIntegerDecoder;
import com.lg.nettystudy.factorial.NumberEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

/**
 * Created by liuguo on 2017/7/28.
 */
public class TestFactorial {

    @Test
    public void testNumberEncoder(){

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new NumberEncoder());

        embeddedChannel.writeOutbound(42);

        ByteBuf byteBuf = embeddedChannel.readOutbound();

        Assert.assertEquals('F',byteBuf.readByte());

        Assert.assertEquals(1,byteBuf.readInt());

        Assert.assertEquals(42,byteBuf.readByte());
    }

    @Test
    public void testBigIntegerDecoder(){
        ByteBuf b = Unpooled.buffer();

        b.writeByte('F');
        b.writeInt(1);
        b.writeByte(42);

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new BigIntegerDecoder());

        embeddedChannel.writeInbound(b);

        BigInteger a = embeddedChannel.readInbound();

        Assert.assertEquals(a.intValue(),42);
    }

    @Test
    public void testDecoderAndEncoder(){

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new NumberEncoder());

        embeddedChannel.writeOutbound(42);

        ByteBuf byteBuf = embeddedChannel.readOutbound();

        embeddedChannel = new EmbeddedChannel(new BigIntegerDecoder());
        embeddedChannel.writeInbound(byteBuf);

        BigInteger a = embeddedChannel.readInbound();

        Assert.assertEquals(a.intValue(),42);
    }
}
