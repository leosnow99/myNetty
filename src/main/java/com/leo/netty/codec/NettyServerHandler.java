package com.leo.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
	/**
	 *
	 * @param ctx ChannelHandlerContext 上下文对象, 含有 管道pipeline, 通道channel, 地址
	 * @param msg 客户端发送的数据, 默认Object
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
		final MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
		if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
			final MyDataInfo.Student student = msg.getStudent();
			System.out.println("学生Id= " + student.getId() + " 姓名: " + student.getName());
		} else {
			final MyDataInfo.Worker worker = msg.getWorker();
			System.out.println("工人名字: " + worker.getName() + " 工人年龄: " + worker.getAge());
		}
	}
	
	//数据读取完毕
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端", CharsetUtil.UTF_8));
	}
	
	//处理异常
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.channel().close();
	}
}
