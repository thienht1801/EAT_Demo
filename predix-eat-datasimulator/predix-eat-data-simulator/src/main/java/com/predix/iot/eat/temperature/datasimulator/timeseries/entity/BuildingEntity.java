package com.predix.iot.eat.temperature.datasimulator.timeseries.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildingEntity{
	Long id;
	double inDoorTemp;
	double outDoorTemp;
	double energyConsume;

	public BuildingEntity(){

	}

	public BuildingEntity(Long id, double inDoorTemp, double outDoorTemp, double energyConsume) {
		super();
		this.id = id;
		this.inDoorTemp = inDoorTemp;
		this.outDoorTemp = outDoorTemp;
		this.energyConsume = energyConsume;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getInDoorTemp() {
		return inDoorTemp;
	}

	public void setInDoorTemp(double inDoorTemp) {
		this.inDoorTemp = inDoorTemp;
	}

	public double getOutDoorTemp() {
		return outDoorTemp;
	}

	public void setOutDoorTemp(double outDoorTemp) {
		this.outDoorTemp = outDoorTemp;
	}

	public double getEnergyConsume() {
		return energyConsume;
	}

	public void setEnergyConsume(double energyConsume) {
		this.energyConsume = energyConsume;
	}

}
