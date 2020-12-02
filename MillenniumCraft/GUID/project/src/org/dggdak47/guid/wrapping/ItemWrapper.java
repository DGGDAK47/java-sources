package org.dggdak47.guid.wrapping;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.dggdak47.guid.Util;
import org.dggdak47.guid.exceptions.ConfigHasNoRequierPropertyException;
import org.dggdak47.guid.wrapping.exceptions.ItemConfigDataException;



public class ItemWrapper {
	private String name;
	private Material material;
	private String command;
	private Integer index;
	
	private String configuration;
	private ArrayList<String> lore = null;
	private Integer amount = null;
	private ArrayList<ItemFlag> flags = null;
	private Hashtable <Enchantment, Integer> enchantments = null;
	private boolean isEnchantmentsUnsafe = false;
	private boolean closeInventoryOnClick = false;
	
	public void show() {
		Logger l = Bukkit.getLogger();
		l.info("=========================");
		l.info("Name: "+this.name);
		l.info("Material: "+this.material.toString());
		l.info("Command: "+this.command);
		l.info("Index: "+this.index);
		l.info("Lore: "+this.lore);
		l.info("Amount: "+this.amount);
		l.info("Flags: "+this.flags);
		l.info("Enchantments: "+this.enchantments);
		l.info("isEnchantmentUnsafe: "+this.isEnchantmentsUnsafe);
	}
	
	public static Hashtable<String, Object> unwrap(String itemConfiguration) throws ConfigHasNoRequierPropertyException{
		Hashtable<String, Object> itemProperties = new Hashtable<String, Object>();
		
		//1
		ArrayList<String> properties = Util.split(itemConfiguration, '|');
		
		//2
		Hashtable<String, String> propertiesHt = new Hashtable<String, String>();
		
		ArrayList<String> splitedProperty;
		for(String property: properties){
			splitedProperty = Util.split(property, ':');
			propertiesHt.put(splitedProperty.get(0), splitedProperty.get(1) );
		}
		
		//3
		String str1;
		ArrayList<String> al1;
		ArrayList<String> al2;
		
		//3.1
		str1 = propertiesHt.get("Name");
		if(str1 == null){
			throw new ConfigHasNoRequierPropertyException();
		}else{
			itemProperties.put("Name", str1);
		}
		
		//index
		str1 = propertiesHt.get("Index");
		if(str1 == null){
			throw new ConfigHasNoRequierPropertyException();
		}else {
			itemProperties.put("Index", new Integer(str1) );
		}
		
		//3.2
		str1 = propertiesHt.get("Material");
		if(str1 == null){
			throw new ConfigHasNoRequierPropertyException();
		}else {
			Material material = Material.valueOf(str1);
			itemProperties.put("Material", material);
		}
		
		//3.3
		str1 = propertiesHt.get("Command");
		if(str1 == null){
			throw new ConfigHasNoRequierPropertyException();
		}else {
			itemProperties.put("Command", str1);
		}
		
		//3.4
		str1 = propertiesHt.get("Amount");
		if(str1 != null){
			itemProperties.put("Amount", new Integer(str1) );
		}
		
		//3.5
		str1 = propertiesHt.get("Lore");
		if(str1 != null){
			al1 = Util.split(str1, '~');
			itemProperties.put("Lore", al1);
		}
		
		//3.6
		str1 = propertiesHt.get("Flags");
		if(str1 != null){
			al1 = Util.split(str1, '~');
			ArrayList<ItemFlag> al3 = new ArrayList<ItemFlag>();
			
			for(String s: al1){
				al3.add(ItemFlag.valueOf(s));
			}
			itemProperties.put("Flags", al3);
		}
		//3.7
		String key = "Enchants";
		Hashtable<Enchantment, Integer> ht1;
		str1 = propertiesHt.get(key);
		if(str1 == null){
			key = "Enchants unsafe";
			str1 = propertiesHt.get(key);
			
			if(str1 != null){
				ht1 = new Hashtable<Enchantment, Integer>();
				al1 = Util.split(str1, '~');
				for(String s: al1){
					al2 = Util.split(s, ' ');
					ht1.put( Enchantment.getByName(al2.get(0))  , new Integer(al2.get(1)) );
				}
				
				itemProperties.put(key, ht1);
			}
		}
		
		
		return itemProperties;
	}
	
	public static ArrayList<ItemWrapper> toItemWrapper(ArrayList<String> items){
		ArrayList<ItemWrapper> toReturn = new ArrayList<ItemWrapper>();
		
		for(String s: items){
			toReturn.add(new ItemWrapper(s));
		}
		
		return toReturn;
	}
	
