package org.dggdak47.mloot.chest;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.dggdak47.dutil.DUtil;
import org.dggdak47.mloot.blockWrapper.BlockDataWrapper;
import org.dggdak47.mloot.blockWrapper.exceptions.BlockWrapperConstructorException;
import org.dggdak47.mloot.chest.exceptions.ChestConstructorException;

public class Chest {
	private BlockDataWrapper chestBlockData;
	private ArrayList<Integer> kitsId = new ArrayList<Integer>();
	
	public BlockDataWrapper getBlockData() {
		return this.chestBlockData;
	}
	public ArrayList<Integer> getKitsId(){
		return (ArrayList)this.kitsId.clone();
	}
	public Integer getRandomKitId() {
		return kitsId.get( (new Random()).nextInt(kitsId.size()) );
	}
	
	public Chest(Map chestMap) throws ChestConstructorException{
		try {
			this.chestBlockData = new BlockDataWrapper( (String)chestMap.get("ChestLocationData") );
		} catch (BlockWrapperConstructorException e) {
			throw new ChestConstructorException("One of chest map has a mistake in its ChestLocationData field. FieldName: "+e.fieldName);
		}
		
		ArrayList<String> sKitsId;
		try{
			sKitsId = DUtil.split((String)chestMap.get("KitsId"), '|');
			
			for(String sKitId: sKitsId){
				this.kitsId.add(new Integer(sKitId));
			}
		}catch(Exception e){
			throw new ChestConstructorException("One of chest map has a mistake in its KitsId field");
		}
	}
}
