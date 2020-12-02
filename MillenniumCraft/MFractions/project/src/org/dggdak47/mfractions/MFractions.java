package org.dggdak47.mfractions;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.dggdak47.config.PluginConfiguration;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.manager.exceptions.FractionNotExistException;
import org.dggdak47.manager.exceptions.PlayerNotExistException;
import org.dggdak47.mfractions.events.FullPluginReloadEvent;
import org.dggdak47.mfractions.fraction.Fraction;
import org.dggdak47.mfractions.fraction.FractionPlayer;
import org.dggdak47.mfractions.fraction.exceptions.FractionConfigPropertyException;
import org.dggdak47.mfractions.fraction.exceptions.FractionDataException;

import com.esotericsoftware.yamlbeans.YamlException;

public class MFractions extends JavaPlugin{
	protected PluginConfiguration config;
	protected Manager manager;
	protected ManagerEvents events;
	public final String accessPermission = "mfractions.access";
	public final String adminPermission = "mfractions.admin";
	protected boolean hasPluginStartedSuccessfully = false;
	
	public static MFractionsAPI getAPI() {
		return new MFractionsAPI( (MFractions)Bukkit.getPluginManager().getPlugin("MFractions") );
	}
	
	//Commands
	private void showFractionsList(CommandSender cs){
		String message;
		
		if( (cs instanceof Player) && !DUtil.hasPermissionPEX((Player)cs, this.accessPermission)){
			message = this.config.getString("Text.HaveNoPerms");
			cs.sendMessage(message);
			return;
		}
		
		ArrayList<Fraction> fractions = this.manager.getFractions();
		
		cs.sendMessage("-----------------------------");
		message = config.getString("Text.FractionsList");
		
		cs.sendMessage(message);
		for(Fraction f: fractions){
			cs.sendMessage("Name: "+f.getName()+" ID: "+f.getId());
		}
		cs.sendMessage("-----------------------------");
	}
	private void joinPlayerToTheFraction(CommandSender cs, Integer fractionId){
		String message;
		Player p = null;
		
		if( (cs instanceof Player) && !DUtil.hasPermissionPEX((Player)cs, this.accessPermission)){
			message = this.config.getString("Text.HaveNoPerms");
			cs.sendMessage(message);
			return;
		}else if( !(cs instanceof Player) ){
			message = this.config.getString("Text.ConsoleCant");
			cs.sendMessage(message);
			return;
		}else {
			p = (Player)cs;
		}
		
		if(fractionId == null){
			message = this.config.getString("Text.JoinFractionForm");
			cs.sendMessage(message);
			return;
		}else if( !(fractionId >= 1) ){
			message = this.config.getString("Text.FractionNoExists");
			cs.sendMessage(message);
			return;
		}
		
		Integer currentPlayerFractionId = this.manager.getPlayer(p).getFractionID();
		if(currentPlayerFractionId.equals(fractionId)){
			message = config.getString("Text.SameFraction");
			p.sendMessage(message);
			return;
		}
		
		
		try {
			this.manager.setPlayerFraction(p.getName(), fractionId, this.config.getBoolean("GeneralOptions.AnnulScoreOnChangingFraction"));
		} catch (PlayerNotExistException e) {
			message = this.config.getString("Text.PlayerNotFound");
			message = String.format(message, e.getPlayerName());
			cs.sendMessage(message);
			
		} catch (FractionNotExistException e) {
			message = this.config.getString("Text.FractionNoExists");
			cs.sendMessage(message);
		}
		
	}
    private void reload(CommandSender cs, boolean isFull){
    	String message;
    	
    	if( (cs instanceof Player) && !DUtil.hasPermissionPEX((Player)cs, this.adminPermission)){
			message = this.config.getString("Text.HaveNoPerms");
			cs.sendMessage(message);
			return;
		}
    	
    	Logger l = Bukkit.getLogger();
    	cs.sendMessage("[MFractions] Reloading...");
    	message = this.config.getString("Text.ErrorOcurred");
    	
    	try {
			this.config = new PluginConfiguration("MFractions");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			cs.sendMessage(message);
			return;
		} catch (YamlException e) {
			e.printStackTrace();
			cs.sendMessage(message);
			return;
		}
    	
    	try {
    		ArrayList<FractionPlayer> players = this.manager.onlinePlayers;
    		Hashtable<String, String> withdrawedPerms = this.manager.withdrawedPermissionGroups;
    		if(isFull){
        		this.manager = new Manager(this);
        		this.manager.onlinePlayers = players;
        		this.manager.withdrawedPermissionGroups = withdrawedPerms;
        		Bukkit.getPluginManager().callEvent(new FullPluginReloadEvent());
        	}else{
        		Manager m = new Manager(this);
        		m.points = this.manager.points;
        		m.onlinePlayers = players;
        		m.withdrawedPermissionGroups = withdrawedPerms;
        		this.manager = m;
        	}
		} catch (FractionConfigPropertyException e) {
			e.printStackTrace();
			cs.sendMessage(message);
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			cs.sendMessage(message);
			return;
		}
    	
    	if(!this.hasPluginStartedSuccessfully){
    		this.events = new ManagerEvents(this);
    		Bukkit.getPluginManager().registerEvents(this.events, this);
    	}
    	
    	cs.sendMessage("[MFractions] Reloaded!");
    }
	private void setPlayerFraction(CommandSender cs, Player p, Integer fractionId){
		String message;
		
		Integer currentPlayerFractionId = this.manager.getPlayer(p).getFractionID();
		if(currentPlayerFractionId.equals(fractionId)){
			message = config.getString("Text.SameFractionA");
			p.sendMessage(message);
			return;
		}
		
		
		try {
			this.manager.setPlayerFraction(p.getName(), fractionId, this.config.getBoolean("GeneralOptions.AnnulScoreOnChangingFraction"));
			message = config.getString("Text.FractionAssigned");
			cs.sendMessage(message);
		} catch (PlayerNotExistException e) {
			message = config.getString("Text.PlayerNotFound");
			message = String.format(message, e.getPlayerName());
			cs.sendMessage(message);
		} catch (FractionNotExistException e) {
			message = config.getString("Text.FractionNoExists");
			cs.sendMessage(message);
		}
			
		
	}
    
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String lable, String[] args) {
		String message;
		
		if(args.length > 0){
			//list
			if(args[0].equals("list")){
				showFractionsList(commandSender);
			//join
			}else if(args[0].equals("join")){
				if(args.length >= 2){
					try {
						Integer fractionId = new Integer(args[1]);
						joinPlayerToTheFraction(commandSender, fractionId);
					}catch(NumberFormatException e) {
						message = this.config.getString("Text.WrongCommand");
						commandSender.sendMessage(message);
						return true;
					}
				}else{
					joinPlayerToTheFraction(commandSender, null);
				}
			//reload
			}else if(args[0].equals("reload")){
				if(args.length >= 2 && args[1].equals("full")){
					reload(commandSender, true);
				}else{
					reload(commandSender, false);
				}
			//set
			}else if(args[0].equals("set")){
				String playerName;
				Integer fractionId;
				try {
					playerName = args[1];
					fractionId = new Integer(args[2]);
					
					Player p = Bukkit.getPlayer(playerName);
					
					if( (commandSender instanceof Player) && !DUtil.hasPermissionPEX((Player)commandSender, this.adminPermission)){
						message = this.config.getString("Text.HaveNoPerms");
						commandSender.sendMessage(message);
						return true;
					}
					
					setPlayerFraction(commandSender, p, fractionId);
				}catch(Exception e) {
					message = this.config.getString("Text.WrongCommand");
					commandSender.sendMessage(message);
					return true;
				}
			}else{
				message = this.config.getString("Text.WrongCommand");
				commandSender.sendMessage(message);
			}
		}else{
			message = this.config.getString("Text.MFractions");
			commandSender.sendMessage(message);
		}
		
		return true;
	}
	
	//Constructor
	@Override
	public void onEnable() {
		try {
			this.config = new PluginConfiguration("MFractions");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (YamlException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			this.manager = new Manager(this);
		} catch (FractionConfigPropertyException e) {
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		
		this.events = new ManagerEvents(this);
		Bukkit.getPluginManager().registerEvents(this.events, this);
		this.hasPluginStartedSuccessfully = true;
	}
	@Override
	public void onDisable() {
		this.manager.disabling();
	}
}
