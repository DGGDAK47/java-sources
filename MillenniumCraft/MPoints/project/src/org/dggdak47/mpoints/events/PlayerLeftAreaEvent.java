package org.dggdak47.mpoints.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.dggdak47.mpoints.area.Area;

public class PlayerLeftAreaEvent extends PlayerEvent{
	private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
	
	
	private Area leftArea;
	
	public Area getLeftArea() {
		return this.leftArea;
	}
	
	public PlayerLeftAreaEvent(Player who, Area leftArea) {
		super(who);
		this.leftArea = leftArea;
	}
}
