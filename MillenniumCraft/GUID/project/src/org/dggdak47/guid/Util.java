package org.dggdak47.guid;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;



public class Util {
	public static boolean hasPermissionPEX(Player p, String perm){
		PermissionUser pu = PermissionsEx.getUser(p);
		return pu.has(perm);
	}
	public static boolean isInventoriesSame(Inventory inv1, Inventory inv2) {
		
		ItemStack[] content1 = inv1.getContents();
		ItemStack[] content2 = inv2.getContents();
		
		if(content1.length != content2.length){
			return false;
		}else{
			ItemStack item1;
			ItemStack item2;
			for(int i = 0; i < content1.length; i++){
				item1 = content1[i];
				item2 = content2[i];
				
				if(!item1.isSimilar(item2)){
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static ArrayList<String> split(String toSplit, Character separator) {
		ArrayList<String> toReturn = new ArrayList<String>();
		
		String element = "";
		Character ch;
		
		for(int i = 0; i < toSplit.length(); i++) {
			ch = toSplit.charAt(i);
			
			if(ch.equals(separator)){
				toReturn.add(element);
				element = "";
				continue;
			}else if(toSplit.length()-1 == i){
				element += ch;
				toReturn.add(element);
				continue;
			}else{
				element += ch;
			}
		}
		
		return toReturn;
	}
	public static void showContent(Inventory i) {
		Logger l = Bukkit.getLogger();
		
		l.info("------------"+i.getName()+"------------");
		ItemStack[] content = i.getContents();
		
		String name;
		Material m;
		Integer amount;
		
		for(ItemStack item: content){
			if(item == null){
				break;
			}
			
			if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
				name = item.getItemMeta().getDisplayName();
			}else {
				name = "DEFAULT";
			}
			
			m = item.getData().getItemType();
			amount = item.getAmount();
			
			l.info("Name: "+name+" Material: "+m.toString()+" Amount: "+amount);
		}
		l.info("--------------------------------------------------------------------------------");
	}
}
