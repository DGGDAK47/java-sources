package org.dggdak47.PACFixings;

import java.util.ArrayList;
import java.lang.Object;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.block.CraftBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.themuhammed2188.pac.api.utils.Attribute;
import me.themuhammed2188.pac.api.HackType;
import me.themuhammed2188.pac.api.event.PlayerViolationEvent;

public class PACFixer extends JavaPlugin implements Listener{
	
	private String[] ms = {"ALLIUM", "OXEYE_DAISY", "PINK_TULIP", "WHITE_TULIP", "ORANGE_TULIP", "RED_TULIP","AZURE_BLUET", "END_ROD", "LILAC", "PEONY", "WALL_SIGN"};
	
	@Override
	public void onEnable(){		
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	
	@EventHandler
	public void onViolation(PlayerViolationEvent e){
		HackType hackType = e.getHackType();
		
	    if(hackType.equals(HackType.PHASE) || hackType.equals(HackType.FLIGHT)){
	    	Attribute attr = (Attribute)e.getAttributes()[0];
	    	String material = getMaterialFromPizdets(attr.getValue().toString());
	    	
	    	if(material == null){
	    		return;
	    	}
	    	
	        for(String s:ms){
	        	if(s.equals(material)){
	        		e.setCancelled(true);
	        		return;
	        	}
	        }
	        
	    }
	}
	
	
	public static String getMaterialFromPizdets(String pizdets){
		int index = pizdets.lastIndexOf("type=");
		index += 5;
		
		String data = "";
		
		while(true){
			if(pizdets.length()-1 == index){
				return null;
			}
			Character ch = new Character(pizdets.charAt(index));

			if(ch.equals(',')){
				break;
			}
			data += ch;
			index++;
		}
		
		return data;
	}
}
