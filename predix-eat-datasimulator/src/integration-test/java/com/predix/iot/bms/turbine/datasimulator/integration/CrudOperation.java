/**
 * predix-wts-backend-api
 */
package com.predix.iot.wts.turbine.datasimulator.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/******************************************************************
 * CrudOperation
 * 
 * @author Andy
 * @date Feb 3, 2016
 * 
 ******************************************************************/

@Component
public class CrudOperation<T> {

	@Autowired
	RestTemplate restTemplate;

	private HttpEntity<T> getRequest(T data, HttpHeaders headers) {
		if (data == null) {
			return new HttpEntity<T>(headers);
		} else {
			return new HttpEntity<T>(data, headers);
		}
	}

	public T insertData(String url, T data, HttpHeaders headers, Class<T> type) {
		System.out.println("*****Insert an object*****");
		ResponseEntity<T> response = restTemplate.postForEntity(url, getRequest(data, headers),
				type);
		return response.getBody();
	}

	public T[] getAllData(String url, HttpHeaders headers, Class<T[]> type) {
		System.out.println("*****Get all data*****");
		ResponseEntity<T[]> response = restTemplate.exchange(url, HttpMethod.GET, getRequest(null, headers),
				type);
		System.out.println("Found: " + response.getBody().length);
		return (T[]) response.getBody();
	}

	public T updateData(String url, long id, T data, HttpHeaders headers, Class<T> type) {
		System.out.println("*****Update a turbine*****");
		ResponseEntity<T> response = restTemplate.exchange(url + "/" + id, HttpMethod.PUT,
				getRequest(data, headers), type);
		return response.getBody();
	}

	public void deleteData(String url, long id, HttpHeaders headers) {
		restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, getRequest(null, headers), String.class);
	}
}
