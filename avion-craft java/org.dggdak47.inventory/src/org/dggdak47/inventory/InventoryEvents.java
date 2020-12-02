package org.dggdak47.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.dggdak47.inventory.InventoryHandler;
import org.dggdak47.inventory.Inv;
import java.util.List;
import org.bukkit.event.block.Action;


public class InventoryEvents implements Listener{
	
	private Inv inv;
	private JavaPlugin jp;
	
	public InventoryEvents(Inv inv, JavaPlugin jp){
		this.inv = inv;
		this.jp = jp;
	}
	
	
	private void closeInventory(List<HumanEntity> humanEntities){
		Bukkit.getScheduler().runTask(this.jp, new Runnable() {
			@Override
			public void run(){
			  try {	
				for(HumanEntity he: humanEntities){
					he.closeInventory();
				}
			  }catch(Exception e){
				  
			  }
			}
		});
	}
	
	@EventHandler
	public void onInventoryMoveItem(InventoryMoveItemEvent e) {
		if(e.getInitiator().getTitle().equals("Ваш инвентарь")){
			e.setCancelled(true);
		}else{
			return;
		}
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e){
		if(e.getInventory().getTitle().equals("Ваш инвентарь")){
			e.setCancelled(true);
		}else{
			return;
		}
	}
	
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
    	if(!e.getInventory().getTitle().equals("Ваш инвентарь")){
    		return;
    	}
    	e.setCancelled(true);
    	if(e.getViewers().size() > 1){
    		closeInventory(e.getViewers());
    		return;
    	}
    	
    	Player p = (Player)e.getViewers().get(0);
    	InventoryHandler ih = inv.getInventoryHandlerByNickname(p.getName());
    	if(ih == null){
    		closeInventory(e.getViewers());
    		return;
    	}
    	

    	
    	String itemDisplayName = "";
    	if(e.getCurrentItem() != null){
    		inv.l.info(e.getCurrentItem().getData().getItemType().toString() );
    		if(e.getCurrentItem().hasItemMeta()){
    			if(e.getCurrentItem().getItemMeta().hasDisplayName()){
        			itemDisplayName = e.getCurrentItem().getItemMeta().getDisplayName();
        		}
    		}
    	}else{
    		return;
    	}
    	 
    	if(itemDisplayName.lastIndexOf("Вперёд") != -1){
    		if(ih.changePage(true)){
    			ih.update(e.getInventory());
    			return;
    		}else{
    			inv.setOpenInfoByNickname(p.getName(), false);
    			closeInventory(e.getViewers());
    			return;
    		}
    		
    	}else if(itemDisplayName.lastIndexOf("Назад") != -1){
    		if(ih.changePage(false)){
    			ih.update(e.getInventory());
    			return;
    		}else{
    			inv.setOpenInfoByNickname(p.getName(), false);
    			closeInventory(e.getViewers());
    			return;
    		}
    	}
    	
    	ItemStack item = e.getCurrentItem();
    	
    	if(ih.hasItemCount(item)){
    		int index = inv.hasFreeSlots(p);
    		
    		if(index == -1){
    			closeInventory(e.getViewers());
    			p.sendMessage("В инвентаре недостаточно места!");
    		}else{
    			inv.give(p, ih.deducktItem(item));
    	        ih.update(e.getInventory());
    		}
    		
    	}else{
    		closeInventory(e.getViewers());
    	}
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
    	if(e.getInventory().getTitle().equals("Ваш инвентарь")){
    		inv.setOpenInfoByNickname(e.getPlayer().getName(), false);
		}else{
			return;
		}
    }
    
    public void onPlayerQuit(PlayerQuitEvent e){
    	inv.setOpenInfoByNickname(e.getPlayer().getName(), false);
    }
}
