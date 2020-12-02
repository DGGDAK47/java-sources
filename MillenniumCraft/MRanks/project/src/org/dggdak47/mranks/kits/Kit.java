package org.dggdak47.mranks.kits;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.guid.wrapping.ItemWrapper;
import org.dggdak47.mranks.kits.exceptions.KitConstructorDataException;

import com.shampaggon.crackshot.CSUtility;

public class Kit {
	//KitInfo
	private String name;
	private String permission;
	private Integer id;
	//ItemsStorage
	private ArrayList<String> csItems = new ArrayList<String>();
	private ArrayList<ItemWrapper> bukkitItems = new ArrayList<ItemWrapper>();
	
	//KitInfo Methods
	public Integer getId() {
		return new Integer(this.id);
	}
	public String getPermission() {
		return this.permission;
	}
	public String getName() {
		return this.name;
	}
	
	//ItemStorage Methods
    public ArrayList<ItemStack> getContent() {
		ArrayList<ItemStack> toReturn = new ArrayList<ItemStack>();
		ItemStack item;
		CSUtility csu = new CSUtility();
		
		for(String csItem: csItems){
			item = csu.generateWeapon(csItem);
			if(item != null){
				toReturn.add(item);
			}
		}
		for(ItemWrapper iw: bukkitItems){
			if(!iw.getName().equals("null")){
				item = iw.assemble();
			}else{
				item = iw.assembleWithDefaultItemName();
			}
			toReturn.add(item);
		}
		
		return toReturn;
	}
	public ArrayList<String> getCsItems(){
		return (ArrayList)this.csItems.clone();
	}
	private void addItemToGeneralStorage(ItemType type, String itemData){
		if(type.equals(ItemType.CrackShot)){
			this.csItems.add(itemData);
		}else if(type.equals(ItemType.Bukkit)){
			itemData += "|Index:0";
			itemData += "|Command:0";
			ItemWrapper iw = new ItemWrapper(itemData);
			this.bukkitItems.add(iw);
		}
	}	
    public static ItemType getItemType(String type) {
		if(type.equals("cs")){
			return ItemType.CrackShot;
		}else if(type.equals("bk")){
			return ItemType.Bukkit;
		}else {
			return null;
		}
	}
	
	
	public Kit(Map kitMap) throws KitConstructorDataException {
		//KitInfo initializing
		Object ob = kitMap.get("Name");
		if(ob == null){
			throw new KitConstructorDataException("One of kit map has no name!");
		}else {
			this.name = (String)ob;
		}
		
		ob = kitMap.get("ID");
		if(ob == null){
			throw new KitConstructorDataException("Kit with name: "+this.name+" has no ID!");
		}else{
			try{
				id = new Integer( (String)ob );
			}catch(NumberFormatException e){
				throw new KitConstructorDataException("Kit with name: "+this.name+" has wrong ID!");
			}
		}
		
		ob = kitMap.get("Permission");
		if(ob == null){
			throw new KitConstructorDataException("Kit with name: "+this.name+" has no Permission field!");
		}else{
			this.permission = (String)ob;
		}
		
		
		//ItemsStorage initializing
		ob = kitMap.get("Items");
		if(ob == null || !(ob instanceof ArrayList)){
			throw new KitConstructorDataException("Kit with name: "+this.name+" has no Items field!");
		}
		
		ArrayList<String> items = (ArrayList<String>)ob;
		ItemType type;
		String itemData;
		ArrayList<String> splitedByFirstEntering;
		
		for(String item: items){
			splitedByFirstEntering = DUtil.splitByFirstEntering(item, '|');
			if(splitedByFirstEntering.size() < 2){
				throw new KitConstructorDataException("Item: ("+item+") has a mistake in its parameters!");
			}
			
			type = Kit.getItemType(splitedByFirstEntering.get(0));
			if(type == null){
				throw new KitConstructorDataException("Item: ("+item+") has a wrong item type!");
			}
			
			itemData = splitedByFirstEntering.get(1);
			
			try{
				addItemToGeneralStorage(type, itemData);
			}catch(ClassCastException e){
				throw new KitConstructorDataException("Item: ("+item+") has a mistake in its parameters!");
			}catch(NullPointerException e){
				throw new KitConstructorDataException("Item: ("+item+") has unfilled field!");
			}
			
			
		}
	}
}
