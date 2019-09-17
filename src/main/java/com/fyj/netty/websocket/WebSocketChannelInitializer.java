package com.fyj.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast(new HttpServerCodec());
        channelPipeline.addLast(new ChunkedWriteHandler());//以块的方式去写
        channelPipeline.addLast(new HttpObjectAggregator(8192)); //netty默认给http请求分块处理，这个是将那些分块的去聚合
        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //负责websocket的握手，心跳，http协议就是http：//  websocket协议就是ws：//，只不过这个/ws还是指的域名
        //TODO WebSocketServerProtocolHandler传递数据的形式都是以frame，这里实现的Frame是TextWebSocketFrameHandler
        channelPipeline.addLast(new TextWebSocketFrameHandler());

    }
}
