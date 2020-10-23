package me.nuubles.nettyfier.bungee;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nuubles.nettyfier.ServerProperties;
import me.nuubles.nettyfier.bungee.netty.ServerChannelInboundHandler;
import me.nuubles.nettyfier.netty.NettyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class Nettyfier extends Plugin {
	private final NettyServer<ServerChannelInboundHandler> server;
	private final Map<String, ServerProperties> spigotServers = new HashMap<String, ServerProperties>();
	private final Configuration config;
	private static Nettyfier instance;
	
	public Nettyfier() throws IOException {
		// create instance of this
		instance = this;
		
		// load and build the server properties
		config = new Configuration();
		List<ServerProperties> spigotServers = config.getServerProperties();
		for(ServerProperties props : spigotServers)
			this.spigotServers.put(props.getName(), props);
		
		// start the server in order to listen to calls
		server = new NettyServer<ServerChannelInboundHandler>(config.getAddress(), config.getPort(), new ServerChannelInboundHandler());
		server.startServer();
	}
	
	
	/**
	 * Returns an instance of nettyfier
	 * @return instance of nettyfier
	 */
	public static Nettyfier getInstance() {
		return instance;
	}
	
	
	@Override
	public void onDisable() {
		server.stopServer();
	}
	
	
	public void registerChannel(String channel) {
		// TODO getProxy().registerChannel(String)		
	}
}
