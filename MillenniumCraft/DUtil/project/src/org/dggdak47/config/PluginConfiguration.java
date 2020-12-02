package org.dggdak47.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.dggdak47.config.exceptions.WrongConfigWayException;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

public class PluginConfiguration {
	private Map config;
	
	public Object get(String way) throws WrongConfigWayException {
		ArrayList<String> listedWay = new ArrayList<String>();
		
		String pathElement = "";
		
		for(int i = 0; i < way.length(); i++){
			if(i == way.length()-1){
				pathElement += way.charAt(i);
				listedWay.add(pathElement);
				continue;
			}else if( new Character(way.charAt(i)).equals('.') ){
				listedWay.add(pathElement);
				pathElement = "";
				continue;
			}
			pathElement += way.charAt(i);
		}
		
		//listedWay ->  [ 'Config', 'Help', 'Test', 'Text' ]
		
		Object ob = null;
		Map m = this.config;
		String key;
		for(int i = 0; i < listedWay.size(); i++){
			key = listedWay.get(i);
			
			if(i == listedWay.size()-1){
				ob = m.get(key);
				if(ob != null){
					return ob;
				}else{
					throw new WrongConfigWayException();
				}
			}else{
				ob = m.get(key);
				if(ob != null){
					m = (Map)ob;
				}else {
					throw new WrongConfigWayException();
				}
			}
			
		}
		return ob;
	}
	
	public ArrayList<String> getListS(String way) {
		Object list;
		try {
			list = get(way);
		} catch (WrongConfigWayException e) {
			e.printStackTrace();
			return null;
		}
		
		if(list instanceof ArrayList){
			return (ArrayList<String>)list;
		}else {
			return null;
		}
	}
	
	public String getString(String way) {
		Object s;
		try {
			s = get(way);
		} catch (WrongConfigWayException e) {
			e.printStackTrace();
			return null;
		}
		
		if(s instanceof String) {
			return (String)s;
		}else {
			return null;
		}
	}
	
	public Integer getInt(String way) {
		Object i;
		try {
			i = get(way);
		} catch (WrongConfigWayException e) {
			e.printStackTrace();
			return null;
		}
		
		if(i instanceof String){
			return new Integer( (String)i);
		}else {
			return null;
		}
	}
	
	public Map getMap(String way) {
		Object m;
		
		try {
			m = get(way);
		}catch (WrongConfigWayException e) {
			e.printStackTrace();
			return null;
		}
		
		if(m instanceof Map){
			return (Map)m;
		}else {
			return null;
		}
	}
	
	public Double getDouble(String way) {
		Object d;
		
		try {
			d = get(way);
		}catch (WrongConfigWayException e) {
			e.printStackTrace();
			return null;
		}
		
		if(d instanceof String){
			return new Double( (String)d );
		}else {
			return null;
		}
	}
	
	public Float getFloat(String way){
       Object f;
		
		try {
			f = get(way);
		}catch (WrongConfigWayException e) {
			e.printStackTrace();
			return null;
		}
		
		if(f instanceof String){
			return new Float( (String)f );
		}else {
			return null;
		}
	}
	
	public Boolean getBoolean(String way) {
		Object b;
		
		try {
			b = get(way);
		}catch (WrongConfigWayException e) {
			e.printStackTrace();
			return null;
		}
		
		if(b instanceof String){
			return new Boolean( (String)b );
		}else {
			return null;
		}
	}
	
	public PluginConfiguration(String pluginName) throws FileNotFoundException, YamlException {
		Properties p = System.getProperties();
		String sep = p.getProperty("file.separator");
		String dir = p.getProperty("user.dir");
		
		YamlReader yr = new YamlReader(new FileReader(dir+sep+"plugins"+sep+pluginName+sep+"config.yml"));
		this.config = (Map)yr.read();
		
	}
}
