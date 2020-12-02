package org.dggdak47.mpoints;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.dggdak47.config.PluginConfiguration;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.guid.wrapping.InventoryWrapper;
import org.dggdak47.guid.wrapping.ItemWrapper;
import org.dggdak47.mfractions.fraction.FractionPlayer;
import org.dggdak47.mpoints.area.Area;
import org.dggdak47.mpoints.area.AreaController;
import org.dggdak47.substitution.Substitution;

public class GuidManager {
	private MPoints plugin;
	
	//Players Respawning points
	private Hashtable<Player, Integer> overriddenPlayersRespawningPointsId = new Hashtable<Player, Integer>();
	
	public Integer hasPlayer(Player p) {
		return overriddenPlayersRespawningPointsId.get(p);
	}
	public void removePlayer(Player p){
		overriddenPlayersRespawningPointsId.remove(p);
	}
    public void removeAllPlayersBySpawningPointId(Integer spawnPointId) {
		ArrayList<Player> playersToRemove = new ArrayList<Player>();
		
		Set<Player> players = overriddenPlayersRespawningPointsId.keySet();
		Integer playersSpawnPointId;
		for(Player p: players){
			playersSpawnPointId = overriddenPlayersRespawningPointsId.get(p);
			if(playersSpawnPointId != null && playersSpawnPointId.equals(spawnPointId)){
				playersToRemove.add(p);
			}
		}
		
		for(Player p: playersToRemove){
			overriddenPlayersRespawningPointsId.remove(p);
		}
	}
	public void putPlayer(Player p, Integer spawnPointId) {
		overriddenPlayersRespawningPointsId.put(p, spawnPointId);
	}
	public ArrayList<Player> getPlayersBySpawnPointId(Integer spawnPointId) {
		ArrayList<Player> toReturn = new ArrayList<Player>();
		
		Set<Player> keys = overriddenPlayersRespawningPointsId.keySet();
		for(Player key: keys){
			if(overriddenPlayersRespawningPointsId.get(key).equals(spawnPointId)){
				toReturn.add(key);
			}
		}
		
		return toReturn;
	}
	
	
	//Commands
	public void sp(CommandSender cs, String[] args) {
		if(args.length <= 0){
			//Chat: /sp
			if(cs instanceof ConsoleCommandSender){
				this.plugin.msg(cs, "Messages.Text.ConsoleCant");
				return;
			}else if(!DUtil.hasPermissionPEX((Player)cs, MPoints.accessPermission)){
				this.plugin.msg(cs, "Messages.Text.HaveNoPerms");
				return;
			}else{
				openSpawnPointChooseingInventory((Player)cs);
			}
		}else if(args.length > 0){
			//Chat: /sp [..]
			if(args[0].equals("choose")){
				//Chat: /sp choose <points_id>
				if(cs instanceof ConsoleCommandSender){
					this.plugin.msg(cs, "Messages.Text.ConsoleCant");
					return;
				}else if(!DUtil.hasPermissionPEX((Player)cs, MPoints.accessPermission)){
					this.plugin.msg(cs, "Messages.Text.HaveNoPerms");
					return;
				}else{
					chooseSpawnPoint((Player)cs, new Integer(args[1]));
				}
			}else{
				plugin.msg(cs, "Messages.Text.WrongCommand");
			}
		}
	}
	private void chooseSpawnPoint(Player sender, Integer newSpawnPointMaskId){
		if(newSpawnPointMaskId.equals(-1)){
			if(hasPlayer(sender) == null){
				plugin.msg(sender, "Messages.Text.SameArea");
				return;
			}else{
				removePlayer(sender);
				plugin.msg(sender, "Messages.Text.NewSpawnPointAssigned");
				return;
			}
		}
		
		FractionPlayer fp = plugin.mfractionsApi.getPlayer(sender);
		AreaController choosenArea = plugin.manager.getAreaControllerByAreaMaskId(newSpawnPointMaskId);
		
		if(choosenArea == null){
			plugin.msg(sender, "Messages.Text.NoSuchSpawnPointMaskId");
			return;
		}else if(!choosenArea.area.hasRespawningPoints()){
			plugin.msg(sender, "Messages.Text.NoSuchSpawnPointMaskId");
			return;
		}else if(choosenArea.getOwners() == null || !choosenArea.getOwners().getId().equals(fp.getFractionID())){
			plugin.msg(sender, "Messages.Text.YourFractionIsNotOwners");
			return;
		}else if(hasPlayer(sender) != null && hasPlayer(sender).equals(newSpawnPointMaskId)){
			plugin.msg(sender, "Messages.Text.SameArea");
			return;
		}
		//ƒальше идЄт код, если: указанна€ зона есть, она принадлежит фракции указанного игрока,
		//на ней можно по€вл€тьс€ (т.е. она имеет точки по€влени€) и эта зона не выбрана игроком
		putPlayer(sender, newSpawnPointMaskId);
		plugin.msg(sender, "Messages.Text.NewSpawnPointAssigned");
	}
	private void openSpawnPointChooseingInventory(Player p){
		Integer inventoryId = c().getInt("SPInventory.InventoryId");
		
		InventoryWrapper iw = this.plugin.guidApi.getInventoryWrapper(inventoryId);
		ArrayList<ItemWrapper> iwContent = iw.getItems();
		
		
		//Substitutions filling
		ArrayList<Substitution> substitutions = new ArrayList<Substitution>();
		
		Integer overriddenPlayersRespawnPointMaskId = hasPlayer(p);
		Area currentPlayersRespawnArea = null;
		
		if(overriddenPlayersRespawnPointMaskId == null){
			String substitutionData = "Index:0|Flags:HIDE_ENCHANTS|Enchants unsafe:DIG_SPEED 1";
			substitutions.add(new Substitution(substitutionData));
		}else{
			currentPlayersRespawnArea = plugin.manager.getAreaByMaskId(overriddenPlayersRespawnPointMaskId);
		}
		
		String owners = c().getString("SPInventory.AreaSpawnItem.owners");
		AreaController ac;
		String substitutionS;
		ArrayList<String> lore;
		
		for(ItemWrapper itemWrapper: iwContent){
			ac = this.plugin.manager.getAreaControllerByAreaName(itemWrapper.getName());
			if(ac == null || !ac.area.hasRespawningPoints()){
				continue;
			}
			
			substitutionS = "";
			substitutionS += "Index:"+itemWrapper.getIndex()+"|";
			substitutionS += "Lore:";
			
			
			lore = itemWrapper.getLore();
			for(String l: lore){
				substitutionS += l+"~";
			}
			
			if(ac.getOwners() == null){
				substitutionS += (String.format(owners, c().getString("Symbols.Area.AreaHasNoOwners")) );
			}else {
				substitutionS += (String.format(owners, ac.getOwners().getName()));
			}
			
			if(currentPlayersRespawnArea != null && currentPlayersRespawnArea.getName().equals(itemWrapper.getName())){
				substitutionS += "|Flags:HIDE_ENCHANTS|Enchants unsafe:DIG_SPEED 1";
			}
			
			substitutions.add(new Substitution(substitutionS));
		}
		
		
		this.plugin.guidApi.openInventory(inventoryId, p, substitutions);
	}
	
