package org.dggdak47.guid;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.dggdak47.assembling.Assembler;
import org.dggdak47.guid.exceptions.InventoryWithSameDataExistsException;
import org.dggdak47.guid.exceptions.SuchInventoryNoExistsException;
import org.dggdak47.guid.wrapping.InventoryWrapper;
import org.dggdak47.guid.wrapping.RightClickWrapper;
import org.dggdak47.opening.InventoryOpener;
import org.dggdak47.substitution.Substitution;

public class Manager {
	private Map config;
	private Guid guid;
	private ArrayList<InventoryWrapper> inventories;
	private ArrayList<InventoryWrapper> additedInventories;
	private ArrayList<InventoryWrapper> createdInventoryies;
	private ArrayList<RightClickWrapper> rightClickItems;
	
	public boolean hasCreatedInventory(Integer invId) {
		for(InventoryWrapper iw: createdInventoryies){
			if(iw.getID().equals(invId)){
				return true;
			}
		}
		return false;
	}
	public boolean hasInventory(String invName){
		ArrayList<InventoryWrapper> allInventories = getAllInventories();
		
		for(InventoryWrapper inv: allInventories){
			if(inv.getName().equals(invName)){
				return true;
			}
		}
		
		return false;
	}
    public boolean hasInventory(Integer invID){
        ArrayList<InventoryWrapper> allInventories = getAllInventories();
		
		for(InventoryWrapper inv: allInventories){
			if(inv.getID().equals(invID)){
				return true;
			}
		}
    	
    	
    	return false;
	}
	public Inventory getInventory(String invName){
        ArrayList<InventoryWrapper> allInventories = getAllInventories();
		
		for(InventoryWrapper inv: allInventories){
			if(inv.getName().equals(invName)){
				return inv.assemble();
			}
		}
		
		return null;
	}
    public Inventory getInventory(Integer invID){
        ArrayList<InventoryWrapper> allInventories = getAllInventories();
		
		for(InventoryWrapper inv: allInventories){
			if(inv.getID().equals(invID)){
				return inv.assemble();
			}
		}
    	
		return null;
	}
    public Inventory getInventory(Integer invID, ArrayList<Substitution> substitutions) {
        ArrayList<InventoryWrapper> allInventories = getAllInventories();
		
		for(InventoryWrapper inv: allInventories){
			if(inv.getID().equals(invID)){
				return inv.assemble(substitutions);
			}
		}
    	
		return null;
    }
    public void openInventory(String invName, Player p){
    	new InventoryOpener(this.guid, p, invName).open();
    }
    public void openInventory(Integer invID, Player p){
    	new InventoryOpener(this.guid, p, invID).open();
    }
    public void openInventory(Integer invID, Player p, ArrayList<Substitution> substitutions){
    	new InventoryOpener(this.guid, p, invID, substitutions).open();
    }
    public String getCommand(String invName, String itemName){
    	return getInventoryWrapper(invName).getCommand(itemName);
    }
    public String getCommand(Integer invID, String itemName){
    	return getInventoryWrapper(invID).getCommand(itemName);
    }
    public InventoryWrapper getInventoryWrapper(String invName) {
        ArrayList<InventoryWrapper> allInventories = getAllInventories();
		
		for(InventoryWrapper inv: allInventories){
			if(inv.getName().equals(invName)){
				return inv;
			}
		}
		
    	return null;
    }
    public InventoryWrapper getInventoryWrapper(Integer invID) {
        ArrayList<InventoryWrapper> allInventories = getAllInventories();
		
		for(InventoryWrapper inv: allInventories){
			if(inv.getID().equals(invID)){
				return inv;
			}
		}
    	
    	return null;
    }
    public boolean hasRightClickItem(String itemName, Material itemMaterial) {
    	for(RightClickWrapper iw: rightClickItems){
    		if(iw.getName().equals(itemName) && iw.getMaterial().equals(itemMaterial)){
    			return true;
    		}
    	}
    	
    	return false;
    }
    public boolean hasRightClickItem(Integer id) {
    	for(RightClickWrapper iw: rightClickItems){
    		if(iw.getID().equals(id)){
    			return true;
    		}
    	}
    	
    	return false;
    }
    public RightClickWrapper getRightClickWrapper(String itemName, Material itemMaterial) {
    	for(RightClickWrapper iw: rightClickItems){
    		if(iw.getName().equals(itemName) && iw.getMaterial().equals(itemMaterial)){
    			return iw;
    		}
    	}
    	
    	return null;
    }
    public RightClickWrapper getRightClickWrapperByID(Integer id){
    	for(RightClickWrapper iw: rightClickItems){
    		if(iw.getID().equals(id)){
    			return iw;
    		}
    	}
    	
    	return null;
    }
    
