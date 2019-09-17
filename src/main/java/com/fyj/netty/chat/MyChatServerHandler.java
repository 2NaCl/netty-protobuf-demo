package com.fyj.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;

//实现一对多的互相通信
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    //保存channel对象
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //（GlobalEventExecutor是一个单线程单例的类）

    @Override
    //当服务端收到客户端的信息触发回调函数
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        //意思很简单，如果不用循环遍历+if else就是自己给别人发消息，消息也会在自己这里出现，而我们想实现，单纯的消息点对点推送
        //这里如果判断是自己发的消息，确实也写了不同于别人发给自己的逻辑。
        channels.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush(channel.remoteAddress() + "发送的消息：" + msg + "\n");
            } else {
                ch.writeAndFlush("【自己】" + msg + "\n");
            }
        });
    }

    @Override
    //cs连接建立触发回调函数
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //因为每次cs建立都会创建channel，如果想要实现客户端之间的通信，就需要将每一个channel保存在一起使用
        //所以我们需要创建channelGroup
        Channel channel = ctx.channel();
        channels.writeAndFlush("【服务器】 - "+channel.remoteAddress()+"加入\n");
        channels.add(channel);
    }

    @Override
    //用户事件的发生触发回调函数
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {//如果事件是一个空闲事件的话
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;

            }
            System.out.println(ctx.channel().remoteAddress()+"超时事件："+eventType);
        }
    }

    @Override
    //若连接处于活动状态，则立刻触发回调函数
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"上线");
    }

    @Override
    //掉线触发回调函数
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"下线");
    }

    @Override
    //连接中断时候触发的回调函数
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.writeAndFlush("【服务器】 - " + channel.remoteAddress() + "下线\n");
        System.out.println(channels.size());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
