package org.dggdak47.mpoints;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.dggdak47.config.PluginConfiguration;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.guid.Guid;
import org.dggdak47.guid.GuidAPI;
import org.dggdak47.mfractions.MFractions;
import org.dggdak47.mfractions.MFractionsAPI;
import org.dggdak47.mpoints.area.exceptions.AreaConstructorDataException;
import org.dggdak47.mpoints.regions.Region;
import org.dggdak47.mpoints.regions.exceptions.RegionConstructorDataException;

import com.esotericsoftware.yamlbeans.YamlException;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.v3.AuthMeApi;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class MPoints extends JavaPlugin implements Listener{
	protected Manager manager;
	protected ManagerEvents events = null;
	public PluginConfiguration config;
	protected GuidManager guidManager;
	
	public MFractionsAPI mfractionsApi;
	public AuthMeApi authMeApi;
	public GuidAPI guidApi;
	
	public final static String accessPermission = "mpoints.access";
	public final static String ownerPermission = "mpoints.owner";
	public final static String respawnPermission = "mpoints.respawnOnChoosenPoint";
	
	//Commands
	public void reload(CommandSender cs) {
		
		if(!(cs instanceof ConsoleCommandSender) && !DUtil.hasPermissionPEX((Player)cs, MPoints.ownerPermission)){
			msg(cs, "Messages.Text.HaveNoPerms");
			return;
		}
		
		cs.sendMessage("[MPoints]Reloading...");
		try {
			this.config = new PluginConfiguration("MPoints");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (YamlException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			this.manager.finalize();
			this.manager = new Manager(this);
		} catch (AreaConstructorDataException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		this.guidManager = new GuidManager(this);
		if(events == null) {
			this.events = new ManagerEvents(this);
			Bukkit.getPluginManager().registerEvents(this.events, this);
		}
		cs.sendMessage("[MPoints]Reloaded!");
	}
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String lable, String[] args) {
		String cmdName = command.getName();
		
		try{
			if(cmdName.equals("mpoints")){
				if(args[0].equals("reload")){
					reload(commandSender);
				}else{
					msg(commandSender, "Messages.Text.WrongCommand");
				}
			}else if(cmdName.equals("sp")){
				this.guidManager.sp(commandSender, args);
			}else{
				msg(commandSender, "Messages.Text.WrongCommand");
			}
		}catch(Exception e){
			msg(commandSender, "Messages.Text.WrongCommand");
		}
		
		return true;
		
	}
	
	public static void sendActionBarMessage(ArrayList<Player> recipients, String message) {
		for(Player p: recipients){
			try{
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
			}catch(Exception e){
				//nothing
			}
		}
	}
	public void msg(CommandSender cs, String way) {
		cs.sendMessage(this.config.getString(way));
	}
	@Override
	public void onEnable() {
		this.authMeApi = AuthMeApi.getInstance();
		this.mfractionsApi = MFractions.getAPI();
		this.guidApi = Guid.getAPI();
		
		try {
			this.config = new PluginConfiguration("MPoints");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (YamlException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			this.manager = new Manager(this);
		} catch (AreaConstructorDataException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.events = new ManagerEvents(this);
		Bukkit.getPluginManager().registerEvents(this.events, this);
		this.guidManager = new GuidManager(this);
	}
}
