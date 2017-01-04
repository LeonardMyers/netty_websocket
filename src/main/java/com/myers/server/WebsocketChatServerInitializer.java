package com.myers.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebsocketChatServerInitializer extends ChannelInitializer<SocketChannel>{
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline channelPipeline = ch.pipeline();
		
		channelPipeline.addLast(new HttpServerCodec());
		channelPipeline.addLast(new HttpObjectAggregator(64*1024));
		channelPipeline.addLast(new ChunkedWriteHandler());
		channelPipeline.addLast(new HttpRequestHandler("/ws"));
		channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		channelPipeline.addLast(new TextWebSocketFrameHandler());
	}
}
