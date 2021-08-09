package it.gallo.pinmanager.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "pin-manager.config")
public class PropertyConfiguration {

	private Long sessionTimeout = 1200L;
	private Integer sessionParallel = 3;
	private Integer sessionAttempts = 3;

	private Integer pinLength = 6;
}
