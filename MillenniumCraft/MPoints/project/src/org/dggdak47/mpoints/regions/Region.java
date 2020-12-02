package org.dggdak47.mpoints.regions;

import java.util.ArrayList;
import java.util.Hashtable;

import org.bukkit.Location;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.mpoints.regions.exceptions.RegionConstructorDataException;

public class Region {
	public final String worldName;
	
	public final Integer xFrom;
	public final Integer xTo;
	public final Integer yFrom;
	public final Integer yTo;
	public final Integer zFrom;
	public final Integer zTo;
	
	
	public boolean isLocationWithin(Location loc) {
		if(!worldName.equals(loc.getWorld().getName())){
			return false;
		}
		
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		if(x >= this.xFrom && x <= this.xTo){
			if(y >= this.yFrom && y <= this.yTo){
				if(z >= this.zFrom && z <= this.zTo){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Region(String data) throws RegionConstructorDataException{
		//data -> "x:from~to|y:from~to|z:from~to|worldname:world"
		
		//splitedData -> ["x:from~to", "y:from~to", "z:from~to", "worldname:world" ]
		ArrayList<String> splitedData = DUtil.split(data, '|');
		
		//splitedArrayList -> {"x":"from~to", "y":"from~to", "z":"from~to", "worldname":"world"}
		Hashtable<String, String> splitedArrayList;
		try{
			splitedArrayList = DUtil.splitArrayList(splitedData, ':');
		}catch(Exception e){
			throw new RegionConstructorDataException("parsing data error");
		}
		
		//* * * * * * * * * * * *
		//
		//* * * * * * * * * * * *
		//splitedCoords -> ["from", "to"]
		ArrayList<String> splitedCoords;
		String pairValue;
		Integer from;
		Integer to;
		
		//x
		pairValue = splitedArrayList.get("x");
		try{
			splitedCoords = DUtil.split(pairValue, '~');
			from = new Integer(splitedCoords.get(0));
			to = new Integer(splitedCoords.get(1));
			if(from >= to){
				this.xFrom = to;
				this.xTo = from;
			}else {
				this.xFrom = from;
				this.xTo = to;
			}
			
		}catch(Exception e){
			throw new RegionConstructorDataException("x: coords data error");
		}
		
		//y
		pairValue = splitedArrayList.get("y");
		try{
			splitedCoords = DUtil.split(pairValue, '~');
			from = new Integer(splitedCoords.get(0));
			to = new Integer(splitedCoords.get(1));
			if(from >= to){
				this.yFrom = to;
				this.yTo = from;
			}else {
				this.yFrom = from;
				this.yTo = to;
			}
		}catch(Exception e){
			throw new RegionConstructorDataException("y: coords data error");
		}
		
		//z
		pairValue = splitedArrayList.get("z");
		try{
			splitedCoords = DUtil.split(pairValue, '~');
			from = new Integer(splitedCoords.get(0));
			to = new Integer(splitedCoords.get(1));
			if(from >= to){
				this.zFrom = to;
				this.zTo = from;
			}else {
				this.zFrom = from;
				this.zTo = to;
			}
		}catch(Exception e){
			throw new RegionConstructorDataException("z: coords data error");
		}
		
		//worldName
		pairValue = splitedArrayList.get("worldname");
		if(pairValue == null){
			throw new RegionConstructorDataException("worldname data error");
		}else {
			this.worldName = pairValue;
		}
	}
}
