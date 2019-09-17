package com.fyj.netty.exp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.beans.Transient;
import java.net.URI;
import java.util.UUID;

public class TestHttpServerHandler extends SimpleChannelInboundHandler<String> {
    //Inbound是对于进来的请求的处理，OutBound是对于return返回的处理但是底层都是Adapter实现的，和Servlet一样
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //ChannelHandlerContext是上下文，msg是client发过来的信息
        //输出远程客户端的地址和消息
        System.out.println(ctx.channel().remoteAddress() + "," + msg);
        ctx.channel().writeAndFlush("from server:" + UUID.randomUUID());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        //遇见问题就打出错误然后关闭连接
    }




























    //        if (msg instanceof HttpRequest) {
//
//            HttpRequest httpRequest = (HttpRequest) msg;
//            System.out.println("请求方法名：" + httpRequest.method().name());
//            URI uri = new URI(httpRequest.uri());
//            if ("/favicon.ico".equals(uri.getPath())) {
//                System.out.println("请求favicon.ico");
//            }
//
//            //创建自己的channel处理器
//            ByteBuf content = Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8);
//            //构造netty内部的相应，将我们的content信息注入
//            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,content);
//            //添加头信息，去响应
//            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
//
//            ctx.writeAndFlush(response);
//        }
//}





}
