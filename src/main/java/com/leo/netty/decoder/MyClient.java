package com.leo.netty.decoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClient {
	public static void main(String[] args) throws InterruptedException {
		final NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
		try {
			final Bootstrap client = new io.netty.bootstrap.Bootstrap();
			client.group(eventExecutors)
					.channel(NioSocketChannel.class)
					.handler(new MyClientInitializer());
			
			final ChannelFuture future = client.connect("127.0.0.1", 7000).sync();
			future.channel().closeFuture().sync();
		} finally {
			eventExecutors.shutdownGracefully();
		}
		
	}
}
