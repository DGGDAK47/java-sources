package org.dggdak47.mpoints;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.dggdak47.config.PluginConfiguration;
import org.dggdak47.mpoints.area.Area;
import org.dggdak47.mpoints.area.AreaController;
import org.dggdak47.mpoints.area.exceptions.AreaConstructorDataException;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Manager {
	private MPoints plugin;
	private ArrayList<AreaController> areas = new ArrayList<AreaController>();
	
	//Area
	public Area getAreaByLocation(Location loc){
		for(AreaController a: areas){
			if(a.area.getAreaRegion().isLocationWithin(loc)){
				return a.area;
				
			}
		}
		
		return null;
	}
	public Area getAreaByMaskId(Integer maskId) {
		for(AreaController a: areas){
			if(a.area.getRespawningPointMaskId().equals(maskId)){
				return a.area;
				
			}
		}
		
		return null;
	}
	
	//AreaController
	public ArrayList<AreaController> getAreas(){
		return (ArrayList)this.areas.clone();
	}
	public AreaController getAreaControllerByLocation(Location loc) {
		for(AreaController a: areas){
			if(a.area.getAreaRegion().isLocationWithin(loc)){
				return a;
			}
		}
		return null;
	}
	public AreaController getAreaControllerByPlayer(Player p) {
		for(AreaController a: areas){
			if(a.hasPlayer(p)){
				return a;
			}
		}
		
		return null;
	}
	public AreaController getAreaControllerByArea(Area a) {
		for(AreaController ac: areas){
			if(ac.area.getRespawningPointMaskId().equals(a.getRespawningPointMaskId())){
				return ac;
			}
		}
		
		return null;
	}
	public AreaController getAreaControllerByAreaName(String name) {
		for(AreaController ac: areas){
			if(ac.area.getName().equals(name)){
				return ac;
			}
		}
		
		return null;
	}
	public AreaController getAreaControllerByAreaMaskId(Integer id) {
		for(AreaController ac: areas){
			if(ac.area.getRespawningPointMaskId().equals(id)){
				return ac;
			}
		}
		
		return null;
	}
	
	//Players Informing
	private static class PlayersActionbarInforming implements Runnable{
		private Manager manager;
		
		@Override
		public void run() {
			ArrayList<Player> notCapturingPlayers;
			String areaInfoMessage = this.manager.c().getString("Messages.Formatting.AreaInfo");
			String areaBeingCapturedMessag = this.manager.c().getString("Messages.Text.AreaIsBeingCaptured");
			String areaHasNoOwner = this.manager.c().getString("Symbols.Area.AreaHasNoOwners");
			String message;
			
			for(AreaController ac: this.manager.areas){
				notCapturingPlayers = ac.getNotCapturingPlayers();
				
				if(notCapturingPlayers.isEmpty()){
					continue;
				}
				
				if(ac.isAreaCapturing()){
					MPoints.sendActionBarMessage(notCapturingPlayers, areaBeingCapturedMessag);
				}else{
					if(ac.getOwners() == null){
						message = String.format(areaInfoMessage, ac.area.getName(), areaHasNoOwner);
					}else{
						message = String.format(areaInfoMessage, ac.area.getName(), ac.getOwners().getName());
					}
					MPoints.sendActionBarMessage(notCapturingPlayers, message);
				}
			}
		}
		
		public PlayersActionbarInforming(Manager manager) {
			this.manager = manager;
		}
	}
	private BukkitTask playersActionBarInformingTask = null;
	private PlayersActionbarInforming informingObject = null;
	
	private void startActionbarInforming(){
		if(playersActionBarInformingTask != null){
			Bukkit.getScheduler().cancelTask(playersActionBarInformingTask.getTaskId());
		}
		PlayersActionbarInforming ob = new PlayersActionbarInforming(this);
		this.informingObject = ob;
		this.playersActionBarInformingTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, ob, 0, 20);
	}
	private void stopActionbarInforming(){
		if(playersActionBarInformingTask != null){
			Bukkit.getScheduler().cancelTask(playersActionBarInformingTask.getTaskId());
			informingObject = null;
			playersActionBarInformingTask = null;
			
		}
	}
	
	
	private PluginConfiguration c() {
		return this.plugin.config;
	}

	public void finalize() {
		stopActionbarInforming();
	}
	public Manager(MPoints plugin) throws AreaConstructorDataException{
		this.plugin = plugin;
		
		Map areas = this.plugin.config.getMap("Areas");
		Set areasKeys = areas.keySet();
		
		for(Object key: areasKeys){
			this.areas.add(new AreaController((Map)areas.get(key), this.plugin));
		}
		
		startActionbarInforming();
	}
}