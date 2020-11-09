package com.leo.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		//相管道加入处理器
		
		//得到管道
		final ChannelPipeline pipeline = channel.pipeline();
		
		//加入一个netty 提供httpServerCodec
		pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
		pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHand());
	}
}
