package com.leo.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
		System.out.println("MyServerHandler 被调用!");
		System.out.println("从客户端: " + ctx.channel().remoteAddress() + " 收取到: " + msg);
		
		//给客户端发送long
		ctx.writeAndFlush(98765L);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
