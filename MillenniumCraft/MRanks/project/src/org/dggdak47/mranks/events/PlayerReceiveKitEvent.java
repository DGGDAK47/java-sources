package org.dggdak47.mranks.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.dggdak47.mranks.kits.Kit;

public class PlayerReceiveKitEvent extends PlayerEvent{
	private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
	//------------------------
    private Kit kit;
    public Kit getKit() {
    	return this.kit;
    }
    
	public PlayerReceiveKitEvent(Player who, Kit kit) {
		super(who);
		this.kit = kit;
	}
}
