package com.predix.iot.bms.dbhandler.specification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.predix.iot.bms.dbhandler.criteria.QueryCriteria;
import com.predix.iot.bms.dbhandler.entity.BaseEntity;

/**
 * Generic query specification, will help to build criteria operations based on the 
 * string based operations input by user.
 *
 * @author Anh V. Nguyen (anhnv16@fsoft.com)
 * @param <T> the generic type
 */
public class QuerySpecification<T extends BaseEntity> implements Specification<T> {
	
	/** The criteria. */
	private QueryCriteria criteria;
	
	/**
	 * Instantiates a new query specification.
	 *
	 * @param criteria the criteria
	 */
	public QuerySpecification(QueryCriteria criteria) {
		this.criteria = criteria;
	}
	
	/**
	 * Override method toPredicate from Specification.
	 *
	 * @param root the root
	 * @param query the query
	 * @param builder the builder
	 * @return Predicate object
	 */
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		@SuppressWarnings("rawtypes")
		Class dataType = root.get(criteria.getKey()).getJavaType();
		if (dataType == Long.class || dataType == Integer.class || dataType == Double.class) { // Number & DateTime
			if (criteria.getOperation().equalsIgnoreCase(":")) {
				return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            } 
			else if (criteria.getOperation().equalsIgnoreCase(">")) {
            	return builder.greaterThan(root.<Double>get(criteria.getKey()),(Double)criteria.getValue());
            }
            else if (criteria.getOperation().equalsIgnoreCase(">=")) {
            	return builder.greaterThanOrEqualTo(root.<Double>get(criteria.getKey()),(Double)criteria.getValue());
            } 
            else if (criteria.getOperation().equalsIgnoreCase("<")) {
            	return builder.lessThan(root.<Double>get(criteria.getKey()), (Double)criteria.getValue());
            }			
            else if (criteria.getOperation().equalsIgnoreCase("<=")) {
            	return builder.lessThanOrEqualTo(root.<Double>get(criteria.getKey()), (Double)criteria.getValue());
            }
		} else if (dataType == String.class) { // String
			return builder.like(root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
		} else if (dataType == Boolean.class) { // Boolean
			if (Boolean.parseBoolean(criteria.getValue().toString())) {
        		return builder.isTrue(root.<Boolean>get(criteria.getKey()));
        	} else {
        		return builder.isFalse(root.<Boolean>get(criteria.getKey()));
        	}
		} else if (dataType == Date.class) { // Date
			Date dateInQuery = new Date(Long.parseLong(criteria.getValue().toString()));
			if (criteria.getOperation().equalsIgnoreCase(":")) {
				return builder.equal(root.<Date>get(criteria.getKey()), dateInQuery);
            } 
			else if (criteria.getOperation().equalsIgnoreCase(">")) {
            	return builder.greaterThan(root.<Date>get(criteria.getKey()), dateInQuery);
            }
            else if (criteria.getOperation().equalsIgnoreCase(">=")) {
            	return builder.greaterThanOrEqualTo(root.<Date>get(criteria.getKey()), dateInQuery);
            } 
            else if (criteria.getOperation().equalsIgnoreCase("<")) {
            	return builder.lessThan(root.<Date>get(criteria.getKey()), dateInQuery);
            }			
            else if (criteria.getOperation().equalsIgnoreCase("<=")) {
            	return builder.lessThanOrEqualTo(root.<Date>get(criteria.getKey()), dateInQuery);
            }
		}
        return null;
	}
}
