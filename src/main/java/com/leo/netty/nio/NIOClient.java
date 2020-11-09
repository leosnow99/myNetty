package com.leo.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
	public static void main(String[] args) throws IOException {
		//得到一个网络通道
		final SocketChannel socketChannel = SocketChannel.open();
		//设置非阻塞
		socketChannel.configureBlocking(false);
		//绑定ip和端口
		final InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 7777);
		//连接服务器
		if (!socketChannel.connect(inetSocketAddress)) {
			while (!socketChannel.finishConnect()) {
				System.out.println("因为连接客户端需要时间, 客户端不会阻塞, 可以做其它工作");
			}
		}
		
		String message = "hello, leo";
		final ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
		//发送数据
		socketChannel.write(buffer);
		System.in.read();
	}
}
