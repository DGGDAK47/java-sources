package org.dggdak47.mloot.blockWrapper;

import java.util.ArrayList;
import java.util.Hashtable;

import org.bukkit.block.BlockFace;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.mloot.blockWrapper.exceptions.BlockWrapperConstructorException;

public class BlockDataWrapper {
	public final Integer x;
	public final Integer y;
	public final Integer z;
	public final String worldName;
	public final BlockFace face;
	
	private void nullChecker(Object ob, String exceptionMessage, String fieldName) throws BlockWrapperConstructorException{
		if(ob == null){
			throw new BlockWrapperConstructorException(exceptionMessage, fieldName);
		}
	}
	public BlockDataWrapper(String constructorData) throws BlockWrapperConstructorException{
		//s -> "worldName:name|x:0|y:0|z:1|face:EAST"
		Hashtable<String, String> parsedConstroctorData;
		try{
			parsedConstroctorData = DUtil.splitStringOnPairs(constructorData, '|', ':');
		}catch(Exception e){
			throw new BlockWrapperConstructorException("Wrong constructor data!", "constructor data");
		}
		
		
		String pairValue;
		
		//worldName
		pairValue = parsedConstroctorData.get("worldName");
		nullChecker(pairValue, "passed constructor data has no pair: worldName", "worldName");
		this.worldName = pairValue;
		
		//x
		pairValue = parsedConstroctorData.get("x");
		nullChecker(pairValue, "passed constructor data has no pair: x", "x");
		try{
			this.x = new Integer(pairValue);
		}catch(Exception e) {
			throw new BlockWrapperConstructorException("passed constructor data has wrong pair value: x", "x");
		}
		//y
		pairValue = parsedConstroctorData.get("y");
		nullChecker(pairValue, "passed constructor data has no pair: y", "y");
		try{
			this.y = new Integer(pairValue);
		}catch(Exception e) {
			throw new BlockWrapperConstructorException("passed constructor data has wrong pair value: y", "y");
		}
		//z
		pairValue = parsedConstroctorData.get("z");
		nullChecker(pairValue, "passed constructor data has no pair: z", "z");
		try{
			this.z = new Integer(pairValue);
		}catch(Exception e) {
			throw new BlockWrapperConstructorException("passed constructor data has wrong pair value: z", "z");
		}
		
		//face
		pairValue = parsedConstroctorData.get("face");
		nullChecker(pairValue, "passed constructor data has no pair: face", "face");
		try{
			this.face = BlockFace.valueOf(pairValue);
		}catch(Exception e) {
			throw new BlockWrapperConstructorException("passed constructor data has wrong pair value: face", "face");
		}
	}
}