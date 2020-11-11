package com.leo.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
	
	
	/**
	 *
	 * @param ctx 上下文对象
	 * @param in 入站的Bytebuffer
	 * @param out List集合, 将解码的数据传递给下一个handler
	 * @throws Exception
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		System.out.println("MyByteToLongDecoder 被调用!");
		if (in.readableBytes() >= 8) {
			out.add(in.readLong());
		} else {
			System.out.println(in.readableBytes());
			System.out.println("数据长度不够");
		}
	}
}
