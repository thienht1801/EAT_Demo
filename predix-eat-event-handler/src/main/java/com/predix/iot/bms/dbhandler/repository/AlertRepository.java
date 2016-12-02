package com.predix.iot.bms.dbhandler.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.predix.iot.bms.dbhandler.entity.Alert;

/**
 * Incident repository
 * 
 * @author Anh V. Nguyen (anhnv16@fsoft.com.vn)
 *
 */
public interface AlertRepository extends PagingAndSortingRepository<Alert, Long>, JpaSpecificationExecutor<Alert> {

}
