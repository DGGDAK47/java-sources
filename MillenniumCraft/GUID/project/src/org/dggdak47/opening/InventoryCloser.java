package org.dggdak47.opening;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.dggdak47.guid.Guid;

public class InventoryCloser implements Runnable{
	private Player p;
	private Guid guid;
	
	public InventoryCloser(Guid guid, Player p){
		this.p = p;
		this.guid = guid;
	}
	
	public void close() {
		Bukkit.getScheduler().runTask(this.guid, this);
	}
	
	@Override
	public void run() {
		if(p.getOpenInventory() != null){
			p.closeInventory();
		}
	}
}
