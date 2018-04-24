package com.daishuhua.eprj.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "eprj")
public class EprjConfig {
	
	private String[] permitClients;

	public String[] getPermitClients() {
		return permitClients;
	}

	public void setPermitClients(String[] permitClients) {
		this.permitClients = permitClients;
	}
	
	
}
