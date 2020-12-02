package org.dggdak47.manager.exceptions;

public class PlayerNotExistException extends ManagerException{
	private String playerName;
	public String getPlayerName() {
		return this.playerName;
	}
	
	public PlayerNotExistException(String message, String playerName) {
		super(message);
		this.playerName = playerName;
	}

}
