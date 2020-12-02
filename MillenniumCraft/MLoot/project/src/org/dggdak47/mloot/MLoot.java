package org.dggdak47.mloot;

import java.io.FileNotFoundException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.dggdak47.config.PluginConfiguration;
import org.dggdak47.mloot.chest.exceptions.ChestConstructorException;
import org.dggdak47.mranks.kits.exceptions.KitConstructorDataException;

import com.esotericsoftware.yamlbeans.YamlException;

public class MLoot extends JavaPlugin{
	protected PluginConfiguration config;
	protected Manager manager;
	
	public void reload(CommandSender cs) {
		cs.sendMessage("[MLoot] Reloading...");
		
		try {
			this.config = new PluginConfiguration("MLoot");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (YamlException e1) {
			e1.printStackTrace();
		}
		
		try {
			if(manager != null){
				manager.stopFillingTask();
			}
			this.manager = new Manager(this);
		} catch (KitConstructorDataException e) {
			e.printStackTrace();
		} catch (ChestConstructorException e) {
			e.printStackTrace();
		}
		
		
		cs.sendMessage("[MLoot] Reloaded!");
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String lable, String[] args) {
		if(args.length > 0 && args[0].equals("reload")){
			reload(commandSender);
		}
		
		return true;
	}
	
	@Override
	public void onEnable() {
		try {
			this.config = new PluginConfiguration("MLoot");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (YamlException e1) {
			e1.printStackTrace();
		}
		
		try {
			this.manager = new Manager(this);
		} catch (KitConstructorDataException e) {
			e.printStackTrace();
		} catch (ChestConstructorException e) {
			e.printStackTrace();
		}
		
		
	}
}