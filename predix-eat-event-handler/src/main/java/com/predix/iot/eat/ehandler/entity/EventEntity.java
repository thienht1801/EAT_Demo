package com.predix.iot.eat.ehandler.entity;

public class EventEntity {

	private String context;
	private String tag;
	private String classification;
	private Alert data;
	
	
	public EventEntity(){
		
	}
	
	public EventEntity(String context, String tag, String classification, Alert data) {
		super();
		this.context = context;
		this.tag = tag;
		this.classification = classification;
		this.data = data;
	}
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public Alert getData() {
		return data;
	}
	public void setData(Alert data) {
		this.data = data;
	}
	
	
	
}
