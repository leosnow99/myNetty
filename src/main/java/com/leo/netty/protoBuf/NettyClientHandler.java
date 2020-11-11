package com.leo.netty.protoBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
	
	//当服务器就绪就会触发
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		final StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(4).setName("林冲").build();
		ctx.writeAndFlush(student);
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
