package com.leo.netty.decoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyServer {
	public static void main(String[] args) throws InterruptedException {
		final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		final NioEventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			final ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new MyServerInitializer());
			
			final ChannelFuture future = server.bind(7000).sync();
			future.channel().closeFuture().sync();
			
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
