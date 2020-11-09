package com.leo.netty.heartBeat;

import com.leo.netty.groupChat.GroupChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {
	public static void main(String[] args) {
		//创建EventLoopGroup
		final EventLoopGroup bossGroup = new NioEventLoopGroup();
		final EventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			final ServerBootstrap server = new ServerBootstrap();
			
			server.group(bossGroup, workGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) {
							final ChannelPipeline pipeline = ch.pipeline();
							//加入netty提供的IdleStateHandler
							pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
							pipeline.addLast(new MyServerHandler());
						}
					});
			System.out.println("启动成功");
			final ChannelFuture channelFuture = server.bind(7000).sync();
			
			//监听关闭
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
