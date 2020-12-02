package org.dggdak47.mfractions.fraction;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.dggdak47.dutil.DUtil;
import org.dggdak47.mfractions.fraction.exceptions.FractionConfigPropertyException;
import org.dggdak47.mfractions.point.exceptions.PointConfigPropertyException;
import org.dggdak47.mfractions.point.Point;

import ru.tehkode.permissions.PermissionUser;

public class Fraction {
	private String fractionName;
	private Point fractionPoint;
	private ArrayList<String> permissionGroups;
	private Integer id;
	
	public String getName() {
		return this.fractionName;
	}
	public Point getPoint() {
		return this.fractionPoint;
	}
	public ArrayList<String> getPermissionGroups(){
		return new ArrayList<String>(this.permissionGroups);
	}
	public Integer getId() {
		return new Integer(this.id);
	}
	public Fraction clone() {
		Hashtable ht = new Hashtable();
		ht.put("Name", this.fractionName);
		ht.put("ID", this.id.toString());
		ht.put("SpawnPoint", this.fractionPoint.getConstructorData());
		
		String permGroups = "";
		if(permissionGroups.size() == 1){
			permGroups += permissionGroups.get(0);
		}else{
			String group;
			
			for(int i = 0; i < permissionGroups.size(); i++){
				group = permissionGroups.get(i);
				
				if(permissionGroups.size()-1 == i){
					permGroups += group;
				}else{
					permGroups += group+'|';
				}
			}
		}
		
		ht.put("PermissionGroups", permGroups);
		
		try {
			return new Fraction(ht);
		} catch (FractionConfigPropertyException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Fraction(Map fraction) throws FractionConfigPropertyException{
		//fraction { Name:"имя", ID:0, PermissionGroups: "grName1|grName2...|grNameN",  SpawnPoint: "..."}
		if(fraction.size() == 0){
			throw new FractionConfigPropertyException("fraction map is empty!");
		}
		
		Object ob;
		
		ob = fraction.get("Name");
		if(ob == null){
			throw new FractionConfigPropertyException("Name has not been passed");
		}else {
			this.fractionName = (String)ob;
		}
		
		ob = fraction.get("ID");
		if(ob == null){
			throw new FractionConfigPropertyException("ID has not been passed");
		}else {
			this.id = new Integer((String)ob);
		}
		
		ob = fraction.get("PermissionGroups");
		if(ob == null){
			throw new FractionConfigPropertyException("PermissionGroups have not been passed");
		}else {
			this.permissionGroups = DUtil.split((String)ob, '|');
		}
		
		ob = fraction.get("SpawnPoint");
		if(ob == null){
			throw new FractionConfigPropertyException("SpawnPoint filed have not been passed");
		}else{
			try{
				this.fractionPoint = new Point((String)ob);
			}catch(PointConfigPropertyException e) {
				e.printStackTrace();
				throw new FractionConfigPropertyException("SpawnPoint data were passed with a mistake!");
			}
		}
	}
}