package org.dggdak47.mranks;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.mfractions.events.PrePermissionGroupsResetEvent;
import org.dggdak47.mfractions.fraction.FractionPlayer;
import org.dggdak47.mranks.kits.exceptions.KitNoExistException;

public class ManagerEvents implements Listener{
	private MRanks plugin;
	
	@EventHandler
	public void respawnEvent(PlayerRespawnEvent e){
		Player p = e.getPlayer();
		if(!DUtil.hasPermissionPEX(p, this.plugin.kitAccessPermission)){
			return;
		}
		
		Integer kitId = this.plugin.manager.hasPlayerKit(p);
		if(kitId == null){
			return;
		}
		
		try {
			this.plugin.manager.giveKit(p, kitId, false);
		} catch (KitNoExistException e1) {
			e1.printStackTrace();
		}
	}
	@EventHandler
	public void onPlayerPermissionGroupsPreReset(PrePermissionGroupsResetEvent e) {
		if(plugin.manager.doesPlayerRankSaves()){
			Player p = e.getPlayer();
			Integer rankId = plugin.manager.hasPlayerRank(p);
			
			if(rankId != null){
				ArrayList<String> rankGroups = plugin.manager.getRank(rankId).getPermissionGroups();
				for(String group: rankGroups){
					e.addGroup(group);
				}
			}
		}
	}
	
	public ManagerEvents(MRanks plugin) {
		this.plugin = plugin;
	}
}
