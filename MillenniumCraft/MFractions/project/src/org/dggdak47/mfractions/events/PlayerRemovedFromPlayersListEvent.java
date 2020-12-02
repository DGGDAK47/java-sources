package org.dggdak47.mfractions.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerRemovedFromPlayersListEvent extends PlayerEvent{
	private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
	
	public PlayerRemovedFromPlayersListEvent(Player player) {
		super(player);
	}

	
	
}
