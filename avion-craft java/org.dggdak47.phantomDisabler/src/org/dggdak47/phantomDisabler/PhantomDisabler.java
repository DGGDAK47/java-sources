package org.dggdak47.phantomDisabler;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class PhantomDisabler extends JavaPlugin implements Listener{
	@Override
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	
	@EventHandler
	public void onPhantomSpawning(EntitySpawnEvent e){
		if(!e.getEntityType().equals(EntityType.PHANTOM)){
			return;
		}
		
		Phantom p = (Phantom)e.getEntity();
		p.remove();
		Bukkit.getLogger().info("----------------PHANTOM KILLING");
	}
}
