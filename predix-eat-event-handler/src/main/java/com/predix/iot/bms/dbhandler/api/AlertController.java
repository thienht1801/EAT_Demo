package com.predix.iot.bms.dbhandler.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.predix.iot.bms.dbhandler.entity.Alert;
import com.predix.iot.bms.dbhandler.repository.AlertRepository;
import com.predix.iot.bms.dbhandler.util.DBHandlerUtils;


/**
 * Incident Controller
 * 
 * @author Anh V. Nguyen (anhnv16@fsoft.com.vn)
 *
 */
@RepositoryRestController
@RequestMapping(value="/alerts")
public class AlertController extends BaseController<Alert, AlertRepository> {

	private static Logger log = Logger.getLogger(AlertController.class);

	@Autowired
	private AlertRepository alertRepository;

	/**
	 * @author Tai Huynh
	 * save a batch incidents
	 * @param incidents list of {@link com.predix.iot.Alert.common.dto.Incident}
	 * @return {@link ResponseEntity} to return the status the result
	 */
	@RequestMapping(value = "/batch", method = RequestMethod.POST)
	public ResponseEntity<String> createQueue(@RequestBody List<Alert> alerts) {
		log.debug("Save a batch alert");
		List<Alert> allData = new ArrayList<Alert>();
		for (Alert alert : alerts) {
			allData.add(DBHandlerUtils.convertToDBAlert(alert));
		}
		List<Alert> result =  (List<Alert>) alertRepository.save(allData);
		if(result != null && result.size() > 0) {
			return new ResponseEntity<String>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

}
