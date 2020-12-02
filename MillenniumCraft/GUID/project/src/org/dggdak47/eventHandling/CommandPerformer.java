package org.dggdak47.eventHandling;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandPerformer implements Runnable{
	private Player p;
	private String command;
	private Plugin plugin;
	
	@Override
	public void run() {
		p.performCommand(command);
	}
	
	public void perform() {
		Bukkit.getScheduler().runTaskLater(this.plugin, this, 5);
	}
	
	public CommandPerformer(Player p, String command, Plugin plugin) {
		this.p = p;
		this.command = command;
		this.plugin = plugin;
	}

}
