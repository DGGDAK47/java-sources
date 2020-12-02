package org.dggdak47.mloot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.dggdak47.config.PluginConfiguration;
import org.dggdak47.mloot.chest.Chest;
import org.dggdak47.mloot.chest.exceptions.ChestConstructorException;
import org.dggdak47.mranks.kits.Kit;
import org.dggdak47.mranks.kits.exceptions.KitConstructorDataException;

public class Manager {
	private MLoot plugin;
	
	//Sets
	private ArrayList<Kit> kits = new ArrayList<Kit>();
	
	public boolean hasKit(Integer kitId) {
		for(Kit kit: kits){
			if(kit.getId().equals(kitId)){
				return true;
			}
		}
		
		return false;
	}
	public Kit getKitById(Integer kitId) {
		for(Kit kit: kits){
			if(kit.getId().equals(kitId)){
				return kit;
			}
		}
		return null;
	}
	
	
	//Chests
	private ArrayList<Chest> chests = new ArrayList<Chest>();
	
	public ArrayList<Chest> getChests(){
		return (ArrayList)this.chests.clone();
	}
	
	
	//Chest filling
	private BukkitTask chestFillingTask = null;
	private ChestFillingTask chestFillingTaskObject = null;
 	
	public void startFillingTask(MLoot plugin){
		if(chestFillingTask == null) {
			chestFillingTaskObject = new ChestFillingTask(this);
			Double periodTaskInMinutes = c().getDouble("GeneralOptions.ChestsUpdateTimeInMinutes");
			periodTaskInMinutes = periodTaskInMinutes * 1200;
			
			chestFillingTask = Bukkit.getScheduler().runTaskTimer(plugin, chestFillingTaskObject, 20, periodTaskInMinutes.longValue() );
		}else{
			Bukkit.getScheduler().cancelTask(chestFillingTask.getTaskId());
		}
	}
	public void stopFillingTask() {
		if(chestFillingTask != null) {
			chestFillingTaskObject = null;
			Bukkit.getScheduler().cancelTask(chestFillingTask.getTaskId());
		}
	}
	public void fillChestsDirectly() {
		if(chestFillingTaskObject != null){
			chestFillingTaskObject.run();
		}
	}
	
	
	public static void fillInventoryRandomly(Inventory inv, ArrayList<ItemStack> content) {
		inv.clear();
		
		Integer index;
		Random r = new Random();
		
		if(inv.getSize() < content.size()) {
			ItemStack item;
			
			for(int i = 0; i < inv.getSize(); i++){
				item = content.get(i);
				
				while(true){
					index = r.nextInt(inv.getSize());
					if(inv.getItem(index) == null){
						inv.setItem(index, item);
						break;
					}
				}
			}
		}else{
			
			
			for(ItemStack item: content){
				while(true){
					index = r.nextInt(inv.getSize());
					if(inv.getItem(index) == null){
						inv.setItem(index, item);
						break;
					}
				}
			}
		}
	}
	private PluginConfiguration c() {
		return this.plugin.config;
	}
	public Manager(MLoot plugin) throws KitConstructorDataException, ChestConstructorException {
		this.plugin = plugin;
		
		//kits initializing
		Map kitsMap = c().getMap("Kits");
		Set keysOfkitsMap = kitsMap.keySet();
		Map kit;
		for(Object key: keysOfkitsMap){
			kit = (Map)kitsMap.get(key);
			
			kit.put("Permission", "0");
			kit.put("Name", "0");
			
			this.kits.add(new Kit(kit));
		}
		
		
		//chests initializing
		Map chestsMap = c().getMap("Chests");
		Set keysOfChestsMap = chestsMap.keySet();
		Map chest;
		for(Object key: keysOfChestsMap){
			chest = (Map)chestsMap.get(key);
			
			this.chests.add(new Chest(chest));
		}
		
		startFillingTask(this.plugin);
	}
}
