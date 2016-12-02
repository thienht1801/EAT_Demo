package com.predix.iot.eat.temperature.datasimulator.timeseries.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.predix.iot.bms.common.asset.entity.BaseAsset;

/******************************************************************
 * TurbineAsset
 *
 * @author Hung
 *
 ******************************************************************/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildingEntity extends BaseAsset{
	String name;
	double longitude;
	double latitude;
	String street;
	String address;
	String city;
	String state;
	int zip_code;

	public BuildingEntity(){

	}


	public BuildingEntity(String name, double longitude, double latitude, String street, String address, String city,
			String state, int zip_code) {
		super();
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.street = street;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip_code = zip_code;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public String getStreet() {
		return street;
	}


	public void setStreet(String street) {
		this.street = street;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public int getZip_code() {
		return zip_code;
	}


	public void setZip_code(int zip_code) {
		this.zip_code = zip_code;
	}

	@Override
	public String toString(){
		return "BuildingAsset [uri="+getUri()+", name=" + name + ", longitude=" + longitude + ", latitude=" + latitude + ", street=" + street
				+ ", address=" + address + ", city=" + city + ", state="
				+ state + ", zip_code=" + zip_code+"]";
	}




}
