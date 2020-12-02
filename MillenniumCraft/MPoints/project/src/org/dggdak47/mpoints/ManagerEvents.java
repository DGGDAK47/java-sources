package org.dggdak47.mpoints;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.dggdak47.config.PluginConfiguration;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.mfractions.events.PlayerChangeFractionEvent;
import org.dggdak47.mfractions.events.PreRespawningEvent;
import org.dggdak47.mfractions.fraction.FractionPlayer;
import org.dggdak47.mpoints.area.Area;
import org.dggdak47.mpoints.area.AreaController;
import org.dggdak47.mpoints.events.AreaCapturedEvent;
import org.dggdak47.mpoints.events.PlayerEnteredAreaEvent;
import org.dggdak47.mpoints.events.PlayerEnteredCapturingRegionEvent;
import org.dggdak47.mpoints.events.PlayerLeftAreaEvent;
import org.dggdak47.mpoints.events.PlayerLeftCapturingRegionEvent;
import org.dggdak47.mpoints.events.PlayerMoveWithinAreaEvent;
import org.dggdak47.mpoints.regions.Region;

public class ManagerEvents implements Listener{
	private MPoints plugin;
	
	
	//MovingHandling
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		
		AreaController controllerWithPlayer = m().getAreaControllerByPlayer(p);
		AreaController controllerByNewLocation = m().getAreaControllerByLocation(e.getTo());
		PluginManager pm = Bukkit.getPluginManager();
		
		//Ожидается что две зоны не пересекаются
		if(controllerWithPlayer == null){
			//игрок еще не входил в зону
			if(controllerByNewLocation == null){
				//данныое перемещение игрока не было вхождением в зону
				return;
			}else{
				//если игрок не был ни в одной зоне, но вошел в одну из них
				controllerByNewLocation.addPlayer(p);
				pm.callEvent(new PlayerEnteredAreaEvent(p, controllerByNewLocation.area, null));
				
			}
		}else{
			// игрок был в одной из зон
			if(controllerByNewLocation == null){
				//игрок вышел за пределы зоны, в которой находился
				controllerWithPlayer.removePlayer(p);
				pm.callEvent(new PlayerLeftAreaEvent(p, controllerWithPlayer.area));
				
			}else{
				//игрок двигается в той же зоне, или он перешел из одной зоны в другую
				if(!controllerWithPlayer.area.getRespawningPointMaskId().equals(controllerByNewLocation.area.getRespawningPointMaskId())){
					//игрок перешел из одной зоны в другую, не выходя из зон вообще
					controllerWithPlayer.removePlayer(p);
					controllerByNewLocation.addPlayer(p);
					pm.callEvent(new PlayerEnteredAreaEvent(p, controllerByNewLocation.area, controllerWithPlayer.area));
					return;
				}else{
					//по идеи, игрок двигается еще в той же зоне
					pm.callEvent(new PlayerMoveWithinAreaEvent(p, controllerWithPlayer.area));
				}
				Region capturingRegionOfArea = controllerWithPlayer.area.getCapturingRegion();
				Boolean isPlayerWithinCapturingRegionFrom = capturingRegionOfArea.isLocationWithin(e.getFrom());
				Boolean isPlayerWithinCapturingRegionTo = capturingRegionOfArea.isLocationWithin(e.getTo());
				
				if(!isPlayerWithinCapturingRegionFrom && isPlayerWithinCapturingRegionTo){
					//игрок вошел в область захвата зоны
					pm.callEvent(new PlayerEnteredCapturingRegionEvent(p, controllerWithPlayer.area));
					
				}else if(isPlayerWithinCapturingRegionFrom && !isPlayerWithinCapturingRegionTo){
					//игрок вышел из области захвата зоны
					pm.callEvent(new PlayerLeftCapturingRegionEvent(p, controllerWithPlayer.area));
					
				}else if(isPlayerWithinCapturingRegionFrom && isPlayerWithinCapturingRegionTo){
					//передвижение игрока было исключительно в пределах области захвата зоны
					
				}
			}
		}
	}
	
	//AreaEvents
	@EventHandler
	public void onPlayerEnterArea(PlayerEnteredAreaEvent e) {
		Player p = e.getPlayer();
		Area enteredArea = e.getEnteredArea();
		
		try{
			
		}catch(Exception exception){
			m().getAreaControllerByArea(enteredArea).removePlayer(p);
		}
	}
	@EventHandler
	public void onPlayerLeaveAreaEvent(PlayerLeftAreaEvent e) {
		Player p = e.getPlayer();
		Area leftArea = e.getLeftArea();
		
		try{
			
		}catch(Exception exception){
			m().getAreaControllerByArea(leftArea).removePlayer(p);
		}
	}
	
	//CapturingEvents
	@EventHandler
	public void onPlayerEnterCaptruringRegionOfArea(PlayerEnteredCapturingRegionEvent e) {
		if(!this.plugin.authMeApi.isAuthenticated(e.getPlayer()) || !DUtil.hasPermissionPEX(e.getPlayer(), MPoints.accessPermission)){
			return;
		}
		
		AreaController ac = this.plugin.manager.getAreaControllerByArea(e.getCapturingRegionArea());
		Player p = e.getPlayer();
		FractionPlayer fp = this.plugin.mfractionsApi.getPlayer(p);
		try{
			if(!ac.isAreaCapturing()){
				if(ac.getOwners() == null){
					ac.startCapturingArea(this.plugin);
				}else if(!ac.getOwners().getId().equals(fp.getFractionID())){
					ac.startCapturingArea(this.plugin);
				}
			}
		}catch(Exception exception){
			//nothing
		}
		
		
	}
	@EventHandler
	public void onPlayerLeaveCapturingRegionOfArea(PlayerLeftCapturingRegionEvent e) {
		
	}
	@EventHandler
	public void onAreaCaptured(AreaCapturedEvent e) {
		this.plugin.guidManager.removeAllPlayersBySpawningPointId(e.getCapturedArea().getRespawningPointMaskId());
	}
	
	//Player Join/Quit
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		//removing from one of area controllers
		AreaController ac = m().getAreaControllerByPlayer(e.getPlayer());
		if(ac != null) {
			ac.removePlayer(e.getPlayer());
		}
		
		//removing from overriddenPlayersRespawningPointsId
		this.plugin.guidManager.removePlayer(e.getPlayer());
	}
	
	//Respawn Controll
	@EventHandler
	public void onPlayerPreRespawn(PreRespawningEvent e) {
		if(!DUtil.hasPermissionPEX(e.getPlayer(), MPoints.respawnPermission)){
			return;
		}
		
		Integer spawnPointMaskId = this.plugin.guidManager.hasPlayer(e.getPlayer());
		
		if(spawnPointMaskId != null){
			Location newLoc = m().getAreaByMaskId(spawnPointMaskId).getRandomRespawningLocation();
			e.setNewLocation(newLoc);
		}
	}
	@EventHandler
	public void onPlayerChangeFraction(PlayerChangeFractionEvent e) {
		this.plugin.guidManager.removePlayer(e.getPlayer());
	}
	
	private PluginConfiguration c() {
		return this.plugin.config;
	}
	private Manager m() {
		return this.plugin.manager;
	}
	public ManagerEvents(MPoints plugin) {
		this.plugin = plugin;
	}
}
