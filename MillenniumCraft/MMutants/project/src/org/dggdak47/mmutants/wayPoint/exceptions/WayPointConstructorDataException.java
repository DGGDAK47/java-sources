package org.dggdak47.mmutants.wayPoint.exceptions;

public class WayPointConstructorDataException extends WayPointException{
	public static enum ExceptionType{
		NULL, INCORRECT_DATA
	}
	
	private String fieldName;
	private ExceptionType type;
	
	public String getFieldName() {
		return this.fieldName;
	}
	public ExceptionType getExceptionType() {
		return this.type;
	}
	
	public WayPointConstructorDataException(String fieldName, ExceptionType type) {
		this.fieldName = fieldName;
		this.type = type;
	}
}
