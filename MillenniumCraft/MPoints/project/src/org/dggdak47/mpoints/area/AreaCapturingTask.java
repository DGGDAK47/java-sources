package org.dggdak47.mpoints.area;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.dggdak47.config.PluginConfiguration;
import org.dggdak47.mfractions.MFractions;
import org.dggdak47.mfractions.MFractionsAPI;
import org.dggdak47.mfractions.fraction.Fraction;
import org.dggdak47.mfractions.fraction.FractionPlayer;
import org.dggdak47.mpoints.MPoints;
import org.dggdak47.mpoints.events.AreaCapturedEvent;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class AreaCapturingTask implements Runnable{
	private AreaController controller;
	private CapturingText ct;
	
	public final Integer maximumPrecents = 100;
	public final Integer addingPrecentsPerUnBlockedCapture = 10;
	
	private Integer capturingPrecents = 0;
    public Integer getCapturingPrecents() {
		return this.capturingPrecents;
	}
	
	@Override
	public void run() {
		ArrayList<Player> captingPlayers = this.controller.getCapturingPlayers();
		if(captingPlayers.size() <= 0){
			this.controller.removeCapturing(true);
			return;
		}
		
		ArrayList<FractionPlayer> fCaptingPlayers = new ArrayList<FractionPlayer>();
		FractionPlayer fp;
		for(Player p: captingPlayers){
			fp = this.controller.plugin.mfractionsApi.getPlayer(p);
			if(fp != null){
				fCaptingPlayers.add(fp);
			}
		}
		
		Boolean isPlayersInSameFraction = isAllPlayersInSameFraction(fCaptingPlayers);
		String message;
		
		if((this.capturingPrecents > this.maximumPrecents) && isPlayersInSameFraction){
			//«хават закончен
			Fraction fractionWithinCapturingArea = this.controller.plugin.mfractionsApi.getFraction(fCaptingPlayers.get(0).getFractionID());
			Fraction owners = this.controller.getOwners();
			if(owners != null && fractionWithinCapturingArea.getId().equals(owners.getId())){
				this.controller.removeCapturing(false);
				return;
			}
			
			this.controller.areaCaptured(fractionWithinCapturingArea, captingPlayers);
			this.controller.removeCapturing(false);
			Bukkit.getPluginManager().callEvent(new AreaCapturedEvent((ArrayList)captingPlayers.clone(), controller.area));
			return;
		}
		
		if(isPlayersInSameFraction){
			if((this.controller.getOwners() != null) && fCaptingPlayers.get(0).getFractionID().equals(this.controller.getOwners().getId())){
				this.controller.removeCapturing(true);
				return;
			}
			
			message = createCapturingMessage(false);
			sendActionBarMessage(captingPlayers, message);
			
			this.capturingPrecents += this.addingPrecentsPerUnBlockedCapture;
		}else{
			message = createCapturingMessage(true);
			sendActionBarMessage(captingPlayers, message);
		}
		
		
	}
	
	
	public String createCapturingMessage(boolean isBlocked) {
		String toReturn = "";
		
		toReturn += this.ct.capturingPrefix;
		if(this.capturingPrecents <= 0){
			for(int i = 0; i < 10; i++){
				toReturn += this.ct.uncapturedSymbol;
			}
		}else if(this.capturingPrecents >= 100){
			for(int i = 0; i < 10; i++){
				if(isBlocked){
					toReturn += this.ct.capturingBlockedSymbol;
				}else {
					toReturn += this.ct.capturedSymbol;
				}
			}
		}else{
			Integer remainingIterationsToCapture = (this.maximumPrecents-this.capturingPrecents)/10;
			Integer capturedIterations = this.capturingPrecents/10;
			for(int i = 0; i < capturedIterations; i++){
				if(isBlocked){
					toReturn += this.ct.capturingBlockedSymbol;
				}else{
					toReturn += this.ct.capturedSymbol;
				}
			}
			
			for(int i = 0; i < remainingIterationsToCapture; i++){
				toReturn += this.ct.uncapturedSymbol;
			}
		}
		
		toReturn += this.ct.capturingSuffix;
		return toReturn;
	}
	public void sendActionBarMessage(ArrayList<Player> recipients, String message) {
		for(Player p: recipients){
			try{
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
			}catch(Exception e){
				//nothing
			}
		}
	}
	public static boolean isAllPlayersInSameFraction(ArrayList<FractionPlayer> fPlayers) {
		for(FractionPlayer fp: fPlayers){
			for(FractionPlayer fp2: fPlayers){
				if(fp.getPlayerName().equals(fp2.getPlayerName())){
					continue;
				}else if(!fp.getFractionID().equals(fp2.getFractionID())){
					return false;
				}
			}
		}
		
		return true;
	}
	public AreaCapturingTask(AreaController controller, CapturingText ct){
		this.controller = controller;
		this.ct = ct;
	}
}
