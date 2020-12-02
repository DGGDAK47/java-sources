package org.dggdak47.assembling;

import java.util.ArrayList;
import java.util.Hashtable;

import org.dggdak47.guid.wrapping.InventoryWrapper;
import org.dggdak47.guid.wrapping.ItemWrapper;

public class Assembler {
	public static InventoryWrapper addition(InventoryWrapper iw1, InventoryWrapper iw2, String newInventoryName, String inventoryID) {
		Hashtable ht = new Hashtable();
		
		ht.put("Name", newInventoryName);
		ht.put("ID", inventoryID);
		
		Integer iw1Size = iw1.getSize();
		Integer iw2Size = iw2.getSize();
		
		if( Integer.compare(iw1Size, iw2Size) > 0){
			ht.put("Size", iw1Size.toString());
		}else if( Integer.compare(iw1Size, iw2Size) < 0 ){
			ht.put("Size", iw2Size.toString());
		}else {
			ht.put("Size", iw1Size.toString());
		}
		
		ht.put("Items", addition(iw1.getItems(), iw2.getItems()) );
		
		
		return new InventoryWrapper(ht);
	}
	
    public static ArrayList<ItemWrapper> addition(ArrayList<ItemWrapper> al1, ArrayList<ItemWrapper> al2) {
		ArrayList<ItemWrapper> toReturn = new ArrayList<ItemWrapper>();
		
		ItemWrapper priorityWrapper1;
		
		for(ItemWrapper secondWrapper: al2){
			for(int i = 0; i < al1.size(); i++){
				priorityWrapper1 = al1.get(i);
				
				if( (i == al1.size()-1) && (priorityWrapper1.getIndex().compareTo(secondWrapper.getIndex()) != 0) ){
					toReturn.add(secondWrapper);
				}else if( priorityWrapper1.getIndex().compareTo(secondWrapper.getIndex()) == 0 ){
					toReturn.add(priorityWrapper1);
					break;
				}
				
			}
		}
		
		ItemWrapper priorityWrapper2;
		for(ItemWrapper firstWrapper: al1){
			for(int i = 0; i < al2.size(); i++){
				priorityWrapper2 = al2.get(i);
				
				if( (i == al2.size()-1 ) && !(priorityWrapper2.getIndex().equals(firstWrapper.getIndex())) ){
					toReturn.add(firstWrapper);
				}else if( priorityWrapper2.getIndex().equals(firstWrapper.getIndex()) ){
					break;
				}
			}
		}
		
		return toReturn;
	}
}
