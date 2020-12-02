package org.dggdak47.dutil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.dggdak47.database.sqlite.PluginDB;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class DUtil extends JavaPlugin{
	@Override
	public void onEnable() {
		
		
		Bukkit.getLogger().info("[DUtil] Enabled!");
	}
	public static ArrayList<String> split(String toSplit, Character separator) {
		ArrayList<String> toReturn = new ArrayList<String>();
		
		String element = "";
		Character ch;
		
		for(int i = 0; i < toSplit.length(); i++) {
			ch = toSplit.charAt(i);
			
			if(ch.equals(separator)){
				toReturn.add(element);
				element = "";
				continue;
			}else if(toSplit.length()-1 == i){
				element += ch;
				toReturn.add(element);
				continue;
			}else{
				element += ch;
			}
		}
		
		return toReturn;
	}
	public static ArrayList<String> splitByFirstEntering(String toSplit, Character symbol){
		ArrayList<String> toReturn = new ArrayList<String>();
		
		String s = "";
		Character ch;
		Boolean hasSymbolEntered = false;
		
		for(int i = 0; i < toSplit.length(); i++){
			ch = new Character(toSplit.charAt(i));
			
			if(ch.equals(symbol) && !hasSymbolEntered){
				toReturn.add(s);
				s = "";
				hasSymbolEntered = true;
				continue;
			}else{
				s += ch;
			}
			
			if(toSplit.length()-1 == i){
				toReturn.add(s);
			}
		}
		
		return toReturn;
	}
	public static ArrayList<String> splitByNthEntering(String toSplit, Character symbol, Integer numberEntering){
		ArrayList<String> toReturn = new ArrayList<String>();
		
		// asd!asd -> [asd, asd]  - numberEntering = 1
		// asd!asd!asd - > [asd!asd, asd] - numberEntering = 2
		Integer symbolEnteredNumber = 0;
		String assemblingText = "";
		Character ch;
		
		for(int i = 0; i < toSplit.length(); i++){
			ch = toSplit.charAt(i);
			
			if(ch.equals(symbol)){
				symbolEnteredNumber++;
				if(symbolEnteredNumber.equals(numberEntering)){
					toReturn.add(assemblingText);
					assemblingText = "";
					continue;
				}
			}
			
			assemblingText += ch;
			if(i == toSplit.length()-1){
				toReturn.add(assemblingText);
			}
		}
		
		return toReturn;
	}
	
	public static Hashtable<String, String> splitArrayList(ArrayList<String> toSplit, Character separator){
		Hashtable<String, String> toReturn = new  Hashtable<String, String>();
		
		ArrayList<String> splitedPair;
		
		for(String pair: toSplit){
			//pair -> "a:b" where (:) is separator local variable
			splitedPair = split(pair, separator);
			toReturn.put(splitedPair.get(0), splitedPair.get(1));
		}
		
		return toReturn;
	}
	public static Hashtable<String, String> splitStringOnPairs(String toSplit, Character pairSpliter, Character keyValueSpliter){
		ArrayList<String> pairs = DUtil.split(toSplit, pairSpliter);
		
		// pairs -> ["a:b", "c:d", "g:k"] where (:) symbol is a keyValueSpliter local variable
		Hashtable<String, String> toReturn = new Hashtable<String, String>();
		
		ArrayList<String> splitedKeyValue;
		for(String pair: pairs){
			splitedKeyValue = DUtil.split(pair, keyValueSpliter);
			toReturn.put(splitedKeyValue.get(0), splitedKeyValue.get(1));
		}
		
		return toReturn;
	}
	
	public static boolean hasPermissionPEX(Player p, String perm){
		PermissionUser pu = PermissionsEx.getUser(p);
		
		return pu.has(perm);
	}
	public static boolean hasList(Object ob, List<Object> list) {
		for(Object o: list){
			if(ob.equals(o)){
				return true;
			}
		}
		
		return false;
	}
	
	
	public static boolean hasPermissionGroup(Player p, String groupName) {
		PermissionUser pu = PermissionsEx.getUser(p);
		
		String[] playerGroupNames = pu.getGroupNames();
		for(String group: playerGroupNames){
			if(group.equals(groupName)){
				return true;
			}
		}
		
		return false;
	}
	public static void setPermissionGroups(Player p, ArrayList<String> groups) {
		PermissionUser pu = PermissionsEx.getUser(p);
		
		String[] arrGroups = new String[groups.size()];
		for(int i = 0; i < groups.size(); i++){
			arrGroups[i] = groups.get(i);
		}
		pu.setGroups(arrGroups);
	}
    public static void removePermissionGroup(Player p, String groupName) {
    	PermissionUser pu = PermissionsEx.getUser(p);
    	
    	pu.removeGroup(groupName);
    }
    public static void addPermissionGroup(Player p, String groupName) {
    	PermissionUser pu = PermissionsEx.getUser(p);
    	pu.addGroup(groupName);
    }
    public static void addPermissionGroupsTop(Player p, ArrayList<String> groups) {
    	PermissionUser pu = PermissionsEx.getUser(p);
    	for(String group: groups){
    		pu.addGroup(group);
    	}
    }
    public static void addPermissionGroupsBottom(Player p, ArrayList<String> groups) {
    	if(new Integer(groups.size()).equals(0)){
    		return;
    	}
    	PermissionUser pu = PermissionsEx.getUser(p);
    	String[] allGroupNames = pu.getGroupNames();
    	String[] toSet = ArrayUtil.add(allGroupNames, ArrayUtil.objectArratToString(groups.toArray()));
    	pu.setGroups(toSet);
    }
    public static ArrayList<String> getAllPlayerPermissionGroups(Player p) {
    	PermissionUser pu = PermissionsEx.getUser(p);
    	
    	String[] playerGroups = pu.getGroupNames();
    	ArrayList<String> toReturn = new ArrayList<String>();
    	for(String s: playerGroups){
    		toReturn.add(s);
    	}
    	
    	return toReturn;
    }
    public static boolean hasPermissinGroups(Player p, ArrayList<String> groups) {
    	for(String group: groups){
    		if(!DUtil.hasPermissionGroup(p, group)){
    			return false;
    		}
    	}
    	
    	return true;
    }
}