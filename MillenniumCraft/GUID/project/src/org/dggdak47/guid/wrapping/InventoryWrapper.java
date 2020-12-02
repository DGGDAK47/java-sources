package org.dggdak47.guid.wrapping;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.dggdak47.substitution.Substitution;

public class InventoryWrapper {
	
	private String name;
	private int id;
	private int size;
	private ArrayList<ItemWrapper> items;
	
	
	
	
	public boolean hasItem(String itemName){
		for(ItemWrapper iw: items){
			if(iw.getName().equals(itemName)){
				return true;
			}
		}
		return false;
	}
	public ArrayList<ItemWrapper> getItems(){
		ArrayList<ItemWrapper> toReturn = new ArrayList<ItemWrapper>();
		
		for(ItemWrapper item: items){
			toReturn.add(item.clone());
		}
		
		return toReturn;
	}
	public String getCommand(String itemName){
		for(ItemWrapper iw: items){
			if(iw.getName().equals(itemName)){
				String command = iw.getCommand();
				if(command.equals("null")){
					return null;
				}else {
					return command;
				}
			}
		}
		return null;
	}
	
	public ItemWrapper getItem(String itemName){
		for(ItemWrapper iw: items){
			if(iw.getName().equals(itemName)){
				return iw;
			}

		}
		return null;
	}
	
	public Integer getID() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public Integer getSize() {
		return this.size;
	}
	
	public InventoryWrapper clone() {
		Hashtable ht = new Hashtable();
		
		ht.put("Name", this.name);
		ht.put("ID", this.id);
		ht.put("Items", this.getItems());
		
		return new InventoryWrapper(ht);
	}
	public Inventory assemble(){
		Inventory i = Bukkit.createInventory(null, this.size, this.name);
		
		for(ItemWrapper item: items){
			i.setItem(item.getIndex() , item.assemble());
		}
		
		return i;
	}
	public Inventory assemble(ArrayList<Substitution> substitutions){
		Inventory inv = Bukkit.createInventory(null, this.size, this.name);
		
		if(substitutions == null || substitutions.size() == 0){
			return assemble();
		}
		
		Substitution s;
		for(ItemWrapper item: items){
			for(int i = 0; i < substitutions.size(); i++){
				s = substitutions.get(i);
				
				if(item.getIndex().compareTo(s.index) == 0){
					ItemStack item2 = item.assemble();
					Substitution.setSubstitution(item2, s);
					inv.setItem(item.getIndex(), item2);
					break;
				}else if(substitutions.size()-1 == i){
					inv.setItem(item.getIndex(), item.assemble());
				}
			}
			
		}
		
		return inv;
	}
	
	public InventoryWrapper(Map inventory) {
		Object items = inventory.get("Items");
		
		if(items != null){
			if( ((ArrayList)items).get(0) instanceof String){
				this.items = ItemWrapper.toItemWrapper(  (ArrayList<String>)items );
			}else {
				this.items = (ArrayList<ItemWrapper>)items;
			}
		}else{
			this.items = new ArrayList<ItemWrapper>();
		}
		
		
		this.id = new Integer((String)inventory.get("ID"));
		this.name = (String)inventory.get("Name");
		this.size = new Integer((String)inventory.get("Size"));
		
	}
	public InventoryWrapper(Map config, ArrayList<ItemWrapper> content) {
		//config -> Name, ID, Size
		
		this.items = content;
		this.name = (String)config.get("Name");
		this.id = new Integer( (String)config.get("ID"));
		this.size = new Integer( (String)config.get("Size") );
	}
}
