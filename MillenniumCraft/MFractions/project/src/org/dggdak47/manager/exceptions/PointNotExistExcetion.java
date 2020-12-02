package org.dggdak47.manager.exceptions;

public class PointNotExistExcetion extends ManagerException{
	public Integer id;
	public Integer getPointId() {
		return this.id;
	}
	
	public PointNotExistExcetion(String message, Integer id) {
		super(message);
		this.id = id;
	}

}
