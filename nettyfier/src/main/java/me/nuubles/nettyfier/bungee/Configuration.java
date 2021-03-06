package me.nuubles.nettyfier.bungee;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;

import me.nuubles.nettyfier.ServerProperties;

/**
 * Wrapper for SnakeYAML Yaml class.
 * @author Nuubles
 *
 */
public class Configuration {
	private final File file;
	private final FileWriter writer;
	private Map<String, Object> fileData;
	private final static String SERVER_KEY = "spigot-servers";
	
	public Configuration() throws IOException {
		file = new File(File.pathSeparator + "Nettyfier" + File.pathSeparator + "config.yml");
		writer = new FileWriter(file);
		
		// generate file if it does not yet exist
		if(!file.exists())
			generateFile();
		
		refresh();
	}
	
	
	/**
	 * Generates a default configuration file
	 */
	private void generateFile() throws IOException {
		Map<String, Object> exampleData = new HashMap<String, Object>();
		exampleData.put("address", "localhost");
		exampleData.put("port", 25585);
		exampleData.put("threads", 2);
		exampleData.put("max-request-size-kb", 1024);
		
		exampleData.put(SERVER_KEY + ".default", "localhost:25566");
		
		new Yaml(getOptions()).dump(exampleData, writer);
	}
	
	
	/**
	 * Returns config file options
	 * @return options
	 */
	private DumperOptions getOptions() {
		DumperOptions options = new DumperOptions();
		options.setIndent(2);
		options.setDefaultFlowStyle(FlowStyle.BLOCK);
		return options;
	}
	
	
	/**
	 * Refreshes the data from config
	 */
	public void refresh() {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file.getPath());
		this.fileData = new Yaml(getOptions()).load(inputStream);
	}
	
	
	/**
	 * Returns the read address from config
	 * @return address of the bungee side netty server
	 */
	public String getAddress() {
		return (String)fileData.get("address");
	}
	
	
	/**
	 * Returns the read port from config
	 * @return port of the bungee side nettyserver
	 */
	public int getPort() {
		return (Integer)fileData.get("port");
	}
	
	
	/**
	 * Gets the amount of threads to be used for netty server
	 * @return amount of threads to use for netty server
	 */
	public int getThreadCount() {
		return (Integer)fileData.get("threads");
	}
	
	
	/**
	 * Returns the maximum request side to be used in kilobytes
	 * @return max request size in kilobytes
	 */
	public int getMaxRequestSize() {
		return (Integer)fileData.get("max-request-size-kb");
	}
	
	
	/**
	 * Returns data of all the servers from the configuration file
	 * @return list of all loaded server properties
	 */
	public List<ServerProperties> getServerProperties() {
		// check that the key exists
		if(!fileData.containsKey(SERVER_KEY)) return null;
		Object serverMap = fileData.get(SERVER_KEY);
		if(!(serverMap instanceof Map)) return null;
		@SuppressWarnings("unchecked")
		Map<String, Object> servers = (Map<String, Object>)serverMap;
		
		List<ServerProperties> props = new LinkedList<ServerProperties>();
		
		// loop all servers in the config
		for(Entry<String, Object> entry : servers.entrySet()) {
			if(!(entry.getValue() instanceof String)) continue;
			String[] addressAndPort = ((String)entry.getValue()).split(":");
			
			// check if the length is valid
			if(addressAndPort.length != 2) {
				System.out.printf("Invalid length in Nettyfier config at key %s\n", entry.getKey());
				continue;
			}
			
			int port = -1;
			// parse values into a ServerProperties object
			try {
				port = Integer.parseInt(addressAndPort[1]);
			} catch(NumberFormatException e) {
				System.out.printf("Invalid port in Nettyfier config at key %s\n", entry.getKey());
				continue;
			}
			
			try {
				props.add(new ServerProperties(entry.getKey(), addressAndPort[0], port));
			} catch(IllegalArgumentException e) {
				System.out.printf("Invalid address in Nettyfier config at key %s\n", entry.getKey());
				continue;
			}
		}
		
		return props;
	}
}
