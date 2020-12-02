package org.dggdak47.mfractions.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PreRespawningEvent extends PlayerEvent{
	private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
	//-----------------------
    private Location newLocation = null;
    
    public void setNewLocation(Location newLoc) {
    	this.newLocation = newLoc;
    }
    public Location getNewLocation() {
    	return this.newLocation;
    }
    
	public PreRespawningEvent(Player who) {
		super(who);
	}
	
}
