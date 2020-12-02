package org.dggdak47.mfractions.point;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.mfractions.point.exceptions.PointConfigPropertyException;

public class Point {
	private Integer id;
	private Location location;
	private String sPoint;
	
	public String getConstructorData() {
		return this.sPoint;
	}
	public Integer getId() {
		return new Integer(this.id);
	}
	public Location getLocation() {
		return location.clone();
	}
	public Point clone() {
		String properties = "";
		properties += location.getWorld().getName()+":";
		properties += location.getX()+":"+location.getY()+":"+location.getZ()+"|";
		properties += location.getYaw()+":"+location.getPitch()+"|"+id;
		
		try {
			return new Point(properties);
		} catch (PointConfigPropertyException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Point(String point) throws PointConfigPropertyException{
		//point -> "worldName:x:y:z|yaw:pitch|ID"
		this.sPoint = point;
		//splitedPoint -> [ "worldName:x:y:z", "yaw:pitch", "ID" ]
		ArrayList<String> splitedPoint = DUtil.split(point, '|');
		
		if(splitedPoint.size() == 0){
			throw new PointConfigPropertyException("Point properties has not been passed");
		}
		
		//splitedLoc1 -> [ "worldName", "x", "y", "z" ]
		ArrayList<String> splitedLoc1 = DUtil.split(splitedPoint.get(0), ':');
		//splitedLoc2 -> [ "yaw", "pitch" ]
		ArrayList<String> splitedLoc2 = DUtil.split(splitedPoint.get(1), ':');
		
		try{
			this.id = new Integer(splitedPoint.get(2));
		}catch(NumberFormatException e) {
			throw new PointConfigPropertyException("ID typed wrong, or not typed at all!");
		}
		
		String worldName = splitedLoc1.get(0);
		Double x;
		Double y;
		Double z;
		Float yaw;
		Float pitch;
		
		if(worldName == null){
			throw new PointConfigPropertyException("Name wasn't typed!");
		}
		
		try {
			x = new Double(splitedLoc1.get(1));
			y = new Double(splitedLoc1.get(2));
			z = new Double(splitedLoc1.get(3));
			
			yaw = new Float(splitedLoc2.get(0));
			pitch = new Float(splitedLoc2.get(1));
		}catch(NumberFormatException e) {
			throw new PointConfigPropertyException("XYZ coords or yaw and pitch typed wrong!");
		}
		World world = Bukkit.getWorld(worldName);
		if(world == null) {
			throw new PointConfigPropertyException("World with name: "+worldName+" doesn't exist");
		}else {
			this.location = new Location(world, x, y, z , yaw, pitch);
		}
	}
}
