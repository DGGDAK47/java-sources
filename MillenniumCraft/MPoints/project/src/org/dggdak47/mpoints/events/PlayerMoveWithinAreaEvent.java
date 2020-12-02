package org.dggdak47.mpoints.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.dggdak47.mpoints.area.Area;

public class PlayerMoveWithinAreaEvent extends PlayerEvent{
	private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
	
	
	private Area area;
	
	public Area getArea() {
		return this.area;
	}
	
	public PlayerMoveWithinAreaEvent(Player who, Area area) {
		super(who);
		this.area = area;
	}
}
