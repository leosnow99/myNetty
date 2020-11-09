package com.leo.last.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

//初始化器， channel注册后，会执行里面的初始化方法
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //通过channel获取对应的管道
        final ChannelPipeline pipeline = socketChannel.pipeline();

        //通过管道添加handler
        //HttpServerCodec是由netty自己提供的助手类，可以理解为拦截器
        pipeline.addLast("", new HttpServerCodec());

        pipeline.addLast("customerHandler", new CustomerHandler());

    }
}
