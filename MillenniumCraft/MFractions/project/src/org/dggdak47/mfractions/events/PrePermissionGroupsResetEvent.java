package org.dggdak47.mfractions.events;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PrePermissionGroupsResetEvent extends PlayerEvent{
    private static final HandlerList hl = new HandlerList();
    @Override
	public HandlerList getHandlers() {
		return hl;
	}
    public static HandlerList getHandlerList() {
        return hl;
    }
    
	private ArrayList<String> groupsToAdd = new ArrayList<String>();
    
	public void addGroup(String groupName) {
		groupsToAdd.add(groupName);
	}
	public ArrayList<String> getGroups(){
		return (ArrayList)groupsToAdd.clone();
	}
	
	
	public PrePermissionGroupsResetEvent(Player who) {
		super(who);
	}
}
