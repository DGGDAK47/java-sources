package org.dggdak47.inventory;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Material;

public class EnchantmentStorage {

	public static class EnchantedItem{
		private String eventInfo;
		private ItemStack item;
		
		public ItemStack getItem(){
			return this.item;
		}
		public String getEventInfo(){
			return this.eventInfo;
		}
		
		public EnchantedItem(String eventInfo, String name, Map<Enchantment,Integer> enchantments){
			this.eventInfo = eventInfo;
			
			String material = eventInfo.split("-")[0];
			
			ItemStack is = new ItemStack(Material.valueOf(material));
			is.addEnchantments(enchantments);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(name);
			is.setItemMeta(im);
			
			this.item = is;
		}
	}
    
	private static ArrayList<EnchantedItem> enchantedItems = new ArrayList<EnchantedItem>();
	
	static{
		Hashtable<Enchantment, Integer> PVPSword = new Hashtable<Enchantment, Integer>();
		PVPSword.put(Enchantment.FIRE_ASPECT , 1);
		PVPSword.put(Enchantment.DAMAGE_ALL, 5);
        enchantedItems.add(new EnchantedItem("DIAMOND_SWORD-PVP", "PVP-Μεχ", PVPSword));
        
	}
	
   
	public static boolean compareEnchantedItems(ItemStack firstItem, ItemStack secondItem){
		ItemMeta im1 = null;
		ItemMeta im2 = null;
		
		if(firstItem.hasItemMeta()){
			im1 = firstItem.getItemMeta();
		}
		if(secondItem.hasItemMeta()){
			im2 = secondItem.getItemMeta();
		}
		
		
		if(!(im1 != null && im2 != null)){
		   	return false;
		}
		
		
		if(im1.hasDisplayName() && im2.hasDisplayName()){
			if(im1.getDisplayName().equals(im2.getDisplayName())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	public static String getEventInfoByItemStack(ItemStack is, Logger l){
		for(EnchantedItem ei: enchantedItems){
			if(compareEnchantedItems(is, ei.getItem())){
				 return ei.getEventInfo();
			}
		}
		return "";
	}
	public static ItemStack getItemByEventInfo(String eventInfo){
		for(EnchantedItem ei: enchantedItems){
			 if(eventInfo.equals(ei.getEventInfo())){
				 return ei.getItem().clone();
			 }
		}
		
		ItemStack is = new ItemStack(Material.ANVIL);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("ERROR");
		is.setItemMeta(im);
		
		return is;
	}
}
