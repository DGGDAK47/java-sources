package org.dggdak47.guid.wrapping;

import java.util.ArrayList;

import org.bukkit.Material;
import org.dggdak47.guid.Util;

public class RightClickWrapper {
	private String name;
	private Material material;
	private String command;
	private int id;
	
	public String getName() {
		return this.name;
	}
	public Material getMaterial() {
		return this.material;
	}
	public String getCommand() {
		return this.command;
	}
	public Integer getID() {
		return this.id;
	}
	
	public RightClickWrapper(String configString){
		// Name|Material|Command
		ArrayList<String> splitedConfig = Util.split(configString, '|');
		this.name = splitedConfig.get(0);
		this.material = Material.valueOf(splitedConfig.get(1));
		this.command = splitedConfig.get(2);
		this.id = new Integer( (String)splitedConfig.get(3) );
	}
}
