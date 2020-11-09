package com.leo.netty.groupChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
	//定义channel组, 管理所有channel
	private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//使用hashMap管理
	public static Map<User, Channel> channels = new HashMap<>();
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) {
		//获取档期那channel
		final Channel channel = ctx.channel();
		channelGroup.forEach(ch -> {
			if (channel != ch) {
				ch.writeAndFlush("[客户]" + channel.remoteAddress() + "发送了消息: " + msg + "\n");
			}
		});
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
	
	//表示连接建立
	//将当前channel加入channelGroup
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		//将该客户加入聊天推送给其它在线客户端
		channelGroup.writeAndFlush("[客户端]" + ctx.channel().remoteAddress() + " " + dateFormat.format(new Date()) + "加入聊天\n");
		channelGroup.add(ctx.channel());
	}
	
	//表示channel处于活动状态, 提示xx上线
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		System.out.println(ctx.channel().remoteAddress() + " 上线!");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		System.out.println(ctx.channel().remoteAddress() + " 离线!");
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		final Channel channel = ctx.channel();
		channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " " + dateFormat.format(new Date()) + " 离开聊天!");
		System.out.println("channelGroup size: " + channelGroup.size());
	}
}
