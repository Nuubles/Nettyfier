package me.nuubles.nettyfier.netty;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.NonNull;

/**
 * Netty client used to communicate with the server
 * @author Nuubles
 *
 */
public class NettyClient<T extends ChannelHandler> {
	private EventLoopGroup 	group = new NioEventLoopGroup();
	private final String 	address;
	private final int 		port;
	private final T 		handler;
	
	/**
	 * Initializes a new client for netty server
	 * @param address address of the server
	 * @param port port of the remote netty server. Range 0001 - 9999 inclusive
	 * @param handler channel handler 
	 * @throws NullPointerException address was null
	 * @throws IllegalArgumentException address was empty or port out of range
	 */
	public NettyClient(@NonNull String address, int port, @NonNull T handler) 
			throws NullPointerException, IllegalArgumentException {
		if(port < 0001 || port > 9999)
			throw new IllegalArgumentException("Port out of range: " + port);
		if(address.equals(""))
			throw new IllegalArgumentException("Given address was empty");
		this.address = address;
		this.port = port;
		this.handler = handler;
	}
	
	
	/**
	 * Creates a netty client
	 * @return was the client creation successful
	 */
	public boolean createClient() {
		boolean success = true;
		
		try{
		    Bootstrap clientBootstrap = new Bootstrap();
	
		    clientBootstrap.group(group);
		    clientBootstrap.channel(NioSocketChannel.class);
		    clientBootstrap.remoteAddress(new InetSocketAddress(address, port));
		    clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
		        protected void initChannel(SocketChannel socketChannel) throws Exception {
		            socketChannel.pipeline().addLast(handler);
		        }
		    });
		    
		    ChannelFuture channelFuture = clientBootstrap.connect().sync();
		    channelFuture.channel().closeFuture().sync();
		} catch(InterruptedException e) {
			success = false;
			e.printStackTrace();   
		} finally {
		    try {
		    	if(group != null)
		    		group.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				success = false;
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	
	/**
	 * Closes the client forcefully
	 * @return was the client close successful
	 */
	public boolean forceClose() {
		boolean success = true;
		
		if(group != null) {
			try {
				group.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				success = false;
				e.printStackTrace();
			}
		}
		
		return success;
	}
}
