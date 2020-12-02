package org.dggdak47.substitution;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.dggdak47.guid.Util;

public class Substitution {
	public Integer index;
	
	public Material material = null;
	public ArrayList<String> lore = null;
	public Integer amount = null;
	public ArrayList<ItemFlag> flags = null;
	public Hashtable<Enchantment, Integer> enchantments = null;
	public boolean isEnchantmentUnsafe = false;
	
	public static void setSubstitution(ItemStack item, Substitution s){
		ItemMeta im = item.getItemMeta();
		
		if(s.material != null){
			item.setType(s.material);
		}
		if(s.lore != null){
			im.setLore(s.lore);
		}
		if(s.amount != null){
			if(s.amount > 64){
				item.setAmount(64);
			}else{
				item.setAmount(s.amount);
			}
		}
		
		if(s.flags != null){
			for(ItemFlag iFlag: s.flags){
				im.addItemFlags(iFlag);
			}
		}
		
		if(s.enchantments != null){
			Set<Enchantment> keys = s.enchantments.keySet();
			for(Enchantment e: keys){
				im.addEnchant(e, s.enchantments.get(e), s.isEnchantmentUnsafe);
			}
		}
		
		item.setItemMeta(im);
	}
	
	public Substitution(String toSubstitute){
		//Index:0|Material:SKULL|Lore:l1~l2…ln|Amount:10|Flags:HIDE_ATTRIBUTES|Enchants [unsafe]:DIG_SPEED 1
		
		
		ArrayList<String> pairs = Util.split(toSubstitute, '|');
		//'Index:0'    'Material:SKULL'  'Lore:l1~l2...~ln' 
		
		Hashtable<String, String> splitedPairs = new Hashtable<String, String>();
		
		String name;
		String value;
		ArrayList<String> pairA;
		for(String pair: pairs){
			pairA = Util.split(pair, ':');
			name = pairA.get(0);
			value = pairA.get(1);
			
			splitedPairs.put(name, value);
		}
		//splitedPairs
	    //Index:0   Material:SKULL   Lore:l1~l2...~ln
		
		//Index
		this.index = new Integer(splitedPairs.get("Index"));
		
		//Material
		if(splitedPairs.get("Material") != null){
			this.material = Material.valueOf( splitedPairs.get("Material") );
		}
		
		
		//Lore
		if(splitedPairs.get("Lore") != null ){
			this.lore = Util.split(splitedPairs.get("Lore"), '~');
		}
		
		//amount
		if(splitedPairs.get("Amount") != null){
			this.amount = new Integer( splitedPairs.get("Amount") );
		}
		
		//flags
		if(splitedPairs.get("Flags") != null){
			ArrayList<String> flags = Util.split(splitedPairs.get("Flags"), '~');
			
			this.flags = new ArrayList<ItemFlag>();
			for(String flag: flags){
				this.flags.add( ItemFlag.valueOf(flag) );
			}
		}
		
		//enchantments
		if(splitedPairs.get("Enchants") != null || splitedPairs.get("Enchants unsafe") != null){
			ArrayList<String> enchantments;
			this.enchantments = new Hashtable<Enchantment, Integer>();
			
			
			//'DIG_SPEED 1~ALALA 10'
			String enchantmentsS;
			
			if(splitedPairs.get("Enchants unsafe") != null){
				this.isEnchantmentUnsafe = true;
				enchantmentsS = splitedPairs.get("Enchants unsafe");
			}else{
				enchantmentsS = splitedPairs.get("Enchants");
			}
			
			
			enchantments = Util.split(enchantmentsS, '~');
			//'DIG_SPEED 1'    'ALALA 10'
			
			Enchantment e;
			Integer eValue;
			ArrayList<String> splitedEnchantment;
			
			for(String enchantment: enchantments){
				splitedEnchantment = Util.split(enchantment, ' ');
				
				e = Enchantment.getByName(splitedEnchantment.get(0));
				eValue = new Integer(splitedEnchantment.get(1));
				
				this.enchantments.put(e, eValue);
			}
		}
	}
}
