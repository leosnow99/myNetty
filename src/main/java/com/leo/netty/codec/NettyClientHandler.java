package com.leo.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
	
	//当服务器就绪就会触发
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//随机发送worker或者student
		final int nextInt = new Random().nextInt(3);
		MyDataInfo.MyMessage myMessage = null;
		
		if (nextInt == 0) {
			myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.StudentType).
					setStudent(MyDataInfo.Student.newBuilder().setId(10).setName("武松").build()).build();
		} else {
			myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.WorkerType).
					setWorker(MyDataInfo.Worker.newBuilder().setAge(20).setName("leo").build()).build();
		}
		
		ctx.writeAndFlush(myMessage);
	}
	
	//当服务器有读取时间是, 就会触发
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		System.out.println("服务器回复的消息" + byteBuf.toString(CharsetUtil.UTF_8));
		System.out.println("服务器的地址: " + ctx.channel().remoteAddress());
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
