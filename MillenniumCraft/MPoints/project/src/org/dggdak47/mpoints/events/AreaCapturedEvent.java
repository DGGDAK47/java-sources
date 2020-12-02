package org.dggdak47.mpoints.events;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.dggdak47.mpoints.area.Area;

public class AreaCapturedEvent extends Event{
	private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
	//----------------------
    private ArrayList<Player> whoWasCapturing;
    private Area capturedArea;
    
    public ArrayList<Player> getCapturingPlayers(){
    	return (ArrayList)this.whoWasCapturing.clone();
    }
    public Area getCapturedArea() {
    	return this.capturedArea;
    }
    
	public AreaCapturedEvent(ArrayList<Player> whoWasCapturing, Area capturedArea) {
		this.whoWasCapturing = whoWasCapturing;
		this.capturedArea = capturedArea;
	}
}
