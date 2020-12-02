package org.dggdak47.mmutants.wayPoint;

import java.util.Hashtable;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.mfractions.point.Point;
import org.dggdak47.mmutants.entityPoint.EntityPoint;
import org.dggdak47.mmutants.wayPoint.exceptions.WayPointConstructorDataException;
import org.dggdak47.mpoints.regions.Region;

public class WayPoint {
	private String worldName;
	private Point spawnPoint;
	private Region region;
	private EntityPoint target;
	private final static Integer rectangleBorderLength = 4;
	
	public String getWorldName() {
		return this.worldName;
	}
	public Point getSpawnPoint() {
		return this.spawnPoint;
	}
	public Region getRegion() {
		return this.region;
	}
	public EntityPoint getEntityPoint() {
		return this.target;
	}
	
	private void incorrectData(String fieldName) throws WayPointConstructorDataException{
		throw new WayPointConstructorDataException(fieldName, WayPointConstructorDataException.ExceptionType.INCORRECT_DATA);
	}
	private void nullChecker(Object ob, String fieldName) throws WayPointConstructorDataException{
		if(ob == null){
			throw new WayPointConstructorDataException(fieldName, WayPointConstructorDataException.ExceptionType.NULL);
		}
	}
	public WayPoint(Map wayPoint, String worldName) throws WayPointConstructorDataException{
		Object ob;
		
		//worldName
		this.worldName = worldName;
		
		//target
		ob = wayPoint.get("Target");
		nullChecker(ob, "Target");
		if( !(ob instanceof String) ){
			incorrectData("Target");
		}
		try{
			String targetData = (String)ob;
			String pairValue;
			Hashtable<String, String> unwrappedData = DUtil.splitStringOnPairs(targetData, '|', ':');
			//unwrapedData -> {"x":"0", "y":"1", "z":"2", "type":"CHICKEN"}
			
			Double x = null;
			Double y = null;
			Double z = null;
			EntityType entityType = null;
			
			//x
			pairValue = unwrappedData.get("x");
			try{
				x = new Double(pairValue);
			}catch(NumberFormatException e){
				incorrectData("Target - x");
			}
			
			//y
			pairValue = unwrappedData.get("y");
			try{
				y = new Double(pairValue);
			}catch(NumberFormatException e){
				incorrectData("Target - y");
			}
			
			//z
			pairValue = unwrappedData.get("z");
			try{
				z = new Double(pairValue);
			}catch(NumberFormatException e){
				incorrectData("Target - z");
			}
			
			//entityType
			pairValue = unwrappedData.get("type");
			try{
				entityType = EntityType.valueOf(pairValue);
			}catch(IllegalArgumentException e){
				incorrectData("Target - type");
			}
			
			this.target = new EntityPoint(new Location(Bukkit.getWorld(this.worldName), x, y, z), entityType);
		}catch(Exception e) {
			incorrectData("Target");
		}
		
		//SpawnPoint
		ob = wayPoint.get("SpawnPoint");
		nullChecker(ob, "SpawnPoint");
		if( !(ob instanceof String) ){
			incorrectData("SpawnPoint");
		}
		try{
			String pointConstructorData = (String)ob;
			pointConstructorData = this.worldName+":"+pointConstructorData+"|0";
			this.spawnPoint = new Point(pointConstructorData);
			
		}catch(Exception e){
			incorrectData("SpawnPoint");
		}
		
		//region
		try{
			String regionData = "";
			Location pointLocation = this.spawnPoint.getLocation();
			regionData += "x:"+(pointLocation.getBlockX()-WayPoint.rectangleBorderLength)+"~"+(pointLocation.getBlockX()+WayPoint.rectangleBorderLength+"|");
			regionData += "y:"+(pointLocation.getBlockY()-WayPoint.rectangleBorderLength)+"~"+(pointLocation.getBlockY()+WayPoint.rectangleBorderLength+"|");
			regionData += "z:"+(pointLocation.getBlockZ()-WayPoint.rectangleBorderLength)+"~"+(pointLocation.getBlockZ()+WayPoint.rectangleBorderLength+"|");
			regionData += "worldname:"+this.worldName;
			
			this.region = new Region(regionData);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
