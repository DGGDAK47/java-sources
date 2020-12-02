package org.dggdak47.eventHandling;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.dggdak47.guid.Guid;
import org.dggdak47.guid.Util;
import org.dggdak47.guid.wrapping.InventoryWrapper;
import org.dggdak47.guid.wrapping.ItemWrapper;
import org.dggdak47.guid.wrapping.RightClickWrapper;
import org.dggdak47.opening.InventoryCloser;


public class GuidEventHandler implements Listener{
	private Guid guid;
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		InventoryView iv = e.getView();
		
		if(iv == null || !iv.getType().equals(InventoryType.CHEST) ){ return; }
		//дальше проходят инвентари типа CHEST
		
		Inventory topInv = iv.getTopInventory();
		
		if(!this.guid.manager.hasInventory(topInv.getName())){ return; }
		//дальше идет код если верхний инвентарь входит в список инвентарей мэнеджера
		e.setCancelled(true);
		
		
		
		ItemStack clickedItem = e.getCurrentItem();
		String command = null;
		ItemWrapper clickedItemW = null;
		InventoryWrapper clickedInv;
		
		if(clickedItem != null && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()){
			clickedInv = this.guid.manager.getInventoryWrapper(topInv.getName());
			if(clickedInv == null){
				return;
			}
			
			clickedItemW = clickedInv.getItem(clickedItem.getItemMeta().getDisplayName());
			if(clickedItemW == null){
				return;
			}
			
			command = clickedItemW.getCommand();
		}
		 
		if(command != null){
			((Player)e.getWhoClicked()).performCommand(command);
			if(clickedItemW.closeInventoryOnClick()){
				new InventoryCloser(this.guid, (Player)e.getWhoClicked()).close();
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		Action action = e.getAction();
		
		if( !action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK) ){
			return;
		}
		//Дальше идет если это RIGHT_CLICK
		ItemStack itemInTheHand = e.getItem();
		if(itemInTheHand != null && itemInTheHand.hasItemMeta() && itemInTheHand.getItemMeta().hasDisplayName()){
			RightClickWrapper wrapperOfHandItem = this.guid.manager.getRightClickWrapper(itemInTheHand.getItemMeta().getDisplayName(), itemInTheHand.getType());
			if(wrapperOfHandItem != null){
				e.setCancelled(true);
				e.getPlayer().performCommand(wrapperOfHandItem.getCommand());
			}
		}
	}
	
	
	public GuidEventHandler(Guid guid) {
		this.guid = guid;
	}
}
