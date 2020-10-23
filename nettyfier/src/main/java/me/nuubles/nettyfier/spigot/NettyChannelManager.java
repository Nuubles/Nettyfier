package me.nuubles.nettyfier.spigot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.Plugin;

public class NettyChannelManager {
	private static HashMap<NettyMessageListener, Plugin> listeners = new HashMap<NettyMessageListener, Plugin>();
	private static NettyMessageListener[] baked = new NettyMessageListener[0];
	
	public static synchronized void register(NettyMessageListener listener, Plugin plugin) {
		int size = listeners.size();
		listeners.put(listener, plugin);
		baked = Arrays.copyOf(baked, size + 1);
		baked[size] = listener;
	}
	
	
	public Map<String, NettyMessageListener> getListeners() {
		return null;
	}
	
	
	static Message callReceived(INetworkManager networkManager, Connection connection, Message packet) {
		for(NettyMessageListener listener : baked) {
			
		}
	}
}
