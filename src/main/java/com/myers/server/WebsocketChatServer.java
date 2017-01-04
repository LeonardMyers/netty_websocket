package com.myers.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WebsocketChatServer {
	private int port;
	
	public WebsocketChatServer(int port) {
		this.port = port;
	}
	
	public void run() throws Exception {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(boss, worker)
				.channel(NioServerSocketChannel.class)
				.childHandler(new WebsocketChatServerInitializer())
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			System.out.println("WebsocketChatServer 启动了!");
			
			//绑定端口，开始接收进来的连接
			ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
			
			//等待服务器socket关闭（在这个例子中不会发生，但仍可以优雅的关闭服务器）
			channelFuture.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
			
			System.out.println("WebsocketChatServer 关闭了!");
		}
	}
	
	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8081;
		}
		
		new WebsocketChatServer(port).run();
	}
}
