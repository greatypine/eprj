package com.guoanshequ.eprj.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "eprj")
public class EprjConfig {
	
	private String[] permitClients;
    
	private String daqWebAddress;
	
	public String[] getPermitClients() {
		return permitClients;
	}

	public void setPermitClients(String[] permitClients) {
		this.permitClients = permitClients;
	}

	public String getDaqWebAddress() {
		return daqWebAddress;
	}

	public void setDaqWebAddress(String daqWebAddress) {
		this.daqWebAddress = daqWebAddress;
	}
	
	
}
