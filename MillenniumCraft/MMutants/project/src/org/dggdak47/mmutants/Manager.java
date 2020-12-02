package org.dggdak47.mmutants;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.dggdak47.config.PluginConfiguration;
import org.dggdak47.mmutants.invasion.Invasion;
import org.dggdak47.mmutants.invasion.exceptions.InvasionConstructorDataException;
import org.dggdak47.mmutants.managerExceptions.InvasionInitializingException;

public class Manager {
	private MMutants pluign;
	
	//Invasions Handler
	private ArrayList<Invasion> invasions = new ArrayList<Invasion>();
	public boolean hasInvasion(Integer id) {
		for(Invasion i: invasions){
			if(i.getId().equals(id)){
				return true;
			}
		}
		
		return false;
	}
	public Invasion getInvasion(Integer id){
		for(Invasion i: invasions){
			if(i.getId().equals(id)){
				return i;
			}
		}
		
		return null;
	}
	
	
	//
	
	private PluginConfiguration c() {
		return this.pluign.config;
	}
	public Manager(MMutants plugin){
		this.pluign = plugin;
		
		
		//Invasions initializing
		Map invasions = c().getMap("Invasions");
		Set invasionsKeys = invasions.keySet();
		
		for(Object key: invasionsKeys){
			try {
				this.invasions.add(new Invasion((Map)invasions.get(key)));
			} catch (InvasionConstructorDataException e) {
				new InvasionInitializingException("Invasion map: "+((String)key)+" has an error in field: "+e.getFieldName() ).printStackTrace();
			}
		}
	}
}
