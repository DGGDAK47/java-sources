package org.dggdak47.inventory;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class InventoryHandler {
     public static class Item{
         
    	 private String eventInfo;
    	 private int count;
    	 private boolean isEnchanted;
    	 
    	 public String getEventInfo() { return this.eventInfo; }
    	 public int getCount() { return this.count; }
    	 public boolean getEnchantInfo() { return this.isEnchanted; }
    	 public short getSize(){
    		 if(this.isEnchanted){
    			 return (short)this.count;
    		 }else{
    			 if(this.count > 64){
        			 return (short) Math.ceil((double)count / 64);
        		 }else{
        			 return 1;
        		 }
    		 }
    	 }
    	 
         public Item copy(){
        	 return new Item(this.eventInfo, this.count, this.isEnchanted);
         }
         
    	 public Item(String ei, int c, boolean isEnch){
    		  
    		  this.eventInfo = ei;
    		  this.count = c;
    		  this.isEnchanted = isEnch;
    	 }
     }
     
     private int size;
     private String userName;
     private String oldInventory;
     private short pageIndex = 1;
     private short countPages;
     private ArrayList<Item> items = new ArrayList<Item>();
     
     private Logger l;
     
     public static String[] enchantedItems = {"DIAMOND_SWORD-PVP"};
     
     
     public InventoryHandler(String userName ,String userInventory, Logger l) {
    	  this.l = l;
    	  this.userName = userName;
    	  this.oldInventory = userInventory;
    	  
    	
    	  this.items = InventoryHandler.convert(userInventory);
    	  this.countPages = InventoryHandler.countPages(this.items);
    	  this.size = InventoryHandler.getSize(this.items);
     }
     
     
     public static ArrayList<Item> addItems(ArrayList<Item> items1, ArrayList<Item> items2){
    	 ArrayList<Item> toReturn = new ArrayList<Item>();
         boolean hasItems1 = false;
    	 boolean hasItems2 = false;
         
    	 for(Item i1: items1){
    		 hasItems1 = true;
    		 
    		 for(int i = 0; i < items2.size() ;i++){
    			 hasItems2 = true;
    			 
    			 Item i2 = items2.get(i);
    			 
    			 if(items2.size()-1 == i){
    				 if(!i1.getEventInfo().equals(i2.getEventInfo())){
    					 toReturn.add(i1.copy());
    				 }
    			 }
    			 
    			 if(i1.getEventInfo().equals(i2.getEventInfo())){
    				 Item item = new Item(i1.getEventInfo(), i1.getCount() + i2.getCount(), i1.getEnchantInfo());
    				 toReturn.add(item);
    				 break;
    			 }
    		 }
    	 }
    	 
    	 for(Item i2: items2){
    		 hasItems2 = true;
    		 for(int i = 0; i < items1.size() ;i++){
    			 hasItems1 = true;
    			 
    			 Item i1 = items1.get(i);
    			 
    			 if(items1.size()-1 == i){
    				 if(!i1.getEventInfo().equals(i2.getEventInfo())){
    					  toReturn.add(i2.copy());
    				 }
    			 }
    			 if(i1.getEventInfo().equals(i2.getEventInfo())){
    				 break;
    			 }
    		 }
    	 }
    	 
    	 if(hasItems1 & !hasItems2){
    		 return items1;
    	 }else if(!hasItems1 & hasItems2){
    		 return items2;
    	 }   	
    	 
    	 return toReturn;
     }
     public static ArrayList<Item> deducktionItems(ArrayList<Item> dbInv, ArrayList<Item> oldInv, Logger l){
    	 ArrayList<Item> toReturn = new ArrayList<Item>();
    	 
    	 boolean isEmptyOldInv = true;
    	 
    	 for(Item dbItem: dbInv){
    		 for(int i = 0; i < oldInv.size() ;i++){
    			 isEmptyOldInv = false;
    			 Item oldItem = oldInv.get(i);
    			 
    			 if(oldInv.size()-1 == i){
    				 if(!dbItem.getEventInfo().equals(oldItem.getEventInfo())){
    					 toReturn.add(dbItem.copy());
    				 }
    			 }
    			 
    			 if(dbItem.getEventInfo().equals(oldItem.getEventInfo())){
    				 Integer dbItemCount = new Integer(dbItem.getCount());
    				 Integer oldItemCount = new Integer(oldItem.getCount());
    				 
    				 if(dbItemCount.compareTo(oldItemCount) > 0){
    					 toReturn.add(new Item(dbItem.getEventInfo(), dbItemCount - oldItemCount, dbItem.getEnchantInfo()));
    				 }else if(dbItemCount.compareTo(oldItemCount) < 0){
    					 toReturn.add(new Item(dbItem.getEventInfo(), oldItemCount - dbItemCount, dbItem.getEnchantInfo()));
    				 }
    				 break;
    			 }
    		 }
    	 }
    	 
    	 if(isEmptyOldInv){
    		 return dbInv;
    	 }
    	 
    	 return toReturn;
     }
     public static ArrayList<Item> convert(String inventory){
    	 ArrayList<Item> al = new ArrayList<Item>();
    	 String[] itemsPairs = inventory.split("\\|");

    	 String eventInfo = "";
    	 String sCount = "";
    	 boolean isCount = false;
         
    	 for(String s: itemsPairs){
    		 for(int i = 0; i < s.length() ; i++){
    			  if(s.charAt(i) == ':'){
    				  isCount = true;
    				  continue;
    			  }
    			  if(s.length()-1 == i){
    				  sCount += s.charAt(i);
    				  
    				  if(Arrays.asList(InventoryHandler.enchantedItems).contains(eventInfo)){
    					  al.add(new Item(eventInfo, Integer.parseInt(sCount), true));
    				  }else{
    					  al.add(new Item(eventInfo, Integer.parseInt(sCount), false));
    				  }
    				  
    				  eventInfo = "";
    				  sCount = "";
    				  isCount = false;
    				  continue;
    			  }
    			  if(isCount){
    				  sCount += s.charAt(i);
    			  }else{
    				  eventInfo += s.charAt(i);
    			  }
    		 }
    	 }
    	 return al;
     }
     public static int getSize(ArrayList<Item> al){
    	  int size = 0;
    	  
    	  for(Item i: al){
    		  if(i.getEnchantInfo()){
    			  size += i.getCount();
    		  }else{
    			  if(i.getCount() > 64){
    				  size += Math.ceil((double)i.getCount() / 64);
    			  }else{
    				  size++;
    			  }
    		  }
    	  }
    	  return size;
     }
     public static short itemIndex(ArrayList<Item> al, String eventInfo){
    	 for(short i = 0; i < al.size(); i++){
    		 Item item = al.get(i);
    		 if(item.eventInfo.equals(eventInfo)){
                 return i;
    		 }
    	 }
    	 
    	 return -1;
     }
     public static short countPages(ArrayList<Item> al){
    	 double size = InventoryHandler.getSize(al);
    	 
    	 if(size > 45){
    		 if(size > 90){
    			 return (short) Math.ceil((double)size / 45);
    		 }else{
    			 return 2;
    		 }
    	 }else{
    		 return 1;
    	 }
     }
     public static ArrayList<ItemStack> getStacks(Item item){
    	 ArrayList<ItemStack> al = new ArrayList<ItemStack>();
    	 
    	 if(item.getEnchantInfo()){
    		 for(int i = 0; i < item.getSize(); i++){
    			 al.add(EnchantmentStorage.getItemByEventInfo(item.getEventInfo()));
    		 }
    		 return al;
    	 }
    	 
    	 int itemCount = item.getCount();
    	 int countStacks = (int) Math.floor(itemCount/64);
    	 
    	 boolean hasNotFullStack = false;
    	 int notFullStackCount = 1;
    	 
    	 if(countStacks*64 < itemCount){
    		 hasNotFullStack = true;
    		 notFullStackCount = itemCount - countStacks*64;
       		 countStacks++;
    	 }

    	 if(countStacks == 1){
    		 if(hasNotFullStack){
    			 al.add(new ItemStack(Material.valueOf(item.getEventInfo()), notFullStackCount));
    		 }else{
    			 al.add(new ItemStack(Material.valueOf(item.getEventInfo()), 64));
    		 }
    	 }else{
    		 for(int i = 0; i < countStacks; i++){
    			 if(i != countStacks-1){
    				 al.add(new ItemStack(Material.valueOf(item.getEventInfo()), 64));
    			 }else{
    				 if(hasNotFullStack){
    					 al.add(new ItemStack(Material.valueOf(item.getEventInfo()), notFullStackCount));
    				 }else{
    					 al.add(new ItemStack(Material.valueOf(item.getEventInfo()), 64));
    				 }
    			 }
    		 }
    	 }
    	 
    	 return al;
     }
     public static ArrayList<ItemStack> getItemsPart(ArrayList<Item> items, int from, int countStacksToReturn){
    	 ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
    	 ArrayList<ItemStack> toReturn = new ArrayList<ItemStack>();
    	 
    	 for(Item i: items){
    		stacks.addAll(InventoryHandler.getStacks(i));
    	 }

    	 for(int i = from; i < stacks.size(); i++){
    		 if(i < countStacksToReturn+from){
    			 toReturn.add(stacks.get(i));
    			 continue;
    		 }else{
    			 break;
    		 }
    	 }
    	 
    	 return toReturn;
     }
     public static ItemStack createArrow(boolean isForward, String index){
    	 ItemStack arrow = new ItemStack(Material.ENDER_PEARL, 1);
    	 ItemMeta im = arrow.getItemMeta();
    	 
    	 if(isForward){
    		 im.setDisplayName("Вперёд. "+index);
    	 }else{
    		 im.setDisplayName("Назад. "+index);
    	 }
    	 
    	 arrow.setItemMeta(im);
    	 
    	 return arrow;
     }
     
     public String getOldInventory() { return this.oldInventory; }
     public String getUserName() { return this.userName; }
     public short getPageIndex() { return this.pageIndex; }
     public short getCountPages() { return this.countPages; }
     

     public boolean hasItemCount(ItemStack is) {
    	 boolean isEnchanted = false;
    	 
    	 if(!EnchantmentStorage.getEventInfoByItemStack(is, l).equals("")){
    		 isEnchanted = true;
    	 }

    	 int index;
    	 if(isEnchanted){
    		 index = itemIndex(this.items, EnchantmentStorage.getEventInfoByItemStack(is, l));
    	 }else{
    		 index = itemIndex(this.items, is.getType().toString());
    	 }

    	 if(index == -1){
    		 return false;
    	 }
    	 
    	 if(this.items.get(index).count >= is.getAmount()){
    		 return true;
    	 }else{
    		 return false;
    	 }
     }
     public ItemStack deducktItem(ItemStack is){
    	 boolean isEnchanted = false;
    	 
    	 if(!EnchantmentStorage.getEventInfoByItemStack(is, l).equals("")){
    		 isEnchanted = true;
    	 }
    	 
    	 short index; 
    	 
    	 if(isEnchanted){
    		 index = InventoryHandler.itemIndex(this.items, EnchantmentStorage.getEventInfoByItemStack(is, l));
    	 }else{
    		 index = InventoryHandler.itemIndex(this.items, is.getType().toString());
    	 }
    	 
    	 items.get(index).count = items.get(index).count - is.getAmount();
    	 if(items.get(index).count <= 0){
    		 items.remove(index);
    	 }
    	 
    	 return is;
     }
     public boolean changePage(boolean forward){
    	 if(forward){
    		 if(this.pageIndex < this.countPages){
    			 this.pageIndex++;
    			 return true;
    		 }else {
    			 return false;
    		 }
    	 }else{
    		 if(this.pageIndex > 1){
    			 this.pageIndex--;
    			 return true;
    		 }else{
    			 return false;
    		 }
    	 }
     }
     public Inventory getInventory(){
    	 Inventory inv = Bukkit.createInventory(null, 54, "Ваш инвентарь");
    	 ItemStack[] is = getPageFromIndex();
    	 
    	 
    	 inv.setContents(is);

    	 if(this.size <= 45){
    		 return inv;
    	 }
    	 
    	 if(this.pageIndex == this.countPages & (this.countPages-1) != 0){
    		 inv.setItem(45, createArrow(false, (this.pageIndex-1)+"/"+this.countPages));
    		 
    	 }else if(this.pageIndex == 1 & this.pageIndex < this.countPages){
    		 inv.setItem(53, createArrow(true, (this.pageIndex+1)+"/"+this.countPages));
    		 
    	 }else if(this.pageIndex > 1 & this.pageIndex < this.countPages){
    		 inv.setItem(45, createArrow(false, (this.pageIndex-1)+"/"+this.countPages));
    		 inv.setItem(53, createArrow(true, (this.pageIndex+1)+"/"+this.countPages));
    	 }
    	 
    	 
    	 return inv;
     }
     public ItemStack[] getPageFromIndex(){
    	 ArrayList<ItemStack> al = new ArrayList<ItemStack>();
    	 
    	 int from = (this.pageIndex-1)*45;
    	 int count;
    	 if(this.pageIndex == this.countPages){
    		 count = this.countPages*45-(this.countPages*45 - this.size);
    	 }else{
    		 count = 45;
    	 }
    	 
    	 al.addAll(InventoryHandler.getItemsPart(this.items, from, count));
    	 
    	 ItemStack[] arr = new ItemStack[al.size()];
    	 return al.toArray(arr);
     }
     public String assemble(String dbInventory){
    	 ArrayList<Item> dbInv = convert(dbInventory);
    	 ArrayList<Item> oldInv = convert(this.oldInventory);
    	 
    	 ArrayList<Item> invToAdd = deducktionItems(dbInv, oldInv, l);
    	 
    	 ArrayList<Item> invToAssemble = addItems(this.items, invToAdd);
    	 
    	 String toReturn = "";
    	 
    	 for(int i = 0; i < invToAssemble.size() ;i++){
    		 Item item = invToAssemble.get(i);
    		 
    		 if(invToAssemble.size()-1 == i){
    			 toReturn += item.eventInfo+":"+item.count;
    			 break;
    		 }
    		 toReturn += item.eventInfo+":"+item.count+"|";
    	 }
    	 return toReturn;
     }
     
     public void update(Inventory openedInventory){
    	 this.countPages = InventoryHandler.countPages(this.items);
    	 this.size = getSize(this.items);
    	 
    	 if(this.countPages < this.pageIndex){
    		 this.pageIndex--;
    	 }
    	 
    	 ItemStack[] is = getPageFromIndex();
    	 openedInventory.setContents(is);
    	 
    	 if(this.pageIndex == this.countPages & (this.countPages-1) != 0){
    		 openedInventory.setItem(45, createArrow(false, (this.pageIndex-1)+"/"+this.countPages));
    		 
    	 }else if(this.pageIndex == 1 & this.pageIndex < this.countPages){
    		 openedInventory.setItem(53, createArrow(true, (this.pageIndex+1)+"/"+this.countPages));
    		 
    	 }else if(this.pageIndex > 1 & this.pageIndex < this.countPages){
    		 openedInventory.setItem(45, createArrow(false, (this.pageIndex-1)+"/"+this.countPages));
    		 openedInventory.setItem(53, createArrow(true, (this.pageIndex+1)+"/"+this.countPages));
    	 }
     }
     
}
