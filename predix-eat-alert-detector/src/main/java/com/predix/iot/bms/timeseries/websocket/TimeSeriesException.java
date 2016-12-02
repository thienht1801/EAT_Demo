package com.predix.iot.bms.timeseries.websocket;

public class TimeSeriesException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1603531732655570747L;

	private String message;
	private Throwable exception;
	
	public TimeSeriesException() {
		super();
	}
	public TimeSeriesException(String message) {
		super(message);
	}
	public TimeSeriesException(Throwable cause) {
		super(cause);
	}
	public TimeSeriesException(String message, Throwable cause) {
		super(message, cause);
	}
	@Override
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Throwable getException() {
		return exception;
	}
	public void setException(Throwable exception) {
		this.exception = exception;
	}
	@Override
	public String toString() {
		return "TimeSeriesException [message=" + message + ", exception=" + exception + "]";
	}
}
