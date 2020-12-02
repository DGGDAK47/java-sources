package org.dggdak47.mpoints.area.exceptions;

public class AreaConstructorDataException extends AreaException{
	public final String fieldName;
	public final String mapName;
	
	
	public AreaConstructorDataException(String mapName, String fieldName) {
		super("Area map with Name: "+mapName+" has no field with name: "+fieldName);
		this.fieldName = fieldName;
		this.mapName = mapName;
	}
}
