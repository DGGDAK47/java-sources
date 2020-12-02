package org.dggdak47.mfractions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.dggdak47.database.sqlite.PluginDB;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.manager.exceptions.FractionNotExistException;
import org.dggdak47.manager.exceptions.PlayerNotExistException;
import org.dggdak47.mfractions.events.PermissionGroupsResetEvent;
import org.dggdak47.mfractions.events.PlayerAddedToPlayersListEvent;
import org.dggdak47.mfractions.events.PlayerChangeFractionEvent;
import org.dggdak47.mfractions.events.PlayerRemovedFromPlayersListEvent;
import org.dggdak47.mfractions.events.PrePermissionGroupsResetEvent;
import org.dggdak47.mfractions.fraction.Fraction;
import org.dggdak47.mfractions.fraction.FractionPlayer;
import org.dggdak47.mfractions.fraction.exceptions.FractionConfigPropertyException;
import org.dggdak47.mfractions.fraction.exceptions.FractionDataException;
import org.dggdak47.mfractions.point.Point;
import org.dggdak47.mfractions.point.exceptions.PointConfigPropertyException;
import org.dggdak47.mfractions.point.exceptions.PointWithSameIdAlreadyExistsException;

public class Manager {
	private MFractions plugin;
	private ArrayList<Fraction> fractions = new ArrayList<Fraction>();
	protected ArrayList<FractionPlayer> onlinePlayers = new ArrayList<FractionPlayer>();
	protected ArrayList<Point> points = new ArrayList<Point>();
	private PluginDB db;
	private Boolean friendlyFire;
	
	private final String defaultPlayerTableCreatingQuerySQLite = "CREATE TABLE IF NOT EXISTS Players ( PlayerName TEXT NOT NULL PRIMARY KEY, FractionID INTEGER NOT NULL, PlayerScore INTEGER NOT NULL);";
	
	//Fractions
	public boolean hasFraction(Integer id){
		for(Fraction f: this.fractions) {
			if(f.getId().equals(id)){
				return true;
			}
		}
		
		return false;
	}
	public Fraction getFraction(Integer id) {
		for(Fraction f: this.fractions) {
			if(f.getId().equals(id)){
				return f.clone();
			}
		}
		
		return null;
	}
	public ArrayList<Fraction> getFractions(){
		ArrayList<Fraction> toReturn = new ArrayList<Fraction>();
		
		for(Fraction f: this.fractions){
			toReturn.add(f.clone());
		}
		
		return toReturn;
	}
	
	//FractionPlayer
	public boolean hasPlayer(String playerName){
		for(FractionPlayer fp: onlinePlayers){
			if(fp.getPlayerName().equals(playerName)){
				return true;
			}
		}
		
		return false;
	}
	public boolean hasPlayer(Player player) {
		String name = player.getName();
		
		for(FractionPlayer fp: onlinePlayers){
			if(fp.getPlayerName().equals(name)){
				return true;
			}
		}
		
		return false;
	}
	public FractionPlayer getPlayer(String playerName) {
		for(FractionPlayer fp: onlinePlayers){
			if(fp.getPlayerName().equals(playerName)){
				return fp;
			}
		}
		
		return null;
	}
    public FractionPlayer getPlayer(Player player) {
    	String name = player.getName();
    	
    	for(FractionPlayer fp: onlinePlayers){
    		
			if(fp.getPlayerName().equals(name)){
				return fp;
			}
		}
    	
		return null;
	}
    public void setPlayerFraction(String playerName, Integer newFractionID, Boolean annulScore) throws PlayerNotExistException, FractionNotExistException{
    	FractionPlayer player = getPlayer(playerName);
    	Player bukkitPlayer = Bukkit.getPlayer(playerName);
    	
    	if(player == null || bukkitPlayer == null){
    		throw new PlayerNotExistException("Player with nickname:("+playerName+") has not been found in the MFractions players list or on the server!", playerName);
    	}else{
    		Fraction newFraction = getFraction(newFractionID);
    		if(newFraction == null){
    			throw new FractionNotExistException("Fraction with id: "+newFractionID+" doesn't exist!", newFractionID);
    		}
    		
    		
    		player.setFractionID(newFraction.getId());
    		if(annulScore){
    			player.setScore(0);
    		}
    		player.setSpawnPointID(newFraction.getPoint().getId());
    		bukkitPlayer.teleport(newFraction.getPoint().getLocation());
    		resetPermissionGroups(bukkitPlayer, false);
    		
    		Bukkit.getPluginManager().callEvent(new PlayerChangeFractionEvent(bukkitPlayer, newFraction));
    	}
    }
    public void addScore(Player player, Integer score) throws PlayerNotExistException {
    	FractionPlayer fp = getPlayer(player);
    	
    	if(fp == null){
    		throw new PlayerNotExistException("Player with name: "+player.getName()+" doesn't exist in the onlinePlayers variable!", player.getName());
    	}else {
    		fp.addScore(score);
    	}
    }
    //---undocumented---
    public ArrayList<Player> getBukkitPlayersByFraction(Fraction f){
    	ArrayList<Player> toReturn = new ArrayList<Player>();
    	Player p;
    	
    	for(FractionPlayer fp: onlinePlayers){
    		p = Bukkit.getPlayer(fp.getPlayerName());
    		if(p != null && fp.getFractionID().equals(f.getId())){
    			toReturn.add(p);
    		}
    	}
    	
    	return toReturn;
    }
    public Fraction getPlayerFraction(Player p) {
    	FractionPlayer fp = getPlayer(p);
    	if(fp != null){
    		return getFraction(fp.getFractionID());
    	}
    	
    	return null;
    }
    
