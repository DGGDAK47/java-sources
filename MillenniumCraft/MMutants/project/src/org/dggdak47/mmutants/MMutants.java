package org.dggdak47.mmutants;

import java.io.FileNotFoundException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.dggdak47.config.PluginConfiguration;
import org.dggdak47.mmutants.entityPoint.EntityPoint;
import org.dggdak47.mmutants.invasion.InvasionTask;

import com.esotericsoftware.yamlbeans.YamlException;

public class MMutants extends JavaPlugin implements Listener{
	protected PluginConfiguration config;
	protected Manager manager;
	
	
	
	private InvasionTask it = null;
	@EventHandler
	public void onChat(PlayerChatEvent e) {
		if(e.getMessage().equals("test")){
			if(it != null){
				it.stop();
			}
			
			it = new InvasionTask(this.manager.getInvasion(0), this);
			it.start();
		}
	}
	
	@Override
	public void onEnable() {
		try {
			this.config = new PluginConfiguration("MMutants");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (YamlException e) {
			e.printStackTrace();
			return;
		}
		
		this.manager = new Manager(this);
		
		Bukkit.getPluginManager().registerEvents(this, this);
	}
}