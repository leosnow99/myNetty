package com.leo.netty.groupChat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient {
	private final String HOST;
	private final int PORT;
	
	public GroupChatClient(String HOST, int PORT) {
		this.HOST = HOST;
		this.PORT = PORT;
	}
	
	public void run() {
		final NioEventLoopGroup group = new NioEventLoopGroup();
		
		try {
			final Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) {
							final ChannelPipeline pipeline = ch.pipeline();
							//向pipeline加入解码器
							pipeline.addLast("decoder", new StringDecoder());
							//向pipeline加入编码器
							pipeline.addLast("encoder", new StringEncoder());
							//向pipeline加入自己handler
							pipeline.addLast("myHandler", new GroupChatClientHandler());
						}
					});
			final ChannelFuture channelFuture = bootstrap.connect(HOST, PORT).sync();
			final Channel channel = channelFuture.channel();
			System.out.println("-----" + channel.localAddress() + "---");
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNextLine()) {
				String msg = scanner.nextLine();
				//通过channel 发送到服务器端
				channel.writeAndFlush(msg + "\r\n");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	
	public static void main(String[] args) {
		final GroupChatClient client = new GroupChatClient("127.0.0.1", 7000);
		client.run();
	}
}
