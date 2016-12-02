package com.predix.iot.bms.dbhandler.api;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.predix.iot.bms.dbhandler.entity.BaseEntity;
import com.predix.iot.bms.dbhandler.specification.builder.SpecificationsBuilder;

/**
 * Generic base controller, every subclasses which inherited from this class
 * will automatically expose the query api
 * 
 * @author Anh V. Nguyen (anhnv16@fsoft.com.vn)
 * 
 * @param <T>
 *            for Entity
 * @param <R>
 *            for Repository
 */
public class BaseController<T extends BaseEntity, R extends JpaSpecificationExecutor<T>> {

	@Autowired
	R repository;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	/**
	 * Query API with query parameter with format: [property][operation: &#58; |
	 * &gt; | &lt; | &gt;= | &lt;=][value] multiple criteria will be separated
	 * by commas
	 * 
	 * @param query
	 * @return ResponseEntity object
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/query")
	public ResponseEntity<List<T>> query(
			@RequestParam(value = "query", defaultValue = "") String query,
			@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "length", defaultValue = "10") int length,
			@RequestParam(value = "isPaging", defaultValue = "false") String isPaging,
			@RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder) {
		query = query.trim();
		if(logger.isDebugEnabled()) {
			logger.debug("DBHandler query: " + query);
		}
		SpecificationsBuilder<T> builder = new SpecificationsBuilder<>();
		String regex = "(\\w+?)(:|<|>|>=|<=)(([\\w/]+( +[\\w/]+)+?)|([\\w/]+?)),";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(query + ",");
		while (matcher.find()) {
			builder.addCriteriaWith(matcher.group(1), matcher.group(2),
					matcher.group(3));
		}
		Specification<T> spec = builder.build();
		Pageable pageable = null;
		if (Boolean.parseBoolean(isPaging)) {
			if ("ASC".equalsIgnoreCase(sortOrder)) {
				pageable = new PageRequest(start / length, length, new Sort(
						Sort.Direction.ASC, sortBy));
			} else {
				pageable = new PageRequest(start / length, length, new Sort(
						Sort.Direction.DESC, sortBy));
			}
		}
		Page<T> results = repository.findAll(spec, pageable);
		return new ResponseEntity<List<T>>(results.getContent(), HttpStatus.OK);
	}

	/**
	 * Count the entity API with query parameter with format: [property][operation: &#58; |
	 * &gt; | &lt; | &gt;= | &lt;=][value] multiple criteria will be separated
	 * by commas
	 * 
	 * @param query
	 * @return count of object
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/count")
	public ResponseEntity<Long> count(
			@RequestParam(value = "query", defaultValue = "") String query) {
		query = query.trim();
		SpecificationsBuilder<T> builder = new SpecificationsBuilder<>();
		String regex = "(\\w+?)(:|<|>|>=|<=)(([\\w/]+( +[\\w/]+)+?)|([\\w/]+?)),";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(query + ",");
		while (matcher.find()) {
			builder.addCriteriaWith(matcher.group(1), matcher.group(2),
					matcher.group(3));
		}
		Specification<T> spec = builder.build();
		Long results = repository.count(spec);
		return new ResponseEntity<Long>(results, HttpStatus.OK);
	}
}