    private ArrayList<InventoryWrapper> getAllInventories(){
    	ArrayList<InventoryWrapper> toReturn = new ArrayList<InventoryWrapper>();
    	
    	toReturn.addAll(this.inventories);
    	toReturn.addAll(this.additedInventories);
    	toReturn.addAll(this.createdInventoryies);
    	
    	return toReturn;
    }
    private void addWrappers()  throws InventoryWithSameDataExistsException{
    	this.additedInventories = new ArrayList<InventoryWrapper>();
    	
    	ArrayList<String> toAddition = (ArrayList<String>)this.config.get("Addition");
    	if(toAddition != null){
			Integer id1;
			Integer id2;
			String name;
			String invID;
			ArrayList<String> splitedData;
			
			for(String inv: toAddition){
				splitedData = Util.split(inv, '|');
				
				id1 = new Integer( splitedData.get(0) );
				id2 = new Integer( splitedData.get(1) );
				name = splitedData.get(2);
				invID = splitedData.get(3);
				
				if(hasInventory(invID) || hasInventory(name)){
					throw new InventoryWithSameDataExistsException();
				}else if(!hasInventory(id1) || !hasInventory(id2)){
					continue;
				}
				
				additedInventories.add(Assembler.addition(getInventoryWrapper(id1), getInventoryWrapper(id2), name, invID ));
			}
		}
    	
    }
	public void createNewInventory(InventoryWrapper iw){
		this.createdInventoryies.add(iw);
		
		try {
			addWrappers();
		} catch (InventoryWithSameDataExistsException e) {
			e.printStackTrace();
		}
	}
	public void removeInventory(Integer id) {
		if(!hasCreatedInventory(id)){
			return;
		}
		
		InventoryWrapper iw = getInventoryWrapper(id);
		this.createdInventoryies.remove(iw);
	}
	
    protected ArrayList<InventoryWrapper> getCreatedInventories(){
    	return this.createdInventoryies;
    }
	
	public Manager(Guid guid, Map pluginConfig) throws InventoryWithSameDataExistsException{
		//1
		this.guid = guid;
		this.config = pluginConfig;
		
		createdInventoryies = new ArrayList<InventoryWrapper>();
		inventories = new ArrayList<InventoryWrapper>();
		rightClickItems = new ArrayList<RightClickWrapper>();
		
		Map inventoriesMap = (Map)pluginConfig.get("Inventories");
		Set keys = inventoriesMap.keySet();
		Map inventory;
		for(Object key: keys){
			inventory = (Map)inventoriesMap.get(key);
			
			this.inventories.add(new InventoryWrapper(inventory) );
		}
		
		//2
		addWrappers();
		
		//3
		ArrayList<String> rightClickItems = (ArrayList<String>)pluginConfig.get("RightClickActions");
		if(rightClickItems != null){
			for(String config: rightClickItems){
				this.rightClickItems.add( new RightClickWrapper(config) );
			}
		}
	}
	public Manager(Guid guid, Map pluginConfig, ArrayList<InventoryWrapper> createdInventories) throws InventoryWithSameDataExistsException{
		//1
		this.guid = guid;
		this.config = pluginConfig;
				
		this.createdInventoryies = createdInventories;
		inventories = new ArrayList<InventoryWrapper>();
		rightClickItems = new ArrayList<RightClickWrapper>();
				
		Map inventoriesMap = (Map)pluginConfig.get("Inventories");
		Set keys = inventoriesMap.keySet();
		Map inventory;
		for(Object key: keys){
			inventory = (Map)inventoriesMap.get(key);
					
			this.inventories.add(new InventoryWrapper(inventory) );
		}
				
		//2
		addWrappers();
				
		//3
		ArrayList<String> rightClickItems = (ArrayList<String>)pluginConfig.get("RightClickActions");
		if(rightClickItems != null){
			for(String config: rightClickItems){
				this.rightClickItems.add( new RightClickWrapper(config) );
			}
		}
	}
	
}
