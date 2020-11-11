package com.leo.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
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
							//加入http解码器
							pipeline.addLast(new HttpServerCodec());
							//以块方式写, 添加ChunkedWriterHandler处理器
							pipeline.addLast(new ChunkedWriteHandler());
							//http数据在传输过程中分段, HttpObjectAggregator, 就是可以将多个段聚合
							pipeline.addLast(new HttpObjectAggregator(8192));
							//对应websocket. websocket的数据以帧(frame)形式传递
							//核心功能: 将http协议升级为ws协议, 保持长连接
							pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
							pipeline.addLast("myHandle", new MyTextWebSocketFrameHandler());
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
