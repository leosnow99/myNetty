package com.leo.netty.nio;


import java.io.*;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;

public class NIOFileChannel {
	public static void main(String[] args) {
		try {
			scattingAndGathering();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void read() throws IOException {
		//创建文件输入流
		final File file = new File("d:\\tem\\file01.txt");
		final FileInputStream fileInputStream = new FileInputStream(file);
		//通过输入流获取channel 实际类型FileChannelImpl
		final FileChannel fileChannel = fileInputStream.getChannel();
		
		//创建缓冲区
		final ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
		//将通道数据读入到buffer
		fileChannel.read(byteBuffer);
		
		//将byteBuffer中的字节数据,转换成String
		System.out.println(new String(byteBuffer.array()));
		fileInputStream.close();
		
	}
	
	public static void copy() throws IOException {
		final FileInputStream sourceStream = new FileInputStream("d:\\tem\\file01.txt");
		final FileOutputStream dstStream = new FileOutputStream("d:\\tem\\file02.txt");
		final FileChannel sourceChannel = sourceStream.getChannel();
		final FileChannel dstChannel = dstStream.getChannel();
		dstChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		sourceChannel.close();
		dstChannel.close();
		sourceStream.close();
		dstStream.close();
	}
	
	public static void write() throws IOException {
		String str = "Hi, Leo!";
		//创建一个输出流
		final FileOutputStream fileOutputStream = new FileOutputStream("d:\\tem\\file01.txt");
		//通过输出流获取对应的FileChannel
		final FileChannel fileChannel = fileOutputStream.getChannel();
		//创建一个缓冲区 ByteBuffer
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		//将str放入byteBuffer
		byteBuffer.put(str.getBytes());
		//对byteBuffer转换
		byteBuffer.flip();
		//将byteBuffer写入file中
		fileChannel.write(byteBuffer);
		fileOutputStream.close();
	}
	
	public static void scattingAndGathering() throws IOException {
		//使用ServerSocketChannel和SocketChannel网络
		final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		final InetSocketAddress inetAddress = new InetSocketAddress(7000);
		
		//绑定端口到socket并启动
		serverSocketChannel.socket().bind(inetAddress);
		//创建buffer数组
		final ByteBuffer[] byteBuffers = new ByteBuffer[2];
		byteBuffers[0] = ByteBuffer.allocate(5);
		byteBuffers[1] = ByteBuffer.allocate(3);
		
		//等待客户端连接
		final SocketChannel socketChannel = serverSocketChannel.accept();
		int messageLength = 8;
		while (true) {
			int byteRead =0;
			while (byteRead < messageLength) {
				final long read = socketChannel.read(byteBuffers);
				byteRead += read; //累计读取字节数
				System.out.println("读取: " + byteRead);
				Arrays.asList(byteBuffers).stream().map(byteBuffer -> "position=" + byteBuffer.position() + " limit= " + byteBuffer.limit())
						.forEach(System.out::println);
			}
			//将所有buffer反转
			Arrays.asList(byteBuffers).forEach(Buffer::flip);
			//将数据读出到客户端
			long byteWrite = 0;
			while (byteWrite < messageLength) {
				final long write = socketChannel.write(byteBuffers);
				byteWrite += write;
			}
			//将所有buffer clean
			Arrays.asList(byteBuffers).forEach(Buffer::clear);
			System.out.println("byteRead:="+ byteRead + " byteWrite" + byteWrite);
		}
		
	}
}
