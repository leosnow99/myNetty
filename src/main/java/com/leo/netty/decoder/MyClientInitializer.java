package com.leo.netty.decoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		final ChannelPipeline pipeline = ch.pipeline();
		
		//加入出站handler
		pipeline.addLast(new MyLongToByteEncoder());
		//入站解码器
		pipeline.addLast(new MyByteToLongDecoder());
		//加入自定义handler, 处理业务
		pipeline.addLast(new MyClientHandler());
		
	}
}
