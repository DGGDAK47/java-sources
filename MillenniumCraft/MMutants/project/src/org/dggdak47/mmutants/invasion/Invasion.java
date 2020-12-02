package org.dggdak47.mmutants.invasion;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.EntityType;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.mmutants.invasion.exceptions.InvasionConstructorDataException;
import org.dggdak47.mmutants.wayPoint.WayPoint;
import org.dggdak47.mmutants.wayPoint.exceptions.WayPointConstructorDataException;

public class Invasion {
	private String name;
	private Integer id;
	private Hashtable<EntityType, Integer> attackers;
	private String worldName;
	private ArrayList<WayPoint> way;
	
	
	
	public String getName() {
		return this.name;
	}
	public Integer getId() {
		return this.id;
	}
	public Hashtable<EntityType, Integer> getAttackers(){
		return (Hashtable)this.attackers.clone();
	}
	public String getWorldName() {
		return this.worldName;
	}
	public ArrayList<WayPoint> getWay(){
		return (ArrayList)this.way.clone();
	}
	
	
	
	private void incorrectData(String fieldName) throws InvasionConstructorDataException{
		throw new InvasionConstructorDataException(fieldName, InvasionConstructorDataException.ExceptionType.INCORRECT_DATA);
	}
	private void nullChecker(Object ob, String fieldName) throws InvasionConstructorDataException{
		if(ob == null){
			throw new InvasionConstructorDataException(fieldName, InvasionConstructorDataException.ExceptionType.NULL);
		}
	}
	public Invasion(Map invasion) throws InvasionConstructorDataException{
		Object ob;
		
		
		//Name
		ob = invasion.get("Name");
		nullChecker(ob, "Name");
		name = (String)ob;
		
		
		//Id
		ob = invasion.get("Id");
		nullChecker(ob, "Id");
		try{
			id = new Integer((String)ob);
		}catch(Exception e){
			incorrectData("Id");
		}
		
		
		//Attackers
		ob = invasion.get("Attackers");
		nullChecker(ob, "Attackers");
		if(!(ob instanceof ArrayList)){
			incorrectData("Attackers not list");
		}
		try{
			ArrayList<String> attackers = (ArrayList)ob;
			Hashtable<String, String> splitedAttackers = DUtil.splitArrayList(attackers, '|');
			
			this.attackers = new Hashtable<EntityType, Integer>();
			
			Set<String> keys = splitedAttackers.keySet();
			String value;
			for(String key: keys){
				value = splitedAttackers.get(key);
				
				this.attackers.put(EntityType.valueOf(key), new Integer(value));
			}
		}catch(Exception e){
			incorrectData("Attackers");
		}
		
		
		//WorldName
		ob = invasion.get("WorldName");
		nullChecker(ob, "WorldName");
		this.worldName = (String)ob;
		
		
		//Way
		ob = invasion.get("Way");
		nullChecker(ob, "Way");
		if(!(ob instanceof Map)){
			incorrectData("Way");
		}
		String wayPointMapKey = "null";
		try{
			this.way = new ArrayList<WayPoint>();
			Map way = (Map)ob;
			Set<String> wayKeys = way.keySet();
			
			Map wayPointMap;
			for(String key: wayKeys) {
				wayPointMapKey = key;
				wayPointMap = (Map)way.get(key);
				this.way.add(new WayPoint(wayPointMap, this.worldName));
			}
		}catch(WayPointConstructorDataException e){
			incorrectData("Way -> "+wayPointMapKey+" -> "+e.getFieldName());
		}
		
		
	}
}
