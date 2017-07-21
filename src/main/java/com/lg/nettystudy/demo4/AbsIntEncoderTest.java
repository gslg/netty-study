package com.lg.nettystudy.demo4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by liuguo on 2017/7/21.
 */
public class AbsIntEncoderTest {

    @Test
    public void testEncoded(){
        ByteBuf buf = Unpooled.buffer();

        for (int i = 0; i < 10; i++) {
            buf.writeInt(i * -1);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        //Write messages to the outbound of this Channel.
        Assert.assertTrue(channel.writeOutbound(buf));

        Assert.assertTrue(channel.finish());

        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(i, (int)channel.readOutbound());
        }

        Assert.assertNull(channel.readOutbound());

    }
}
