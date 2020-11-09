package com.leo.netty.groupChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatServer {
	private final int port;
	
	public GroupChatServer(int port) {
		this.port = port;
	}
	
	//处理客户端请求
	public void run() {
		//创建EventLoopGroup
		final EventLoopGroup bossGroup = new NioEventLoopGroup();
		final EventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			final ServerBootstrap server = new ServerBootstrap();
			
			server.group(bossGroup, workGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 128)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) {
							final ChannelPipeline pipeline = ch.pipeline();
							//向pipeline加入解码器
							pipeline.addLast("decoder", new StringDecoder());
							//向pipeline加入编码器
							pipeline.addLast("encoder", new StringEncoder());
							//向pipeline加入自己handler
							pipeline.addLast("myHandler", new GroupChatServerHandler());
						}
					});
			System.out.println("启动成功");
			final ChannelFuture channelFuture = server.bind(port).sync();
			
			//监听关闭
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		final GroupChatServer groupChatServer = new GroupChatServer(7000);
		groupChatServer.run();
	}
}
