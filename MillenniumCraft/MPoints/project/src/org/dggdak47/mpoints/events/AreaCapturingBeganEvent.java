package org.dggdak47.mpoints.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.dggdak47.mpoints.area.Area;

public class AreaCapturingBeganEvent extends Event{
	private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
	//----------------------
    private Area area;
    
    public Area getCapturingArea() {
    	return this.area;
    }
    
    public AreaCapturingBeganEvent(Area area) {
    	this.area = area;
    }
}
