package org.dggdak47.mloot;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.dggdak47.mloot.blockWrapper.BlockDataWrapper;
import org.dggdak47.mloot.chest.Chest;

public class ChestFillingTask implements Runnable{
	private Manager manager;
	
	@Override
	public void run() {
		World world;
		Block block;
		org.bukkit.block.Chest bukkitChest;
		BlockDataWrapper bdw;
		
		for(Chest chest: manager.getChests()){
			bdw = chest.getBlockData();
			world = Bukkit.getWorld(bdw.worldName);
			
			block = world.getBlockAt(bdw.x, bdw.y, bdw.z);
			
			if(!(block instanceof org.bukkit.block.Chest)){
				block.setType(Material.CHEST);
				bukkitChest = (org.bukkit.block.Chest)block.getState();
			}else{
				bukkitChest = (org.bukkit.block.Chest)block.getState();
			}
			
			Manager.fillInventoryRandomly(bukkitChest.getInventory(), manager.getKitById(chest.getRandomKitId()).getContent() );
		}
	}
	
	public ChestFillingTask(Manager manager) {
		this.manager = manager;
	}
}
