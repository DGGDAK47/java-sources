package org.dggdak47.mfractions.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.dggdak47.mfractions.fraction.Fraction;

public class PlayerChangeFractionEvent extends PlayerEvent{
	private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
	//--------------------
    private Fraction newFraction;
    public Fraction getNewFraction() {
    	return this.newFraction;
    }
    
	public PlayerChangeFractionEvent(Player who, Fraction f) {
		super(who);
		this.newFraction = f;
	}
}
