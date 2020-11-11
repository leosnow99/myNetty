package com.leo.netty.decoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
		//回写数据
		System.out.println(msg);
		System.out.println("收到服务器: " + ctx.channel().remoteAddress() + " 的消息: " + msg);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("MyClientHandler发送数据!");
		ctx.writeAndFlush(Unpooled.copiedBuffer("123565755".getBytes()));
		ctx.writeAndFlush(123456L);
		super.channelActive(ctx);
	}
}
