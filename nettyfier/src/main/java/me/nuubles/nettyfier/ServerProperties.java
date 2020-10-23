package me.nuubles.nettyfier;

import lombok.Getter;
import lombok.NonNull;

/**
 * Object used to represet a server, whether it be a bungee server, spigot server, or any other server
 * @author Nuubles
 *
 */
public class ServerProperties implements Comparable<ServerProperties> {
	private final String name;
	@Getter
	private final String host;
	@Getter
	private final int port;
	
	public ServerProperties(@NonNull final String name, @NonNull final String host, int port) {
		this.name = name;
		this.host = host;
		this.port = port;
	}
	
	
	/**
	 * Returns the name assigned to this server property
	 * @return name of the server
	 */
	public String getName() {
		return this.name;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof ServerProperties))
			return false;
		ServerProperties props = (ServerProperties)o;
		return name.equals(props.name) && host.equals(props.host) && port == props.port;
	}


	@Override
	public int compareTo(ServerProperties arg) {
		return name.compareTo(arg.name);
	}
}
