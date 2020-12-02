package org.dggdak47.mranks.ranks;

import java.util.ArrayList;
import java.util.Map;

import org.dggdak47.dutil.DUtil;
import org.dggdak47.mranks.ranks.exceptions.RankConstructorDataException;

public class Rank {
	private String name;
	private Integer id;
	private Integer score;
	private ArrayList<String> permissionGroups;
	
	public String getName() {
		return this.name;
	}
	public Integer getId() {
		return new Integer(this.id);
	}
	public Integer getScore() {
		return new Integer(this.score);
	}
	public ArrayList<String> getPermissionGroups(){
		return (ArrayList)this.permissionGroups.clone();
	}
	
	private void nullChecker(Object ob, String msg) throws RankConstructorDataException{
		if(ob == null){
			throw new RankConstructorDataException(msg);
		}
	}
	public Rank(Map rankMap) throws RankConstructorDataException{
		
		Object ob = rankMap.get("Name");
		nullChecker(ob, "One of ranks map has no Name property!");
		this.name = (String)ob;
		
		
		ob = rankMap.get("ID");
		nullChecker(ob, "Rank: ("+this.name+") has no Id property!");
		try{
			this.id = new Integer( (String)ob );
		}catch(NumberFormatException e){
			throw new RankConstructorDataException("Rank: ("+this.name+") has wrong Id property!");
		}
		
		
		ob = rankMap.get("PermissionGroups");
		nullChecker(ob, "Rank: ("+this.name+") has no PermissinGroups property!");
		if( !(ob instanceof String) ){
			throw new RankConstructorDataException("Rank: ("+this.name+") has wrong PermissinGroups property!");
		}
		this.permissionGroups = DUtil.split( (String)ob , '|');
		
		
		ob = rankMap.get("Score");
		nullChecker(ob, "Rank: ("+this.name+") has no Score property!");
		try {
			this.score = new Integer((String)ob);
		}catch(NumberFormatException e) {
			throw new RankConstructorDataException("Rank: ("+this.name+") has wrong Score property!");
		}
	}
}
