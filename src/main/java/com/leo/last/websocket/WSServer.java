package com.leo.last.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WSServer {
	public static void main(String[] args) throws InterruptedException {
		final NioEventLoopGroup mainGroup = new NioEventLoopGroup();
		final NioEventLoopGroup childGroup = new NioEventLoopGroup();
		
		try {
			final ServerBootstrap server = new ServerBootstrap();
			server.group(mainGroup, childGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new WSServerInitializer());
			
			final ChannelFuture channelFuture = server.bind(8088).sync();
			//监听关闭的channel， 并且设置为同步方式
			channelFuture.channel().closeFuture().sync();
		} finally {
			mainGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
	}
}
