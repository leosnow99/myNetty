package com.leo.last.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloServer {
    public static void main(String[] args) throws InterruptedException {
        //定义一对线程组
        //主线程组，用于接受客户端的连接，但是不做任何处理
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //从线程组，用于处理任务
        final NioEventLoopGroup workGroup = new NioEventLoopGroup();


        try {
            //netty服务器的创建 ServerBootstrap是一个启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(workGroup, bossGroup) //设置主线程组
                    .channel(NioServerSocketChannel.class) //设置nio通道
                    .childHandler(new HelloServerInitializer()); //子处理器， 用于处理workGroup

            //启动server, 并且设置8888为启动端口号，启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();
            //监听关闭的channel， 并且设置为同步方式
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }
}
