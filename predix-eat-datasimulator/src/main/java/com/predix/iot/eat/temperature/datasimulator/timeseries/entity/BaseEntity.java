/**
 * predix-wts-common
 */
package com.predix.iot.eat.temperature.datasimulator.timeseries.entity;

/******************************************************************
 * BaseAsset
 * 
 * @author Andy
 * @date Mar 8, 2016
 * 
 ******************************************************************/

public abstract class BaseEntity {

	String uri;
	
	Long id;
	
	public BaseEntity() {}
	
	public BaseEntity(BaseEntity asset){
		this.uri = asset.uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
