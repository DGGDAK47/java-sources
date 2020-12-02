package org.dggdak47.mchat;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.dggdak47.config.PluginConfiguration;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.mfractions.MFractions;
import org.dggdak47.mfractions.MFractionsAPI;
import org.dggdak47.mfractions.fraction.Fraction;
import org.dggdak47.mfractions.fraction.FractionPlayer;

import com.esotericsoftware.yamlbeans.YamlException;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class MChat extends JavaPlugin implements Listener{
	private MFractionsAPI mfractionsApi;
	private PluginConfiguration config;
	public final static String chatAccessPermission = "mchat.access";
	public final static String ownerPermission = "mchat.owner";
	public final static String localChatPermission = "mchat.localChat";
	public final static String commandChatPermission = "mchat.commandChat";
	public final static String globalChatPermission = "mchat.globalChat";
	
	
	//Commands
	@Override
	public boolean onCommand(CommandSender cs, Command command, String lable, String[] args){
		if(args.length < 1){
			msg(cs, "Text.WrongCommand");
			return true;
		}else if(args[0].equals("reload")){
			reload(cs);
			return true;
		}else {
			msg(cs, "Text.WrongCommand");
			return true;
		}
	}
	private void reload(CommandSender cs) {
		if( !(cs instanceof ConsoleCommandSender) && !DUtil.hasPermissionPEX((Player)cs, MChat.ownerPermission)){
			msg(cs, "Text.HaveNoPerms");
			return;
		}
		
		cs.sendMessage("[MChat]Reloading...");
		
		try {
			this.config = new PluginConfiguration("MChat");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			msg(cs, "Text.ErrorOccurred");
			return;
		} catch (YamlException e) {
			e.printStackTrace();
			msg(cs, "Text.ErrorOccurred");
			return;
		}
		
		cs.sendMessage("[MChat]Reloaded!");
	}
	
	//Chat handlers
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		if(!DUtil.hasPermissionPEX(e.getPlayer(), MChat.chatAccessPermission)){
			msg(e.getPlayer(), "Text.HaveNoPerms");
			return;
		}
		if(config.getInt("GeneralOptions.ChatType").equals(0)){
			firstChatType(e);
		}else if(config.getInt("GeneralOptions.ChatType").equals(1)){
			secondChatType(e);
		}
	}
	private void firstChatType(AsyncPlayerChatEvent e){
		//access to the chat (and its types) goes through permissions
		secondChatType(e);
	}
	private void secondChatType(AsyncPlayerChatEvent e){
		//access to the chat goes only with one access permission
		String typedText = e.getMessage();
		PermissionUser pu = PermissionsEx.getUser(e.getPlayer());
		Player p = e.getPlayer();
		FractionPlayer fp = this.mfractionsApi.getPlayer(p);
		Fraction f = this.mfractionsApi.getFraction(fp.getFractionID());
		String fractionName = f.getName();
		
		if(!character(typedText.charAt(0)).equals('!')){
			// chat: asd
			ArrayList<Player> nearPlayers = getNearPlayers(p, this.config.getInt("GeneralOptions.LocalChatDistance"));
			
			chat(p, typedText, ChatType.Local, fractionName, pu.getPrefix(), pu.getSuffix(), nearPlayers);
			
			return;
		}else{
			if(typedText.length() <= 1){
				//chat: !
				return;
			}else if(typedText.length() <= 2 && character(typedText.charAt(1)).equals('!')){
				//chat: !!
				return;
			}
			
			ArrayList<Player> recipients;
			String handledTypedText;
			if( character(typedText.charAt(1)).equals('!') ){
				// chat: !!asdasd
				handledTypedText = DUtil.splitByNthEntering(typedText, '!', 2).get(1);
				recipients = mfractionsApi.getBukkitPlayersByFraction( mfractionsApi.getPlayerFraction(p) );
				
				chat(p, handledTypedText, ChatType.Command, fractionName, pu.getPrefix(), pu.getSuffix(), recipients);
			}else{
				// chat: !asdasd
				handledTypedText = DUtil.splitByNthEntering(typedText, '!', 1).get(1);
				recipients = (ArrayList) p.getWorld().getPlayers();
				
				chat(p, handledTypedText, ChatType.Global, fractionName, pu.getPrefix(), pu.getSuffix(), recipients);
			}
			
		}
	}
	private  void chat(Player sender, String message, ChatType chatType, String playersFractionName, String prefix, String suffix, ArrayList<Player> recipients){
		String toSend = "";
		if(chatType.equals(ChatType.Local)){
			toSend += config.getString("ChatTypePrefix.LocalChat");
		}else if(chatType.equals(ChatType.Command)) {
			toSend += config.getString("ChatTypePrefix.CommandChat");
		}else if(chatType.equals(ChatType.Global)) {
			toSend += config.getString("ChatTypePrefix.GlobalChat");
		}
		
		toSend += playersFractionName+prefix+sender.getName()+suffix+": "+message;
		Bukkit.getLogger().info("[MChat: message] "+toSend);
		
		sendMessage(recipients, toSend);
	}
	
	//Utils
	private void msg(CommandSender cs, String configWay){
		cs.sendMessage(this.config.getString(configWay));
	}
	public static ArrayList<Player> getNearPlayers(Player player, Integer distance){
		ArrayList<Player> toReturn = new ArrayList<Player>();
		
		Location playerLocation = player.getLocation();
		Location anotherPlayerLocation;
		ArrayList<Player> playersOfPlayerWorld = (ArrayList) playerLocation.getWorld().getPlayers();
		
		for(Player p: playersOfPlayerWorld){
			anotherPlayerLocation = p.getLocation();
			
			if( playerLocation.getBlockX()-distance <= anotherPlayerLocation.getBlockX() && anotherPlayerLocation.getBlockX() <= playerLocation.getBlockX()+distance){
				if(playerLocation.getBlockY()-distance <= anotherPlayerLocation.getBlockY() && anotherPlayerLocation.getBlockY() <= playerLocation.getBlockY()+distance){
					if(playerLocation.getBlockZ()-distance <= anotherPlayerLocation.getBlockZ() && anotherPlayerLocation.getBlockZ() <= playerLocation.getBlockZ()+distance){
						toReturn.add(p);
					}
				}
			}
			
		}
		
		return toReturn;
	}
	private static Character character(char ch) {
		return new Character(ch);
	}
	private static void sendMessage(ArrayList<Player> recipients, String message){
		for(Player recipient: recipients){
			try {
				recipient.sendMessage(message);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	//Constructor
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		
		this.mfractionsApi = MFractions.getAPI();
		try {
			this.config = new PluginConfiguration("MChat");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (YamlException e) {
			e.printStackTrace();
		}
	}
}
