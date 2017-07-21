package com.lg.nettystudy.websocket;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by liuguo on 2017/7/21.
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String wsUri;

    private final static File INDEX;

    static {
        URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();

        try {
           String path = location.toURI()+"index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if(wsUri.equalsIgnoreCase(request.uri())){
            //如果请求是一次升级了的 WebSocket 请求，则递增引用计数器（retain）并且将它传递给在 ChannelPipeline 中的下个 ChannelInboundHandler
            ctx.fireChannelRead(request.retain());
        }else {
            if(HttpUtil.is100ContinueExpected(request)){
                // Returns true if and only if the specified message contains the "Expect: 100-continue" header.
                //指定消息头中当且仅当含有"Expect: 100-continue"头信息时,处理符合 HTTP 1.1的 "100 Continue" 请求
                send100Continue(ctx);
            }

            //读取index.html
            RandomAccessFile file = new RandomAccessFile(INDEX,"r");

            //在 头被设置后，写一个 HttpResponse 返回给客户端。
            // 注意，这不是 FullHttpResponse，这只是响应的第一部分。另外，这里我们也不使用 writeAndFlush()， 这个是在留在最后完成。
            HttpResponse response = new DefaultHttpResponse(request.protocolVersion(),HttpResponseStatus.OK);
            response.headers().set(
                    HttpHeaderNames.CONTENT_TYPE,"text/html; charset=UTF-8"
            );

            //判断 keepalive 是否在请求头里面
            boolean keepAlive = HttpUtil.isKeepAlive(request);
            if(keepAlive){
                response.headers().set(
                        HttpHeaderNames.CONTENT_LENGTH,file.length()
                );
                response.headers().set(
                        HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE
                );
            }
            //写 HttpResponse 到客户端
            ctx.write(response);

            //写 index.html 到客户端，根据 ChannelPipeline 中是否有 SslHandler 来决定使用 DefaultFileRegion 还是 ChunkedNioFile
            /**
             * 如果传输过程既没有要求加密也没有要求压缩，那么把 index.html 的内容存储在一个 DefaultFileRegion 里就可以达到最好的效率。
             * 这将利用零拷贝来执行传输。出于这个原因，我们要检查 ChannelPipeline 中是否有一个 SslHandler。如果是的话，我们就使用 ChunkedNioFile。
             */
            if(ctx.pipeline().get(SslHandler.class) == null){
                ctx.write(new DefaultFileRegion(file.getChannel(),0,file.length()));
            }else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }

            //写并刷新 LastHttpContent 到客户端，标记响应完成
            //调用 writeAndFlush() 来刷新所有以前写的信息
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if(!keepAlive){
                //如果 请求头中不包含 keepalive，当写完成时，关闭 Channel
                future.addListener(ChannelFutureListener.CLOSE);
            }

        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        //如果客户端发送的 HTTP 1.1 头是“Expect: 100-continue” ，则发送“100 Continue”的响应。
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
