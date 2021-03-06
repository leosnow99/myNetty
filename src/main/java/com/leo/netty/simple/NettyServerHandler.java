package com.leo.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	/**
	 *
	 * @param ctx ChannelHandlerContext 上下文对象, 含有 管道pipeline, 通道channel, 地址
	 * @param msg 客户端发送的数据, 默认Object
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("server ctx= " + ctx);
		//将msg转化为ByteBuffer
		ByteBuf buffer = (ByteBuf) msg;
		System.out.println("客户端发送的消息: " + buffer.toString(CharsetUtil.UTF_8));
		System.out.println("客户端的地址: " + ctx.channel().remoteAddress());
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
