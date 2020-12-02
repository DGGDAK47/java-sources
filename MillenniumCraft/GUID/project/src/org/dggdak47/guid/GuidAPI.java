package org.dggdak47.guid;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.dggdak47.guid.wrapping.InventoryWrapper;
import org.dggdak47.guid.wrapping.RightClickWrapper;
import org.dggdak47.opening.InventoryCloser;
import org.dggdak47.substitution.Substitution;

public class GuidAPI {
	private Guid guid;
	
	public boolean hasInventory(Integer invID) {
		return this.guid.manager.hasInventory(invID);
	}
	public void openInventory(Integer invID, Player p) {
		this.guid.manager.openInventory(invID, p);
	}
	public void openInventory(Integer invID, Player p, ArrayList<Substitution> substitutions){
		this.guid.manager.openInventory(invID, p, substitutions);
	}
	public InventoryWrapper getInventoryWrapper(Integer id) {
		return this.guid.manager.getInventoryWrapper(id);
	}
	public RightClickWrapper getRightClickWrapperById(Integer id){
		return this.guid.manager.getRightClickWrapperByID(id);
	}
	public boolean hasRightClickItem(Integer id) {
		return this.guid.manager.hasRightClickItem(id);
	}
	public void createInventory(InventoryWrapper iw){
		this.guid.manager.createNewInventory(iw);
	}
	public void closeInventoryAsynch(Player p){
		new InventoryCloser(this.guid, p).close();
	}
	public void removeCreatedInventory(Integer invId) {
		this.guid.manager.removeInventory(invId);
	}
	
	public GuidAPI(Guid guid){
		this.guid = guid;
	}
}
