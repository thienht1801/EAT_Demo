package com.predix.iot.bms.dbhandler.specification.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import com.predix.iot.bms.dbhandler.criteria.QueryCriteria;
import com.predix.iot.bms.dbhandler.entity.BaseEntity;
import com.predix.iot.bms.dbhandler.specification.QuerySpecification;

/**
 * The builder will build the specification based on a list of QueryCriteria,
 * we can add criteria using the addCriteriawith method .
 *
 * @author  Nguyen V. Anh (anhnv16@fsoft.com.vn)
 * @param <T> the generic type
 */

public class SpecificationsBuilder<T extends BaseEntity> {
	
	/** The params. */
	private final List<QueryCriteria> params;
	
	/**
	 * Instantiates a new specifications builder.
	 */
	public SpecificationsBuilder() {
        params = new ArrayList<>();
    }

	/**
	 * Adding criteria for building specification.
	 *
	 * @param key the key
	 * @param operation the operation
	 * @param value the value
	 * @return current instance of SpecificationsBuilder
	 */
	public SpecificationsBuilder<T> addCriteriaWith(String key, String operation, Object value) {
        params.add(new QueryCriteria(key, operation, value));
        return this;
    }
	
	
	/**
	 * Build the specification based on the added criteria.
	 *
	 * @return Specification object
	 */
	public Specification<T> build() {
        if (params.isEmpty()) {
            return null;
        }
 
        List<Specification<T>> specs = new ArrayList<>();
        for (QueryCriteria param : params) {
            specs.add(new QuerySpecification<T>(param));
        }
 
        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
}
