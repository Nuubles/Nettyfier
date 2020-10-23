package me.nuubles.nettyfier.spigot;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import me.nuubles.nettyfier.netty.NettyServer;
import me.nuubles.nettyfier.spigot.netty.ServerChannelInboundHandler;

public class Nettyfier extends JavaPlugin {
	//private NettyClient<SpigotChannelInboundHandler> client;
	private NettyServer<ServerChannelInboundHandler> server;
	private static Nettyfier nettyfier;
	
	public Nettyfier() throws IOException {
		nettyfier = this;
		Configuration config = new Configuration();
		
		server = new NettyServer<ServerChannelInboundHandler>(config.getAddress(), config.getPort(), new ServerChannelInboundHandler());
		server.startServer();
		// creates a new netty client which sends data to 
		//client = new NettyClient<SpigotChannelInboundHandler>(config.getAddress(), config.getPort(), new SpigotChannelInboundHandler());
		//client.createClient();
	}
	
	
	@Override
	public void onDisable() {
		server.stopServer();
	}
	
	
	/**
	 * Retrieves an instance of nettyfier
	 * @return plugin instance or null if plugin has not been initialized
	 */
	public static Nettyfier getInstance() {
		return nettyfier;
	}
	
	
	public void registerOutgoingDataChannel(JavaPlugin plugin, String channel) {
		//TODO
	}
	
	
	public void registerIncomingDataChannel(JavaPlugin plugin, String channel, NettyMessageListener listener) {
		//TODO
	}
}
