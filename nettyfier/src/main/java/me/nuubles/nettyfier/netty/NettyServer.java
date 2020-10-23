package me.nuubles.nettyfier.netty;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.NonNull;

/**
 * The main server of nettyfier
 * @author Nuubles
 */
public class NettyServer<T extends ChannelHandler> {
	private EventLoopGroup 	group = new NioEventLoopGroup();
	private final String 	address;
	private final int 		port;
	private final T			handler;
	
	/**
	 * Creates a new NettyServer instance
	 * @param address address of the server
	 * @param port port of the server. Must be between 0001 and 9999 inclusive
	 * @param handler server handler
	 * @throws IllegalArgumentException port was out of range or address was null
	 * @throws NullPointerException Address was null
	 */
	public NettyServer(@NonNull String address, int port,@NonNull T handler) 
			throws IllegalArgumentException, NullPointerException {
		if(port < 0 || port > 9999)
			throw new IllegalArgumentException("Port out of range: " + port);
		if(address.equals(""))
			throw new IllegalArgumentException("Given address was empty");
		this.address = address;
		this.port = port;
		this.handler = handler;
	}
	
	
	/**
	 * Starts the netty server.
	 * @return Was the server booted successfully
	 */
	public boolean startServer() {
		boolean success = true;
		try {
		    ServerBootstrap serverBootstrap = new ServerBootstrap();
		    serverBootstrap.group(group);
		    serverBootstrap.channel(NioServerSocketChannel.class);
		    serverBootstrap.localAddress(new InetSocketAddress(address, port));
		    
		    serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
		        protected void initChannel(SocketChannel socketChannel) throws Exception {
		            socketChannel.pipeline().addLast(handler);
		        }
		    });
		    
		    ChannelFuture channelFuture = serverBootstrap.bind().sync();
		    channelFuture.channel().closeFuture().sync();
		} catch(InterruptedException e){
		    e.printStackTrace();
		    success = false;
		} finally {
			// automatic server disabler
			try {
	    	if(group != null)
				group.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	group = null;
		}
		
		return success;
	}
	
	
	/**
	 * Stops the netty server forcefully
	 * @return was the server close a success 
	 */
	public boolean stopServer() {
		boolean success = true;
		try {
			if(group != null)
				group.shutdownGracefully().sync();
		} catch(InterruptedException e) {
			success = false;
			e.printStackTrace();
		}
		
		return success;
	}
}
