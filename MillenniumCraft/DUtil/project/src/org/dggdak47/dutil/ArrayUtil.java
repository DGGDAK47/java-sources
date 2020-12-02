package org.dggdak47.dutil;

public class ArrayUtil {
	public static String[] objectArratToString(Object[] toString) {
		String[] toReturn = new String[toString.length];
		for(int i = 0; i < toString.length; i++){
			toReturn[i] = (String)toString[i];
		}
		return toReturn;
	}
	
	public static String[] add(String[] first, String[] second) {
		String[] toReturn = new String[first.length+second.length];
		for(int i = 0; i < first.length; i++){
			toReturn[i] = first[i];
		}
        for(int i = 0; i < second.length; i++){
			toReturn[i+first.length] = second[i];
		}
        
        return toReturn;
	}
}
