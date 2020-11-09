package com.leo.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
	public static void main(String[] args) throws IOException {
		//线程池机制
		
		//创建一个线程池
		final ExecutorService executorService = Executors.newCachedThreadPool();
		
		//创建ServerSocket
		final ServerSocket serverSocket = new ServerSocket(6666);
		System.out.println("服务器启动了");
		
		//如果有客户端连接, 就创建一个线程, 与之通信
		while (true) {
			//监听, 等待客户端连接
			final Socket socket = serverSocket.accept();
			System.out.println("连接到一个客户端");
			
			//创建一个线程, 与之通信
			executorService.execute(() -> {
				//和客户端通信
				handler(socket);
			});
		}
	}
	
	public static void handler(Socket socket) {
		try {
			System.out.println("线程信息: id=" + Thread.currentThread().getId() + " 名字: " + Thread.currentThread().getName());
			final byte[] bytes = new byte[1024];
			//通过socket获取输入流
			final InputStream inputStream = socket.getInputStream();
			
			//循环读取客户端发送到数据
			while (true) {
				final int read = inputStream.read(bytes);
				if (read != -1) {
					System.out.println(new String(bytes, 0, read));
				} else {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("关闭和client连接");
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
