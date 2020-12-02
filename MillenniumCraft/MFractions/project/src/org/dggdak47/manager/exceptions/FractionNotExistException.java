package org.dggdak47.manager.exceptions;

public class FractionNotExistException extends ManagerException{
	private Integer fractionID;
	
	public Integer getId() {
		return this.fractionID;
	}
	
	public FractionNotExistException(String message, Integer fractionID) {
		super(message);
		this.fractionID = fractionID;
	}

}
