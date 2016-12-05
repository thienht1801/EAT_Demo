package com.predix.iot.eat.temperature.datasimulator.app.exceptions;

public class CustomException extends Exception{

	/**
	 *
	 */
	private static final long serialVersionUID = -1603531732655570747L;

	private String message;
	private Throwable exception;

	public CustomException() {
		super();
	}
	public CustomException(String message) {
		super(message);
	}
	public CustomException(Throwable cause) {
		super(cause);
	}
	public CustomException(String message, Throwable cause) {
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
		return "Exception [message=" + message + ", exception=" + exception + "]";
	}
}