    //DB Players
    public void addPlayer(Player player) {
		String query = "SELECT * FROM Players WHERE PlayerName = '"+player.getName()+"';";
		ResultSet rs;
		Boolean hasResult = false;
		
		try {
			rs = db.executeQuery(query);
			FractionPlayer fPlayer;
			
			while(rs.next()) {
				hasResult = true;
				
				fPlayer = new FractionPlayer(rs.getString("PlayerName"), rs.getInt("FractionID"), rs.getInt("PlayerScore"), this.getFraction(rs.getInt("FractionID")).getPoint().getId() );
				onlinePlayers.add(fPlayer );
				break;
			}
			if(!hasResult){
				String query2 = "INSERT INTO Players VALUES('"+player.getName()+"', 0, 0);";
				db.executeUpdate(query2);
				
				fPlayer = new FractionPlayer(player.getName(), 0, 0, this.getFraction(0).getPoint().getId());
				
				onlinePlayers.add(fPlayer);
			}
			
			resetPermissionGroups(player, false);
			
			rs.close();
			
			Bukkit.getPluginManager().callEvent(new PlayerAddedToPlayersListEvent(player) );
		} catch (SQLException e) {
			player.kickPlayer("DB query ERROR!");
			e.printStackTrace();
		}
	}
	public void removePlayer(Player player) throws PlayerNotExistException, SQLException {
		
		FractionPlayer fPlayer = getPlayer(player);
		
		if(fPlayer == null){
			throw new PlayerNotExistException("", player.getName());
		}
		
		String query = "UPDATE Players SET FractionID = "+fPlayer.getFractionID()+", PlayerScore = "+fPlayer.getScore()+" WHERE PlayerName = '"+fPlayer.getPlayerName()+"'";
		onlinePlayers.remove(fPlayer);
		
		returnWithdrawedPermissionGroups(player);
		
		db.executeUpdate(query);
		
		Bukkit.getPluginManager().callEvent(new PlayerRemovedFromPlayersListEvent(player));
	}
    
	
    //Points
	public boolean hasPoint(Integer id){
		for(Point point: points){
    		if(point.getId().equals(id)){
    			return true;
    		}
    	}
    	for(Fraction fraction: fractions){
    		if(fraction.getPoint().getId().equals(id)){
    			return true;
    		}
    	}
		
		return false;
	}
    public void createPoint(String properties) throws PointConfigPropertyException, PointWithSameIdAlreadyExistsException{
    	Point pointToAdd = new Point(properties);
    	
    	if(!hasPoint(pointToAdd.getId())){
    		this.points.add(pointToAdd);
    	}else {
    		throw new PointWithSameIdAlreadyExistsException("Point with id: "+pointToAdd.getId()+" already exists!");
    	}
    	
    }
    public Point getPoint(Integer id){
    	for(Point point: points){
    		if(point.getId().equals(id)){
    			return point.clone();
    		}
    	}
    	for(Fraction fraction: fractions){
    		if(fraction.getPoint().getId().equals(id)){
    			return fraction.getPoint().clone();
    		}
    	}
    	
    	return null;
    }
    public ArrayList<Point> getPoints(Boolean andFractions){
    	ArrayList<Point> toReturn = new ArrayList<Point>();
    	
    	if(andFractions){
    		for(Fraction fraction: fractions){
        		toReturn.add(fraction.getPoint().clone());
        	}
    	}
    	for(Point point: points){
    		toReturn.add(point.clone());
    	}
    	
    	
    	return toReturn;
    }
    
