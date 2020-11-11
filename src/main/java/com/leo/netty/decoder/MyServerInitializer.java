package com.leo.netty.decoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		//获取到pipeline
		final ChannelPipeline pipeline = ch.pipeline();
		
		//入站handler解码
		pipeline.addLast(new MyByteToLongDecoder());
		pipeline.addLast(new MyLongToByteEncoder());
		
		pipeline.addLast(new MyServerHandler());
	}
}
