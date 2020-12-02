package org.dggdak47.mpoints.area;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.dggdak47.mfractions.point.Point;
import org.dggdak47.mpoints.area.exceptions.AreaConstructorDataException;
import org.dggdak47.mpoints.regions.Region;
import org.dggdak47.mpoints.regions.exceptions.RegionConstructorDataException;

public class Area{
	private String areaName;
	private Integer respawningPointMaskId;
	private Integer rewardingScoreAmount;
	private ArrayList<Point> respawningPoints = null;
	private Region capturingRegion;
	private Region areaRegion;
	private ArrayList<String> description;
	private Material itemMaterial = null;
	
	public String getName() { 
		return this.areaName; 
	}
	public Integer getRespawningPointMaskId() { 
		return this.respawningPointMaskId; 
	}
	public Material getItemMaterial() {
		return this.itemMaterial;
	}
	public Integer getRewardingScore() { 
		return this.rewardingScoreAmount; 
	}
	public boolean hasRespawningPoints() {
		if(this.respawningPoints != null){
			return true;
		}else {
			return false;
		}
	}
	public ArrayList<Point> getRespawningPoints(){ 
		return (ArrayList)this.respawningPoints.clone(); 
	}
	public ArrayList<String> getDescription(){
		return (ArrayList)this.description;
	}
	public Region getCapturingRegion() {
		return this.capturingRegion;
	}
	public Region getAreaRegion() {
		return this.areaRegion;
	}
	public Location getRandomRespawningLocation(){
		return respawningPoints.get( (new Random()).nextInt(respawningPoints.size()) ).getLocation();
	}
	
	private void nullChecker(Object ob, String mapName, String fieldName) throws AreaConstructorDataException{
		if(ob == null){
			throw new AreaConstructorDataException(mapName, fieldName);
		}
	}
	public Area(Map areaData) throws AreaConstructorDataException{
		Object ob;
		
		//Name
		ob = areaData.get("Name");
		nullChecker(ob, "none", "Name");
		try{
			this.areaName = (String)ob;
		}catch(Exception e){
			throw new AreaConstructorDataException("none", "Name");
		}
		
		//RespawningPointMaskId
		ob = areaData.get("RespawningPointMaskId");
		nullChecker(ob, this.areaName, "RespawningPointMaskId");
		try {
			this.respawningPointMaskId = new Integer((String)ob);
		}catch(NumberFormatException e) {
			throw new AreaConstructorDataException(this.areaName, "RespawningPointMaskId");
		}
		
		//RewardingScoreAmount
		ob = areaData.get("RewardingScoreAmount");
		nullChecker(ob, this.areaName, "RewardingScoreAmount");
		try {
			this.rewardingScoreAmount = new Integer((String)ob);
		}catch(NumberFormatException e) {
			throw new AreaConstructorDataException(this.areaName, "RewardingScoreAmount");
		}
		
		//RespawningPoints
		ob = areaData.get("RespawningPoints");
		if(ob != null){
			//инициализация (respawningPoints)
			if(!(ob instanceof ArrayList)){
				throw new AreaConstructorDataException(this.areaName, "RespawningPoints");
			}else{
				ArrayList<String> points = (ArrayList)ob;
				this.respawningPoints = new ArrayList<Point>();
				try{
					for(String point: points){
						this.respawningPoints.add(new Point(point+"|0"));
					}
				}catch(Exception e){
					throw new AreaConstructorDataException(this.areaName, "RespawningPoints");
				}
			}
		}
		
		//CapturingRegion
		ob = areaData.get("CapturingRegion");
		nullChecker(ob, this.areaName, "CapturingRegion");
		try{
			this.capturingRegion = new Region((String)ob);
		}catch(RegionConstructorDataException e) {
			throw new AreaConstructorDataException(this.areaName, "CapturingRegion");
		}
		
		//AreaRegion
		ob = areaData.get("AreaRegion");
		nullChecker(ob, this.areaName, "AreaRegion");
		try{
			this.areaRegion = new Region((String)ob);
		}catch(RegionConstructorDataException e) {
			throw new AreaConstructorDataException(this.areaName, "AreaRegion");
		}
		
		//Description
		ob = areaData.get("Description");
		if(ob != null){
			if( !(ob instanceof ArrayList)){
				throw new AreaConstructorDataException(this.areaName, "Description");
			}else {
				try{
					this.description = (ArrayList<String>)ob;
				}catch(Exception e){
					throw new AreaConstructorDataException(this.areaName, "Description");
				}
			}
		}
		
		
		//Material
		ob = areaData.get("Material");
		if(ob != null){
			try{
				this.itemMaterial = Material.valueOf((String)ob);
			}catch(Exception e) {
				throw new AreaConstructorDataException(this.areaName, "Material");
			}
		}
	}
}