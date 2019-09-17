package com.fyj.netty.exp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

//连接成功之后创建初始化器
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //创建一个收集http请求的pipeline
        ChannelPipeline pipeline = ch.pipeline();
        //接下来我们要创建的是pipeline的拦截器，不同的拦截器有不同的功能
        //HttpServerCodec是netty的控制器，我们也可以创建自己的控制器
//        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        //二进制解码器转换成信息
        pipeline.addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0,4));
        //编码器
        pipeline.addLast("LengthFieldPrepender",new LengthFieldPrepender(4));
        //定义一下使用的编码的解码格式
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        //定义一下使用的编码的编码格式
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        //添加我们刚才自己的控制器TestHttpServerHandler
        pipeline.addLast("testHttpServerHandler", new TestHttpServerHandler());

    }





}
