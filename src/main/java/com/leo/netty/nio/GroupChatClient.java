package com.leo.netty.nio;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
	//定义相关属性
	private final String HOST = "218.198.180.33";
	private final int PORT = 6667;
	private SocketChannel socketChannel;
	private Selector selector;
	private String username;
	
	//构造器
	public GroupChatClient() {
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);
			username = String.valueOf(socketChannel.getLocalAddress());
			System.out.println(username + "初始化成功!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendInfo(String message) {
		message = username + " 说: " + message;
		try {
			socketChannel.write(ByteBuffer.wrap(message.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readInfo() {
		try {
			final int count = selector.select();
			if (count > 0) {
				final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				while (iterator.hasNext()) {
					final SelectionKey key = iterator.next();
					if (key.isReadable()) {
						final SocketChannel channel = (SocketChannel) key.channel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						final int read = channel.read(buffer);
						if (read > 0) {
							System.out.println("收到服务器消息: " + new String(buffer.array()));
						}
					}
//					iterator.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//启动客户端
		final GroupChatClient client = new GroupChatClient();
		
		//启动一个线程, 读取从服务器发送的消息
		new Thread(() -> {
			while (true) {
				client.readInfo();
				try {
					Thread.currentThread().sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		//发送给服务器消息
		final Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()){
			client.sendInfo(scanner.nextLine());
		}
	}
}
