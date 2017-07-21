package com.lg.nettystudy.demo2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Created by liuguo on 2017/7/17.
 */
public class ByteBufTest {

    @Test
    public void testSlice(){
        Charset utf8 = Charset.forName("UTF-8");

        ByteBuf buf = Unpooled.copiedBuffer("Hello World!",utf8);

        ByteBuf buf2 = buf.slice(0,5);

        System.out.println(buf2.toString(utf8));

        buf2.setByte(0,'J');

        System.out.println("buf="+buf.toString(utf8));
        System.out.println("buf2="+buf2.toString(utf8));

        assert buf2.getByte(0) == buf.getByte(0);
    }

    @Test
    public void testCopy(){
        Charset utf8 = Charset.forName("UTF-8");

        ByteBuf buf = Unpooled.copiedBuffer("Hello World!",utf8);

        ByteBuf buf2 = buf.copy(0,5);

        System.out.println(buf2.toString(utf8));

        buf2.setByte(0,'J');

        System.out.println("buf="+buf.toString(utf8));
        System.out.println("buf2="+buf2.toString(utf8));
        assert buf2.getByte(0) != buf.getByte(0);
    }

    @Test
    public void testGetAndSet(){
        Charset utf8 = Charset.forName("UTF-8");

        ByteBuf buf = Unpooled.copiedBuffer("Hello World!",utf8);

        System.out.println((char) buf.getByte(0));

        int readIndex = buf.readerIndex();
        int writeIndex = buf.writerIndex();

        System.out.println("readIndex="+readIndex);
        System.out.println("writeIndex="+writeIndex);

        buf.setByte(0,'G');

        System.out.println((char)buf.getByte(0));

        assert readIndex == buf.readerIndex();
        assert writeIndex == buf.writerIndex();
    }

    @Test
    public void testReadAndWrite(){
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Hello World!",utf8);

        int readIndex = buf.readerIndex();
        System.out.println("readIndex="+readIndex);
        int writeIndex = buf.writerIndex();
        System.out.println("writeIndex="+writeIndex);

        System.out.println((char) buf.readByte());
        readIndex = buf.readerIndex();
        System.out.println("readIndex2="+readIndex);
        writeIndex = buf.writerIndex();
        System.out.println("writeIndex2="+writeIndex);

        buf.writeByte('?');

        readIndex = buf.readerIndex();
        System.out.println("readIndex3="+readIndex);
        writeIndex = buf.writerIndex();
        System.out.println("writeIndex3="+writeIndex);

        System.out.println("buf="+buf.toString(utf8));
    }
}