	//Inventory forming
	public InventoryWrapper formInventory(){
		ArrayList<AreaController> areas = this.plugin.manager.getAreas();
		ArrayList<ItemWrapper> inventoryContent = new ArrayList<ItemWrapper>();
		
		
		//DefaultSpawnItem initializing
		Hashtable<String, Object> itemConfig = new Hashtable<String, Object>();
		itemConfig.put("Name", c().getString("SPInventory.DefaultSpawnItem.ItemName"));
		itemConfig.put("Material", Material.valueOf(c().getString("SPInventory.DefaultSpawnItem.Material")));
		itemConfig.put("Command", "sp choose -1");
		itemConfig.put("Index", 0);
		itemConfig.put("Lore", c().getListS("SPInventory.DefaultSpawnItem.Description"));
		itemConfig.put("CloseOnClick", true);
		inventoryContent.add(new ItemWrapper(itemConfig));
		
		
		//AreaSpawnItems initializing
		AreaController ac;
		
		ArrayList<String> defaultItemLore = c().getListS("SPInventory.AreaSpawnItem.defaultLore");
		Integer exceptions = 0;
		
		for(int i = 0; i < areas.size(); i++){
			ac = areas.get(i);
			
			if(!ac.area.hasRespawningPoints()){
				exceptions++;
				continue;
			}
			
			itemConfig = new Hashtable<String, Object>();
			itemConfig.put("Name", ac.area.getName());
			itemConfig.put("Material", ac.area.getItemMaterial());
			itemConfig.put("Command", "sp choose "+ac.area.getRespawningPointMaskId());
			itemConfig.put("Index", i+1-exceptions);
			ArrayList<String> lore = new ArrayList<String>();
			lore.addAll(c().getListS("SPInventory.AreaSpawnItem.defaultLore"));
			lore.addAll(ac.area.getDescription());
			itemConfig.put("Lore", lore);
			itemConfig.put("CloseOnClick", true);
			
			inventoryContent.add(new ItemWrapper(itemConfig));
		}
		
		//InventoryWrapper initializing
		Hashtable<String, String> inventConfig = new Hashtable<String, String>();
		inventConfig.put("Name", c().getString("SPInventory.InventoryName"));
		inventConfig.put("ID", c().getString("SPInventory.InventoryId"));
		inventConfig.put("Size", "54");
		
		return new InventoryWrapper(inventConfig, inventoryContent);
	}
	
	public PluginConfiguration c() {
		return this.plugin.config;
	}
	public GuidManager(MPoints plugin){
		this.plugin = plugin;
		this.plugin.guidApi.removeCreatedInventory(c().getInt("SPInventory.InventoryId"));
		this.plugin.guidApi.createInventory(formInventory());
	}
}