	public ItemStack assemble(){
		ItemStack is = new ItemStack(this.material, this.amount);
		ItemMeta im = is.getItemMeta();
		
		im.setDisplayName(this.name);
		if(this.lore != null){
			im.setLore(this.lore);
		}
		
		if(this.flags != null){
			for(ItemFlag iFlag: this.flags){
				im.addItemFlags(iFlag);
			}
		}
		
		is.setItemMeta(im);
		
		if(this.enchantments != null){
			if(this.isEnchantmentsUnsafe){
				is.addUnsafeEnchantments(this.enchantments);
			}else{
				is.addEnchantments(this.enchantments);
			}
		}
		
		
		return is;
	}
	public ItemStack assembleWithDefaultItemName(){
		ItemStack is = new ItemStack(this.material, this.amount);
		ItemMeta im = is.getItemMeta();
		
		if(this.lore != null){
			im.setLore(this.lore);
		}
		
		if(this.flags != null){
			for(ItemFlag iFlag: this.flags){
				im.addItemFlags(iFlag);
			}
		}
		
		is.setItemMeta(im);
		
		if(this.enchantments != null){
			if(this.isEnchantmentsUnsafe){
				is.addUnsafeEnchantments(this.enchantments);
			}else{
				is.addEnchantments(this.enchantments);
			}
		}
		
		
		return is;
	}
    public ItemWrapper clone() {
    	Hashtable<String, Object> configCopy = new Hashtable<String, Object>();
    	configCopy.put("Name", this.name);
    	configCopy.put("Command", this.command);
    	configCopy.put("Index", this.index);
    	configCopy.put("Amount", this.amount);
    	configCopy.put("UnsafeEnchantments", this.isEnchantmentsUnsafe);
    	configCopy.put("CloseOnClick", this.closeInventoryOnClick);
    	configCopy.put("Material", this.material);
    	if(this.lore != null){
    		configCopy.put("Lore", (ArrayList)this.lore.clone());
    	}
    	if(this.flags != null){
    		configCopy.put("Flags", (ArrayList)this.flags.clone());
    	}
    	if(this.enchantments != null){
    		configCopy.put("Enchantments", (Hashtable)this.enchantments.clone());
    	}
    	
		return new ItemWrapper(configCopy);
	}
	
	public Integer getIndex() {
		return this.index;
	}
	public String getName() {
		return this.name;
	}
	public String getCommand() {
		return this.command;
	}
	public Material getMaterial() {
		return this.material;
	}
	public String getItemConfiguration() {
		return this.configuration;
	}
	public Integer getAmount() {
		return this.amount;
	}
	public void setLore(ArrayList<String> newLore) {
		this.lore = newLore;
	}
	public ArrayList<String> getLore() {
		return this.lore;
	}
	
	public boolean closeInventoryOnClick() {
		return this.closeInventoryOnClick;
	}
	
	public void nullChecker(Object ob, String message) {
		if(ob == null){
			throw new ItemConfigDataException(message);
		}
	}
	public ItemWrapper(String itemConfiguration) {
		this.configuration = itemConfiguration;
		
		Hashtable<String, Object> properties = null;
		try{
			properties = unwrap(itemConfiguration);
		}catch(Exception e){
			e.printStackTrace();
		}
		this.name = (String)properties.get("Name");
		this.material = (Material)properties.get("Material");
		this.command = (String)properties.get("Command");
		this.index = (Integer)properties.get("Index");
		
		Object ob;
		
		ob = properties.get("Amount");
		if(ob != null){
			if((Integer)ob > 64){
				this.amount = 64;
			}else{
				this.amount = (Integer)ob;
			}
		}else {
			this.amount = 1;
		}
		
		ob = properties.get("Lore");
		if(ob != null){
			this.lore = (ArrayList<String>)ob;
		}
		
		ob = properties.get("Flags");
		if(ob != null){
			this.flags = (ArrayList<ItemFlag>)ob;
		}
		
		ob = properties.get("Enchants");
		if(ob == null){
			ob = properties.get("Enchants unsafe");
			if(ob != null){
				isEnchantmentsUnsafe = true;
				this.enchantments = (Hashtable<Enchantment, Integer>) ob;
			}
		}else{
			this.enchantments = (Hashtable<Enchantment, Integer>) ob;
		}
		
		ob = properties.get("CloseOnClick");
		if(ob != null){
			this.closeInventoryOnClick = new Boolean( (String)ob );
		}
	}
	public ItemWrapper(Map<String, Object> itemConfiguration) {
		Object ob;
		
		//Name (String)
		ob = itemConfiguration.get("Name");
		nullChecker(ob, "Name");
		this.name = (String)ob;
		
		//Material (Material)
		ob = itemConfiguration.get("Material");
		nullChecker(ob, "Item wtih name: "+this.name+" has no Material property");
		this.material = (Material)ob;
		
		//Command (String)
		ob = itemConfiguration.get("Command");
		nullChecker(ob, "Item wtih name: "+this.name+" has no Command property");
		this.command = (String)ob;
		
		//Index (Integer)
		ob = itemConfiguration.get("Index");
		nullChecker(ob, "Item wtih name: "+this.name+" has no Index property");
		this.index = (Integer)ob;
		
		//Not Requiring
		//Lore (ArrayList<String>)
		ob = itemConfiguration.get("Lore");
		if(ob != null && (ob instanceof ArrayList)){
			try{
				this.lore = (ArrayList)ob;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//Amount (Integer)
		ob = itemConfiguration.get("Amount");
		if(ob != null){
			try {
				this.amount = (Integer)ob;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			this.amount = 1;
		}
		
		//Flags (ArrayList<ItemFlag>)
		ob = itemConfiguration.get("Flags");
		if(ob != null){
			try{
				this.flags = (ArrayList)ob;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//Enchantments (Hashtable<Enchantment, Integer>)
		ob = itemConfiguration.get("Enchantments");
		if(ob != null){
			try {
				this.enchantments = (Hashtable)ob;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		//UnsafeEnchantments (Boolean)
		ob = itemConfiguration.get("UnsafeEnchantments");
		if(ob != null){
			try{
				this.isEnchantmentsUnsafe = (Boolean)ob;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//CloseOnClick (Boolean)
		ob = itemConfiguration.get("CloseOnClick");
		if(ob != null){
			try {
				this.closeInventoryOnClick = (Boolean)ob;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
