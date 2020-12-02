package org.dggdak47.mmutants.invasion.exceptions;

public class InvasionConstructorDataException extends InvasionException{
	public static enum ExceptionType{
		NULL, INCORRECT_DATA
	}
	
	private ExceptionType type;
	private String fieldName;
	
	public ExceptionType getType() {
		return this.type;
	}
	public String getFieldName() {
		return this.fieldName;
	}
	
	public InvasionConstructorDataException(String fieldName, ExceptionType type) {
		this.type = type;
		this.fieldName = fieldName;
	}
}
