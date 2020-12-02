package org.dggdak47.opening;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.dggdak47.guid.Guid;
import org.dggdak47.substitution.Substitution;

public class InventoryOpener implements Runnable{
	private Player p;
	private Guid guid;
	private String nameToOpen = null;
	private Integer idToOpen = null;
	private ArrayList<Substitution> substitutions = null;
	
	public InventoryOpener(Guid guid, Player p, String invName){
		this.p = p;
		this.guid = guid;
		this.nameToOpen = invName;
	}
	public InventoryOpener(Guid guid, Player p, Integer idToOpen){
		this.p = p;
		this.guid = guid;
		this.idToOpen = idToOpen;
	}
	public InventoryOpener(Guid guid, Player p, Integer idToOpen, ArrayList<Substitution> substitutions) {
		this.p = p;
		this.guid = guid;
		this.idToOpen = idToOpen;
		this.substitutions = substitutions;
	}
	
	
	public void open(){
		Bukkit.getScheduler().runTask(this.guid, this);
	}
	
	@Override
	public void run(){
		if(p.getOpenInventory() != null){
			p.closeInventory();
		}
		
		if(substitutions == null && (nameToOpen == null) ){
			p.openInventory( this.guid.manager.getInventory(this.idToOpen));
		}else if(substitutions != null ){
			p.openInventory( this.guid.manager.getInventory(this.idToOpen, substitutions) );
		}else {
			p.openInventory( this.guid.manager.getInventory(this.nameToOpen) );
		}
	}
}
