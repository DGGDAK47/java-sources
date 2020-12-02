package org.dggdak47.mfractions.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FullPluginReloadEvent extends Event{
	private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
}
//5469 5200 1452 7659