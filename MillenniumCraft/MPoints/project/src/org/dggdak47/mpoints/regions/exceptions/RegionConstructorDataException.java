package org.dggdak47.mpoints.regions.exceptions;

public class RegionConstructorDataException extends RegionException{
	public final String parameterName;
	
	public RegionConstructorDataException(String parameterName) {
		this.parameterName = parameterName;
	}
}
