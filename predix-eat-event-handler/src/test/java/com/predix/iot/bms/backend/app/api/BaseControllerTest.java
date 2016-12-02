package com.predix.iot.bms.backend.app.api;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.predix.iot.bms.dbhandler.app.Application;


/**
 * Base controller test
 * 
 * @author Anh V. Nguyen (anhnv16@fsoft.com.vn)
 *
 * @param <T> 
 */

@SuppressWarnings("rawtypes")
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
public class BaseControllerTest<T extends PagingAndSortingRepository> {
	@Autowired
	protected T repository;
	@Autowired
    protected WebApplicationContext webApplicationContext;
	protected MockMvc mockMvc;
	
	@Before
	public void init() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
	}
	
	@After
    public void teardown() throws Exception {
    	repository.deleteAll();
    }
}

