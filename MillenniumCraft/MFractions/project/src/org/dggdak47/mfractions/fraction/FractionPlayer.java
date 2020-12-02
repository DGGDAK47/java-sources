package org.dggdak47.mfractions.fraction;

import org.dggdak47.mfractions.Manager;

public class FractionPlayer {
	private String playerName;
	private Integer fractionID;
	private Integer playerScore;
	private Integer spawnPointID;
	
	public String getPlayerName(){
		return this.playerName;
	}
	
	
	public Integer getFractionID() {
		return new Integer(this.fractionID);
	}
	
	public Integer getScore() {
		return new Integer(this.playerScore);
	}
	
	public Integer getSpawnPointID() {
		return new Integer(this.spawnPointID);
	}
	
	
	public void addScore(Integer score){
		this.playerScore += score;
	}
	
	public void setScore(Integer newScore) {
		this.playerScore = newScore;
	}
	
	public void setFractionID(Integer newFractionID){
		this.fractionID = newFractionID;
		
	}
	
	public void setSpawnPointID(Integer newSpawnPointID){
		this.spawnPointID = newSpawnPointID;
	}
	
	
	public FractionPlayer(String playerName, Integer fractionID, Integer playerScore, Integer spawnPointID) {
		this.playerName = playerName;
		this.fractionID = fractionID;
		this.playerScore = playerScore;
		this.spawnPointID = spawnPointID;
	}
}
