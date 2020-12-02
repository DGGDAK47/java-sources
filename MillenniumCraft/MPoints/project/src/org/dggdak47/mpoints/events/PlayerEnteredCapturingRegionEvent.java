package org.dggdak47.mpoints.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.dggdak47.mpoints.area.Area;

public class PlayerEnteredCapturingRegionEvent extends PlayerEvent{
	private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
	
	
    
	private Area enteredCapturingRegionArea;
	
	public Area getCapturingRegionArea() {
		return this.enteredCapturingRegionArea;
	}
	
	public PlayerEnteredCapturingRegionEvent(Player who, Area enteredCapturingRegionArea) {
		super(who);
		this.enteredCapturingRegionArea = enteredCapturingRegionArea;
	}
}
