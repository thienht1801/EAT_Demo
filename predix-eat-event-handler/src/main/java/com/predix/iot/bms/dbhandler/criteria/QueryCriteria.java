package com.predix.iot.bms.dbhandler.criteria;


/**
 * This object will carry the key (property), operation and value for criteria
 * supported operations: &#58; | &gt; | &lt; | &gt;= | &lt;=.
 *
 * @author Anh V. Nguyen (anhnv@fsoft.com.vn)
 */
public class QueryCriteria {
	
    /** The key. */
    private String key;
    
    /** The operation. */
    private String operation;
    
    /** The value. */
    private Object value;
    
	/**
	 * Instantiates a new query criteria.
	 *
	 * @param key the key
	 * @param operation the operation
	 * @param value the value
	 */
	public QueryCriteria(String key, String operation, Object value) {
		super();
		this.key = key;
		this.operation = operation;
		this.value = value;
	}
	
	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Sets the key.
	 *
	 * @param key the new key
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * Gets the operation.
	 *
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}
	
	/**
	 * Sets the operation.
	 *
	 * @param operation the new operation
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Object getValue() {
		return value;		
	}
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
}
