package com.leo.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class TestHttpServerHand extends SimpleChannelInboundHandler<HttpObject> {
	//读取客户端数据
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject msg) throws Exception {
		//判断msg是不是HttpRequest请求
		if (msg instanceof HttpRequest) {
//			System.out.println("msg 类型: " + msg.getClass());
//			System.out.println("客户端地址: " + channelHandlerContext.channel().remoteAddress());
			System.out.println("Pipeline hashcode: " + channelHandlerContext.pipeline().hashCode() + " Test" +
					"HttpServerHandler hash: " + this.hashCode());
			//获取请求
			HttpRequest httpRequest = (HttpRequest) msg;
			//获取请求URI
			final String uri = httpRequest.uri();
			if ("/favicon.ico".equals(uri)) {
				return;
			}
			//返回数据
			final ByteBuf byteBuf = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);
			//构造响应
			final DefaultHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
			
			//返回响应
			channelHandlerContext.writeAndFlush(response);
		}
	}
}
