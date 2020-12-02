package org.dggdak47.mpoints.area;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.dggdak47.config.PluginConfiguration;
import org.dggdak47.mfractions.fraction.Fraction;
import org.dggdak47.mpoints.MPoints;
import org.dggdak47.mpoints.Manager;
import org.dggdak47.mpoints.area.exceptions.AreaConstructorDataException;
import org.dggdak47.mpoints.events.AreaCapturingAbortedEvent;
import org.dggdak47.mpoints.events.AreaCapturingBeganEvent;

public class AreaController {
	public final Area area;
	public MPoints plugin;
	
	//Within Area Players
	private ArrayList<Player> playersWithinArea = new ArrayList<Player>();
	public boolean isEmpty() {
		return this.playersWithinArea.isEmpty();
	}
    public boolean hasPlayer(Player p){
		for(Player pwa: playersWithinArea){
			if(p.equals(pwa)){
				return true;
			}
		}
		
		return false;
	}
	public void addPlayer(Player p){
		if(!hasPlayer(p)){
			playersWithinArea.add(p);
		}
	}
	public void removePlayer(Player p) {
		playersWithinArea.remove(p);
	}
	
	//Capturing
	private Fraction areaOwners = null;
	private BukkitTask capturingTask;
	private AreaCapturingTask capturingTaskObject;
	
	public void areaCaptured(Fraction owners, ArrayList<Player> invaders){
		this.areaOwners = owners;
		String message = this.plugin.config.getString("Messages.Formatting.AreaCaptured");
		message = String.format(message, area.getName());
		this.capturingTaskObject.sendActionBarMessage(invaders, message);
		
		message = this.plugin.config.getString("Messages.Formatting.RewardingMessage");
		message = String.format(message, area.getRewardingScore(), area.getName());
		for(Player p: invaders){
			try {
				this.plugin.mfractionsApi.addScore(p, area.getRewardingScore());
				p.sendMessage(message);
			}catch(Exception e){
				//nothing
			}
		}
	}
	public Boolean isAreaCapturing(){
		if(capturingTask == null){
			return false;
		}
		return true;
	}
	public void startCapturingArea(MPoints plugin) {
		if(capturingTask != null){
			Bukkit.getScheduler().cancelTask(capturingTask.getTaskId());
		}
		
		this.capturingTaskObject = new AreaCapturingTask(this, getCapturingText(plugin.config));
		this.capturingTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this.capturingTaskObject, 20, 15);
		Bukkit.getPluginManager().callEvent(new AreaCapturingBeganEvent(area));
	}
	public void removeCapturing(Boolean isAborted) {
		if(capturingTask != null){
			Bukkit.getScheduler().cancelTask(capturingTask.getTaskId());
			capturingTask = null;
			capturingTaskObject = null;
			
			if(isAborted){
				Bukkit.getPluginManager().callEvent(new AreaCapturingAbortedEvent(area));
			}
		}
	}
	
    public Fraction getOwners() {
		return this.areaOwners;
	}
	public ArrayList<Player> getCapturingPlayers(){
		ArrayList<Player> players = new ArrayList<Player>();
		
		for(Player p: playersWithinArea){
			if(area.getCapturingRegion().isLocationWithin(p.getLocation())){
				players.add(p);
			}
		}
		
		return players;
	}
	public ArrayList<Player> getNotCapturingPlayers(){
		if(!this.isAreaCapturing()){
			return (ArrayList)this.playersWithinArea.clone();
		}
		
		ArrayList<Player> toReturn = (ArrayList)this.playersWithinArea.clone();
		ArrayList<Player> capturingPlayers = this.getCapturingPlayers();
		
		for(Player p: capturingPlayers){
			toReturn.remove(p);
		}
		
		return toReturn;
	}
	
	public static CapturingText getCapturingText(PluginConfiguration config){
		String capturedSymbol = config.getString("Symbols.CapturingProgressSymbols.Captured");
		String uncapturedSymbol = config.getString("Symbols.CapturingProgressSymbols.UnCaptured");
		String capturingBlockedSymbol = config.getString("Symbols.CapturingProgressSymbols.CapturingBlocked");
		String capturingPrefix = config.getString("Symbols.CapturingProgressSymbols.CapturingPrefix");
		String capturingSuffix = config.getString("Symbols.CapturingProgressSymbols.CapturingSuffix");
		
		return new CapturingText(capturedSymbol, uncapturedSymbol, capturingBlockedSymbol, capturingPrefix, capturingSuffix);
	}
	public AreaController(Map area, MPoints plugin) throws AreaConstructorDataException{
		this.area = new Area(area);
		this.plugin = plugin;
	}
}
