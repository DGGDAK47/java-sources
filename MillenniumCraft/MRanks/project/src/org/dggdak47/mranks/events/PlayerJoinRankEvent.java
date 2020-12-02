package org.dggdak47.mranks.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.dggdak47.mranks.ranks.Rank;

public class PlayerJoinRankEvent extends PlayerEvent{
	private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
	//----------------
    private Rank rank;
    public Rank getRank() {
    	return this.rank;
    }
    
	public PlayerJoinRankEvent(Player who, Rank rank) {
		super(who);
		this.rank = rank;
	}
}
