package com.predix.iot.eat.ehandler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "audit-trail")
public class AuditTrailConfiguration {

	private String runtimeUri;
	private String tenantUuid;
	
	public String getRuntimeUri() {
		return runtimeUri;
	}

	public void setRuntimeUri(String runtimeUri) {
		this.runtimeUri = runtimeUri;
	}

	public String getTenantUuid() {
		return tenantUuid;
	}

	public void setTenantUuid(String tenantUuid) {
		this.tenantUuid = tenantUuid;
	}
			
}