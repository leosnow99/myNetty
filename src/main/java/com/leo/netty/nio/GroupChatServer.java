package com.leo.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
	private Selector selector;
	private ServerSocketChannel listenChannel;
	private static final int PORT = 6667;
	
	//构造器
	public GroupChatServer() {
		try {
			//得到选择器
			selector = Selector.open();
			//获取ServerSocketChannel
			listenChannel = ServerSocketChannel.open();
			//绑定端口
			listenChannel.socket().bind(new InetSocketAddress(PORT));
			//设置非阻塞模式
			listenChannel.configureBlocking(false);
			//将listenChannel注册到Selector
			listenChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//监听
	public void listen() {
		try {
			//循环处理
			while (true) {
				final int count = selector.select(2000);
				if (count > 0) {
					//遍历selectionKey集合
					final Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
					while (keyIterator.hasNext()) {
						//取出selectionKey
						final SelectionKey selectionKey = keyIterator.next();
						//监听到accept
						if (selectionKey.isAcceptable()) {
							final SocketChannel channel = listenChannel.accept();
							channel.configureBlocking(false);
							//将channel注册到selector
							channel.register(selector, SelectionKey.OP_READ);
							//提示
							System.out.println(channel.getRemoteAddress() + " 上线 ");
						} else if (selectionKey.isReadable()) {
							//通道发生read事件(通道可读状态)
							readData(selectionKey);
						}
						//移除当前key
						keyIterator.remove();
					}
				} else {
					System.out.println("等待...");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void readData(SelectionKey key) {
		//定义SocketChannel
		SocketChannel channel = null;
		try {
			//取得关联channel
			channel = (SocketChannel) key.channel();
			final ByteBuffer buffer = ByteBuffer.allocate(1024);
			final int count = channel.read(buffer);
			//根据count值处理
			if (count > 0) {
				//将缓冲区数据转换为字符串
				final String message = new String(buffer.array());
				//输出消息
				System.out.println("from 客户端: " + message);
				//向其它客户端转发消息
				sendInfoToOtherClients(message, channel);
			}
		} catch (IOException e) {
			try {
				System.out.println(channel.getRemoteAddress() + ": 离线了!");
				//取消注册
				key.cancel();
				//关闭通到
				channel.close();
				e.printStackTrace();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	private void sendInfoToOtherClients(String message, SocketChannel self) {
		try {
			System.out.println("服务器转发消息中...");
			for (SelectionKey key : selector.keys()) {
				//通过key取出SocketChannel
				final Channel targetChannel =  key.channel();
				if (targetChannel instanceof SocketChannel && targetChannel != self) {
					((SocketChannel) targetChannel).write(ByteBuffer.wrap(message.getBytes()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new GroupChatServer().listen();
	}
}