    //Permissions
    //withdrawedPermissionGroups -> { "PlayerName":"Group1|Group2...|GroupN", ...}
    protected Hashtable<String, String> withdrawedPermissionGroups = new Hashtable<String, String>();;
    public boolean hasWithdrawedPermsForPlayer(Player p) {
    	if( withdrawedPermissionGroups.get(p.getName()) != null ){
    		return true;
    	}else {
    		return false;
    	}
    }
    public boolean withdrawPermissionGroup(Player p, String groupName){
    	Boolean hasGroup = DUtil.hasPermissionGroup(p, groupName);
    	Boolean isPlayerInWithdrawedList = hasWithdrawedPermsForPlayer(p);
    	
    	if(isPlayerInWithdrawedList){
    		if(hasGroup){
    			String withdrawedPlayerGroups = withdrawedPermissionGroups.get(p.getName());
        		withdrawedPlayerGroups += "|"+groupName;
        		DUtil.removePermissionGroup(p, groupName);
        		withdrawedPermissionGroups.put(p.getName(), withdrawedPlayerGroups);
        		
        		return true;
    		}else {
    			return false;
    		}
    	}else{
    		if(hasGroup){
    			DUtil.removePermissionGroup(p, groupName);
    			withdrawedPermissionGroups.put(p.getName(), groupName);
    			
    			return true;
    		}else {
    			return false;
    		}
    	}
    	
    }
    public void withdrawPermissionGroups(Player p, ArrayList<String> groupsToWithdraw){
    	for(String group: groupsToWithdraw){
    		withdrawPermissionGroup(p, group);
    	}
    }
    public void withdrawPermissionGroupsNot(Player p, ArrayList<String> groupsNameNotToWithdrawing){
    	ArrayList<String> allPlayerGroups = DUtil.getAllPlayerPermissionGroups(p);
    	ArrayList<String> permGroupsToWithdraw = new ArrayList<String>();
    	
    	for(String group: allPlayerGroups){
    		if( !DUtil.hasList(group, (List)groupsNameNotToWithdrawing) ){
    			permGroupsToWithdraw.add(group);
    		}
    	}
    	
    	for(String group: permGroupsToWithdraw){
    		withdrawPermissionGroup(p, group);
    	}
    }
    public boolean returnWithdrawedPermissionGroups(Player p) {
    	Boolean isPlayerInWithdrawedList = hasWithdrawedPermsForPlayer(p);
    	if(isPlayerInWithdrawedList){
    		ArrayList<String> groups = DUtil.split( withdrawedPermissionGroups.get(p.getName()) , '|');
    		
    		for(String group: groups){
    			DUtil.addPermissionGroup(p, group);
    		}
    		withdrawedPermissionGroups.remove(p.getName());
    		return true;
    	}else {
    		return false;
    	}
    }
    
    public void resetPermissionGroups(Player p, Boolean fullReset) {
    	PrePermissionGroupsResetEvent preResetEvent = new PrePermissionGroupsResetEvent(p);
    	Bukkit.getPluginManager().callEvent(preResetEvent);
    	
    	ArrayList<String> groups = new ArrayList<String>();
    	
    	ArrayList<String> defaultGroups = this.plugin.config.getListS("GeneralOptions.DefaultPermGroupsForEachPlayer");
    	groups.addAll(defaultGroups);
    	
    	Fraction playerFraction = this.getFraction(this.getPlayer(p).getFractionID());
    	groups.addAll(playerFraction.getPermissionGroups());
    	
    	if(!fullReset){
    		ArrayList<String> groupsFromPreResetEventObject = preResetEvent.getGroups();
        	if(groupsFromPreResetEventObject.size() != 0){
        		groups.addAll(groupsFromPreResetEventObject);
        	}
    	}
    	
    	DUtil.setPermissionGroups(p, groups);
    	Bukkit.getPluginManager().callEvent(new PermissionGroupsResetEvent(p));
    }
    
    protected void disabling() {
    	Player p;
    	FractionPlayer fp;
    	
    	ArrayList onlinePlayersClone = (ArrayList) this.onlinePlayers.clone();
    	
    	for(Object ob: onlinePlayersClone){
    		fp = (FractionPlayer)ob;
    		p = Bukkit.getPlayer(fp.getPlayerName());
    		
    		try {
				removePlayer(p);
			} catch (PlayerNotExistException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    public boolean friendlyFire() {
    	return this.friendlyFire;
    }
    public void show() {
    	Logger l = Bukkit.getLogger();
    	
    	l.info("------onlinePlayers------");
    	for(FractionPlayer player: onlinePlayers){
    		l.info("Name: "+player.getPlayerName()+" Score: "+player.getScore()+" FractionID: "+player.getFractionID()+" SpawnPointID: "+player.getSpawnPointID());
    	}
    }
	public Manager(MFractions plugin) throws FractionConfigPropertyException, SQLException{
		this.plugin = plugin;
		
		Map fractions = this.plugin.config.getMap("Fractions");
		
		Set<String> keys = fractions.keySet();
		for(String fractionName: keys){
			Map fraction = (Map)fractions.get(fractionName);
			
			this.fractions.add( new Fraction(fraction) );
			
		}
		
		this.db = new PluginDB("MFractions", "pluginDB.db");
		this.db.execute(defaultPlayerTableCreatingQuerySQLite);
		
		this.friendlyFire = this.plugin.config.getBoolean("GeneralOptions.FriendlyFire");
	}
}
