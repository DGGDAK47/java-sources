package org.dggdak47.mloot.blockWrapper.exceptions;

public class BlockWrapperConstructorException extends BlockWrapperException{
	public final String fieldName;
	
	public BlockWrapperConstructorException(String msg, String fieldName) {
		super(msg);
		this.fieldName = fieldName;
	}

}
