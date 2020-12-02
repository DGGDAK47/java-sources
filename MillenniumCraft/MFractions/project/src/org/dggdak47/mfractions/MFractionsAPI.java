package org.dggdak47.mfractions;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.dggdak47.manager.exceptions.FractionNotExistException;
import org.dggdak47.manager.exceptions.PlayerNotExistException;
import org.dggdak47.mfractions.fraction.Fraction;
import org.dggdak47.mfractions.fraction.FractionPlayer;
import org.dggdak47.mfractions.point.Point;
import org.dggdak47.mfractions.point.exceptions.PointConfigPropertyException;
import org.dggdak47.mfractions.point.exceptions.PointWithSameIdAlreadyExistsException;


public class MFractionsAPI {
	private MFractions plugin;
	
	public boolean hasPluginStartedSuccessfully() {
		return this.plugin.hasPluginStartedSuccessfully;
	}
	
	//Fractions
	public boolean hasFraction(Integer id){
		return this.plugin.manager.hasFraction(id);
	}
    public Fraction getFraction(Integer id) {
    	return this.plugin.manager.getFraction(id);
    }
    public ArrayList<Fraction> getFractions(Integer id){
    	return this.plugin.manager.getFractions();
    }
	
    //FractionPlayer
    public boolean hasPlayer(String playerName) {
    	return this.plugin.manager.hasPlayer(playerName);
    }
    public boolean hasPlayer(Player player) {
    	return this.plugin.manager.hasPlayer(player);
    }
    public FractionPlayer getPlayer(String playerName) {
    	return this.plugin.manager.getPlayer(playerName);
    }
    public FractionPlayer getPlayer(Player player) {
    	return this.plugin.manager.getPlayer(player);
    }
    public void setPlayerFraction(String playerName, Integer newFractionID, Boolean annulScore) throws PlayerNotExistException, FractionNotExistException {
    	this.plugin.manager.setPlayerFraction(playerName, newFractionID, annulScore);
    }
    public void addScore(Player player, Integer score) throws PlayerNotExistException {
    	this.plugin.manager.addScore(player, score);
    }
    public ArrayList<Player> getBukkitPlayersByFraction(Fraction f){
    	return this.plugin.manager.getBukkitPlayersByFraction(f);
    }
    public Fraction getPlayerFraction(Player p) {
    	return this.plugin.manager.getPlayerFraction(p);
    }
    
    //Points
    public boolean hasPoint(Integer id) {
    	return this.plugin.manager.hasPoint(id);
    }
    public void createPoint(String properties) throws PointConfigPropertyException, PointWithSameIdAlreadyExistsException {
    	this.plugin.manager.createPoint(properties);
    }
    public Point getPoint(Integer id) {
    	return this.plugin.manager.getPoint(id);
    }
    public ArrayList<Point> getPoints(Boolean andFractions){
    	return this.plugin.manager.getPoints(andFractions);
    }
    
    //Permissions
    public boolean hasWithdrawedPermsForPlayer(Player p) {
    	return this.plugin.manager.hasWithdrawedPermsForPlayer(p);
    }
    public boolean withdrawPermissionGroup(Player p, String groupName) {
    	return this.plugin.manager.withdrawPermissionGroup(p, groupName);
    }
    public void withdrawPermissionGroups(Player p, ArrayList<String> groupsToWithdraw) {
    	this.plugin.manager.withdrawPermissionGroups(p, groupsToWithdraw);
    }
    public void withdrawPermissionGroupsNot(Player p, ArrayList<String> groupsNameNotToWithdrawing) {
    	this.plugin.manager.withdrawPermissionGroupsNot(p, groupsNameNotToWithdrawing);
    }
    public boolean returnWithdrawedPermissionGroups(Player p) {
    	return this.plugin.manager.returnWithdrawedPermissionGroups(p);
    }
    
    public void resetPermissionGroups(Player p, Boolean fullReset) {
    	this.plugin.manager.resetPermissionGroups(p, fullReset);
    }
    
    public boolean friendlyFire() {
    	return this.plugin.manager.friendlyFire();
    }
    
	public MFractionsAPI(MFractions plugin){
		this.plugin = plugin;
	}
}
