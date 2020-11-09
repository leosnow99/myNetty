package com.leo.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
	public static void main(String[] args) throws IOException {
		//创建ServerSocketChannel
		final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		
		//得到一个Selector对象
		final Selector selector = Selector.open();
		
		//绑定一个端口
		serverSocketChannel.bind(new InetSocketAddress(7777));
		//设置为非阻塞
		serverSocketChannel.configureBlocking(false);
		//serverSocketChannel注册到selector 关心事件
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		//循环等待连接
		while (true) {
			if (selector.select(1000) == 0) {
				//没有事件发生
				System.out.println("服务器等待1s, 无连接");
				continue;
			}
			//事件发生, 获取相关selectionKey集合
			//获取关注事件集合
			final Set<SelectionKey> selectionKeys = selector.selectedKeys();
			System.out.println("selectionKeys 数量 = " + selectionKeys.size());
			//遍历
			final Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
			while (keyIterator.hasNext()) {
				//获取selectionKey
				final SelectionKey selectionKey = keyIterator.next();
				//根据客户端事件做相应的处理
				if (selectionKey.isAcceptable()) {
					//给客户端生成一个socketChannel
					final SocketChannel socketChannel = serverSocketChannel.accept();
					System.out.println("客户端连接成功 生成一个socketChannel: " + socketChannel.hashCode());
					//将socketChannel注册到selector, 关注事件为op_read, 同时关联一个buffer
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
				} else if (selectionKey.isReadable()) {
					//通过key 获取channel
					final SocketChannel channel = (SocketChannel) selectionKey.channel();
					//获取channel关联的buffer
					final ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
					channel.read(buffer);
					System.out.println("from 客户端: " + new String(buffer.array()));
				}
				//手动从集合中移动当前的selectionKey, 防止重复操作
				keyIterator.remove();
			}
		}
	}
}
