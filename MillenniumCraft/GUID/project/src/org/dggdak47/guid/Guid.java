package org.dggdak47.guid;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.dggdak47.eventHandling.GuidEventHandler;
import org.dggdak47.guid.exceptions.InventoryWithSameDataExistsException;
import org.dggdak47.guid.exceptions.SuchInventoryNoExistsException;
import org.dggdak47.guid.wrapping.InventoryWrapper;
import org.dggdak47.guid.wrapping.ItemWrapper;
import org.dggdak47.opening.InventoryOpener;
import org.dggdak47.substitution.Substitution;

import com.esotericsoftware.yamlbeans.YamlReader;

public class Guid extends JavaPlugin{
	
	public Manager manager;
	private GuidEventHandler geh;
	private Map config;
	
	public final String permission = "GUID.access";
	
	private void reload(boolean isFull) {
		
		Logger l = Bukkit.getLogger();
		
		l.info("[GUID] Reloading");
		this.config = configProcess();
		
		ArrayList<InventoryWrapper> createdInventories = this.manager.getCreatedInventories();
		
		try {
			if(isFull){
				this.manager = new Manager(this, this.config);
			}else {
				this.manager = new Manager(this, this.config, createdInventories);
			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		this.geh = new GuidEventHandler(this);
		l.info("[GUID] Reloaded");
	}
	
	public static GuidAPI getAPI(){
		return new GuidAPI( (Guid)Bukkit.getServer().getPluginManager().getPlugin("Guid") );
	}
	
	private static Map configProcess(){
		//1.2.1.1
		Properties p = System.getProperties();
		String sep = p.getProperty("file.separator");
		String dir = p.getProperty("user.dir");
		
		
		//1.2.1.2
		try {
			YamlReader yr = new YamlReader(new FileReader(dir+sep+"plugins"+sep+"GUID"+sep+"config.yml"));
			
			return (Map)yr.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Hashtable();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String lable, String[] args) {
		
		//консоль
		if(commandSender instanceof ConsoleCommandSender){
			if(command.getName().equals("guid")){
				if(args.length == 0){
					String message = (String)((Map)this.config.get("Text")).get("GUID");
					commandSender.sendMessage(message);
					
				}else if(args[0].equals("reload")){
					if(args.length > 1 && args[1].equals("full")){
						reload(true);
					}else{
						reload(false);
					}
				}
		    }
		//игрок
		}else if(commandSender instanceof Player && Util.hasPermissionPEX((Player) commandSender, this.permission)){
			if(command.getName().equals("guid") && args.length == 0){
				String message = (String)((Map)this.config.get("Text")).get("GUID");
				commandSender.sendMessage(message);
				
			}else if(command.getName().equals("guid") && args.length > 0 ){
				if(args[0].equals("reload")){
					if(args.length > 1 && args[1].equals("full")){
						reload(true);
					}else{
						reload(false);
					}
					
				}else if(args[0].equals("test")){
					this.manager.openInventory(0, (Player)commandSender);
				}else{
					String message = (String)((Map)this.config.get("Text")).get("WrongCommand");
					commandSender.sendMessage(message);
				}
				
			}
		}else{
			String haveNoAccessMessage = (String)((Map)this.config.get("Text")).get("HaveNoAccess");
			commandSender.sendMessage(haveNoAccessMessage);
		}
		
		return true;
	}
	
	@Override
	public void onEnable() {
		this.config = configProcess();
		this.geh = new GuidEventHandler(this);
		
		Bukkit.getPluginManager().registerEvents(this.geh, this);
		
		try{
			this.manager = new Manager(this, this.config);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
