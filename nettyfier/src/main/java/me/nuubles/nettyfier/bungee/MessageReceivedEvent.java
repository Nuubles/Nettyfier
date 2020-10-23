package me.nuubles.nettyfier.bungee;


import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class MessageReceivedEvent extends Event implements Cancellable {
	private final String senderAddress;
	private final int senderPort;
	private final byte[] message;
	private final String channel;
	private boolean cancelled = false;
	
	
	public MessageReceivedEvent(final String channel, 
			final String senderAddress, 
			final int senderPort, 
			final byte[] message) {
		this.channel = channel;
		this.senderAddress = senderAddress;
		this.senderPort = senderPort;
		this.message = message;
	}
	
	
	public final String getTag() {
		return channel;
	}
	
	
	public final byte[] getData() {
		return message;
	} 
	
	
	public final int getSenderPort() {
		return senderPort;
	}
	
	
	public final String getSenderAddress() {
		return senderAddress;
	}


	@Override
	public boolean isCancelled() {
		return cancelled;
	}


	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
