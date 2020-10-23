package me.nuubles.nettyfier;

import lombok.Getter;

/**
 * Exception thrown when nettyfier configuration is malformed
 * @author Nuubles
 *
 */
public class ConfigurationException extends Exception {
	private static final long serialVersionUID = 5004879392703809200L;
	@Getter
	private final String configurationPath;
	
	public ConfigurationException(String errorMessage, String configurationPath) {
		super(errorMessage);
		this.configurationPath = configurationPath;
	}
}
