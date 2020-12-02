package org.dggdak47.mfractions;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.dggdak47.manager.exceptions.PlayerNotExistException;
import org.dggdak47.mfractions.events.PreRespawningEvent;
import org.dggdak47.mfractions.fraction.Fraction;
import org.dggdak47.mfractions.fraction.FractionPlayer;
import org.dggdak47.mfractions.point.Point;

import com.shampaggon.crackshot.events.*;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.events.LoginEvent;


public class ManagerEvents implements Listener{
	private MFractions plugin;
	
	//PlayersInteraction
	@EventHandler
	public void onPlayerShoted(WeaponDamageEntityEvent e) {
		Entity eVictim = e.getVictim();
		Entity eDamager = e.getDamager();
		
        if( !(eVictim instanceof Player) || !(eDamager instanceof Player) ){ return; }
		
		Player victim = (Player)eVictim;
		Player damager = (Player)eDamager;
		
		FractionPlayer fVictim = this.plugin.manager.getPlayer(victim);
		FractionPlayer fDamager = this.plugin.manager.getPlayer(damager);
		if(fVictim == null || fDamager == null || fVictim.getPlayerName().equals(fDamager.getPlayerName()) ){ return; }
		
		if(fVictim.getFractionID().equals(fDamager.getFractionID()) && !this.plugin.manager.friendlyFire()){
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerDamaged(EntityDamageByEntityEvent e) {
		Entity eVictim = e.getEntity();
		Entity eDamager = e.getDamager();
		if( !(eVictim instanceof Player) || !(eDamager instanceof Player) ){ return; }
		
		Player victim = (Player)eVictim;
		Player damager = (Player)eDamager;
		
		FractionPlayer fVictim = this.plugin.manager.getPlayer(victim);
		FractionPlayer fDamager = this.plugin.manager.getPlayer(damager);
		if(fVictim == null || fDamager == null){ return; }
		
		if(fVictim.getFractionID().equals(fDamager.getFractionID()) && !this.plugin.manager.friendlyFire()){
			e.setCancelled(true);
		}
	}
	
	//Player Join/Quit
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if(!this.plugin.manager.hasPlayer(e.getPlayer())){
			return;
		}
		
		if(this.plugin.manager.hasWithdrawedPermsForPlayer(e.getPlayer())){
			this.plugin.manager.returnWithdrawedPermissionGroups(e.getPlayer());
		}
		try {
			this.plugin.manager.removePlayer(e.getPlayer());
		} catch (PlayerNotExistException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	@EventHandler
	public void onPlayerLogin(LoginEvent e) {
		this.plugin.manager.addPlayer(e.getPlayer());
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if(!AuthMe.getApi().isRegistered(p.getName())) {
			p.teleport(this.plugin.manager.getFraction(0).getPoint().getLocation());
		}
	}
	
	//Player Spawning
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		PreRespawningEvent preResp = new PreRespawningEvent(e.getPlayer());
		Bukkit.getPluginManager().callEvent(preResp);
		
		Player p = e.getPlayer();
		FractionPlayer fp = this.plugin.manager.getPlayer(p);
		
		if(fp == null) { 
			this.plugin.manager.addPlayer(p);
		}
		
		Point point = this.plugin.manager.getPoint(fp.getSpawnPointID());
		
		if(preResp.getNewLocation() != null){
			e.setRespawnLocation(preResp.getNewLocation());
		}else if(point != null){
			e.setRespawnLocation(point.getLocation());
		}else{
			Fraction fraction = this.plugin.manager.getFraction(fp.getFractionID());
			e.setRespawnLocation(fraction.getPoint().getLocation());
		}
	}
	
	//PlayerRewarding
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player dead = e.getEntity();
		EntityDamageEvent entityDamageInfo = dead.getLastDamageCause();
		if(entityDamageInfo == null){
			return;
		}
	    Entity damager = entityDamageInfo.getEntity();
		
	    if( damager == null || !(damager instanceof Player) ){ return; }
	    
	    FractionPlayer fDead = this.plugin.manager.getPlayer(dead);
	    FractionPlayer fDamager = this.plugin.manager.getPlayer((Player)damager);
	    
	    if(fDead == null || fDamager == null || fDead.getPlayerName().equals(fDamager.getPlayerName())){ return; }
	    
	    String message = String.format( this.plugin.config.getString("Text.KillingMessage"), dead.getName());
	    
	    fDamager.addScore(1);
	    damager.sendMessage(message);
	}
	
	public ManagerEvents(MFractions plugin){
		this.plugin = plugin;
	}
}
