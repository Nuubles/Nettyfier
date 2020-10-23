package me.nuubles.nettyfier.spigot;

public interface NettyMessageListener {
	public abstract void onNettyMessageReceived(String channel, Object server, byte[] message);
}
