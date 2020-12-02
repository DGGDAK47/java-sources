package org.dggdak47.mranks;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.dggdak47.config.PluginConfiguration;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.mfractions.MFractions;
import org.dggdak47.mfractions.MFractionsAPI;

import com.esotericsoftware.yamlbeans.YamlException;

public class MRanks extends JavaPlugin{
	protected PluginConfiguration config;
	protected Manager manager;
	protected MFractionsAPI mfractionsApi;
	protected ManagerEvents events = null;
	private PluginCommands commands;
	public final static String accessPermission = "mranks.access";
	public final static String kitAccessPermission = "mranks.kitAccess";
	public final static String adminPermission = "mranks.admin";
	public final static String ownerPermission = "mranks.owner";
	public final static String kitDelayExemptPermission = "mranks.kitDelayExempt";
	
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String lable, String[] args) {
		try{
			if(args[0].equals("show")){
				this.commands.show(commandSender, args);
			}else if(args[0].equals("rank")){
				this.commands.rank(commandSender, args);
			}else if(args[0].equals("kit")){
				this.commands.kit(commandSender, args);
			}else if(args[0].equals("add")){
				this.commands.add(commandSender, args);
			}else if(args[0].equals("reload")){
				reload(commandSender);
			}else{
				this.commands.msg(commandSender, "Text.WrongCommand");
			}
		}catch(Exception e){
			this.commands.msg(commandSender, "Text.WrongCommand");
		}
		
		return true;
	}
	private void reload(CommandSender cs){
		if( !(cs instanceof ConsoleCommandSender) && !DUtil.hasPermissionPEX((Player)cs, MRanks.ownerPermission)){
			String message = config.getString("Text.HaveNoPerms");
			cs.sendMessage(message);
			return;
		}
		
		cs.sendMessage("[MRanks]Reloading...");
		try {
			this.config = new PluginConfiguration("MRanks");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (YamlException e) {
			e.printStackTrace();
		}
		
		try {
			this.manager = new Manager(this);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		if(this.events == null){
			this.events = new ManagerEvents(this);
			Bukkit.getPluginManager().registerEvents(this.events, this);
		}
		
		cs.sendMessage("[MRanks]Reloaded!");
	}
	
	public static MRanksAPI getApi() {
		return new MRanksAPI((MRanks)Bukkit.getPluginManager().getPlugin("MRanks"));
	}
	@Override
	public void onEnable() {
		mfractionsApi = MFractions.getAPI();
		commands = new PluginCommands(this);
		
		try {
			this.config = new PluginConfiguration("MRanks");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (YamlException e) {
			e.printStackTrace();
			return;
		}
		try {
			this.manager = new Manager(this);
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		
		this.events = new ManagerEvents(this);
		Bukkit.getPluginManager().registerEvents(this.events, this);
	}
}
