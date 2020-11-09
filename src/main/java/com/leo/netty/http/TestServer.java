package com.leo.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {
	public static void main(String[] args) {
		final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		final NioEventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			final ServerBootstrap server = new ServerBootstrap();
			
			server.group(bossGroup, workGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new TestServerInitializer());
			final ChannelFuture channelFuture = server.bind(6670);
			System.out.println("启动成功");
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
