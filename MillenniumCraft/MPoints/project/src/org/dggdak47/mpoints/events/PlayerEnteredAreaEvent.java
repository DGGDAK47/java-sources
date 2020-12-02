package org.dggdak47.mpoints.events;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.dggdak47.mpoints.area.Area;

public class PlayerEnteredAreaEvent extends PlayerEvent{
	private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
	
	
	
	private Area previousArea;
	private Area enteredArea;
	
	public Area getPreviousArea() {
		return this.previousArea;
	}
	public Area getEnteredArea(){
		return this.enteredArea;
	}
	
	public PlayerEnteredAreaEvent(Player who, Area enteredArea, Area previousArea){
		super(who);
		this.previousArea = previousArea;
		this.enteredArea = enteredArea;
	}
}
