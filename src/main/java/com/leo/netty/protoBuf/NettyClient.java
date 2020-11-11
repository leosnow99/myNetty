package com.leo.netty.protoBuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class NettyClient {
	public static void main(String[] args) throws InterruptedException {
		//客户端只需要一个事件循环组
		final NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
		
		try {
			//创建客户端启动对象
			final Bootstrap bootstrap = new Bootstrap();
			
			//设置相关参数
			bootstrap.group(eventExecutors)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast("encoder", new ProtobufEncoder());
							socketChannel.pipeline().addLast(new NettyClientHandler());
						}
					});
			System.out.println("客户端 ok...");
			
			//启动客户端连接服务器端
			bootstrap.connect("127.0.0.1", 6668).sync();
		} finally {
			eventExecutors.shutdownGracefully();
			
		}
		
	}
}
