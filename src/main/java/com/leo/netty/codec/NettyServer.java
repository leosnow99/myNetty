package com.leo.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class NettyServer {
	public static void main(String[] args) throws InterruptedException {
		//创建BossGroup
		//bossGroup只处理连接请求, 真正和客户端业务处理, 会交给workerGroup
		final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		//创建WorkGroup
		final NioEventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			
			//创建服务器端启动对象, 配置参数
			final ServerBootstrap bootstrap = new ServerBootstrap();
			
			//使用链式编程进行设置
			bootstrap.group(bossGroup, workGroup) //设置两个线程组
					.channel(NioServerSocketChannel.class) //使用NIOSocketChannel作为服务器的通道实现
					.option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到连接个数
					.childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动链接状态
					.childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道测试对象(匿名对象)
						//设置处理器
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast("decoder", new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));
							socketChannel.pipeline().addLast(new NettyServerHandler());
						}
					});//给workerGroup的EventLoop对相应的管道设置处理器
			System.out.println("服务器 is ready");
			//绑定一个端口并且同步, 生成一个ChannelFuture对象
			final ChannelFuture channelFuture = bootstrap.bind(6668).sync();
			
			//对关闭通道进行监听
			channelFuture.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
