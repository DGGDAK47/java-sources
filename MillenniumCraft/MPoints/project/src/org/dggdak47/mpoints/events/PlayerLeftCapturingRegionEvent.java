package org.dggdak47.mpoints.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.dggdak47.mpoints.area.Area;

public class PlayerLeftCapturingRegionEvent extends PlayerEvent{
	private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
	
	
	private Area leftCapturingRegionArea;
	
	public Area getLeftCapturingRegionArea() {
		return this.leftCapturingRegionArea;
	}
	
	public PlayerLeftCapturingRegionEvent(Player who, Area leftCapturingRegionArea) {
		super(who);
		this.leftCapturingRegionArea = leftCapturingRegionArea;
	}
}
