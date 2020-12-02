package org.dggdak47.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandExecutor;
import org.dggdak47.inventory.InventoryEvents;
import org.dggdak47.inventory.InventoryHandler;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class Inv extends JavaPlugin implements CommandExecutor{
	
	  public static class InventoryHolder{
		  private boolean isOpenned;
		  private InventoryHandler ih;
		  
		  public boolean getOpenInfo(){
			  return this.isOpenned;
		  }
		  
		  public InventoryHolder(InventoryHandler ih, boolean isOpenned){
			  this.isOpenned = isOpenned;
			  this.ih = ih;
		  }
	  }
	  
	  private InventoryEvents inventoryEvents;
	  private ArrayList<InventoryHolder> inventories = new ArrayList<InventoryHolder>();
	  protected Logger l;
	  
	  public Inv(){
		  this.inventoryEvents = new InventoryEvents(this, this);
		  l = Bukkit.getLogger();
	  }
	  
	  @Override
      public void onEnable() {
        Bukkit.getPluginManager().registerEvents(inventoryEvents, this);
        
      }
	  @Override
	  public void onDisable(){
		  
		  if(inventories.size() != 0){
			  if(!synchronize(true)){
				  synchronize(false);
			  }
		  }
	  }

	  
	  @Override
      public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		     Runnable r = new Runnable(){
		    	 @Override
		    	 public void run(){
		    		 //начало
		    		 Player p = (Player)commandSender;
		    		 
		    		 byte result = addUserInventory(p.getName());
		    		 
		    		 if(result == -1){
				    	 p.sendMessage("Произошла какая то ошибка! Попробуйте позже.");
				     }else if(result == 0){
				    	 p.sendMessage("Перед использованием инвентаря вам следует зарегестрироваться на сайте сервера.");
				     }else if(result == 1){
				    	 InventoryHandler ih = getInventoryHandlerByNickname(p.getName());
				    	 
				    	 if(ih != null){
				    		 p.openInventory(ih.getInventory());
				    	 }else{
				    		 p.sendMessage("Произошла какая то ошибка! Попробуйте позже.");
				    	 }
				     }
		    		 //конец
		    	 }
		     };
		     Bukkit.getScheduler().runTaskAsynchronously(this, r);

		     return true;
      }
	  
	  public byte addUserInventory(String userName){
		  setOpenInfoByNickname(userName, false);
		  
		  if(inventories.size() != 0){
			  if(!synchronize(false)){
				  return -1;
			  }
		  }
		  
		  
		  Connection conn = null;
		  Statement stmt = null;
		  boolean hasException = false;
		  
		  String inventory = null;
		  
		  try{
			  conn = getConnection();
			  stmt = conn.createStatement();
			  
			  String sql = "USE armagedon132;";
			  stmt.executeQuery(sql);
			  
			  sql = "SELECT inventory FROM user_table WHERE nickname ='"+userName+"';";
			  ResultSet rs = stmt.executeQuery(sql);
			  
			  
			  while(rs.next()){
				  inventory = rs.getString("inventory");
			  }
			  
			  conn.close();
			  stmt.close();
			  rs.close();
		  }catch(SQLNonTransientConnectionException e) {
			  l.info("----------------inventory SQLNonTransientConnectionException----------");
		  }catch(SQLException e){
			  e.printStackTrace();
			  hasException = true;
		  }catch(ClassNotFoundException e){
			  e.printStackTrace();
			  hasException = true;
		  }catch(Exception e){
			  e.printStackTrace();
			  hasException = true;
		  }finally{
			  try{
				  if(stmt != null){
					  stmt.close();
				  }
			  }catch(SQLException se){
				  se.printStackTrace();
				  hasException = true;
			  }
			  
			  try{
				  if(conn != null){
					  conn.close();
				  }
			  }catch(SQLException se){
				  se.printStackTrace();
				  hasException = true;
			  }
			  if(hasException){
				  return -1;
			  }
		  }

		  if(inventory != null){
			  InventoryHandler ih = new InventoryHandler(userName, inventory, l);
			  inventories.add(new InventoryHolder(ih, true));
			  
			  return 1;
		  }else{
			  return 0;
		  }
		  
	  }
	  
	  protected void setOpenInfoByNickname(String nickname ,boolean value){
		  for(InventoryHolder ih: inventories) {
			  if(ih.ih.getUserName().equals(nickname)){
				  ih.isOpenned = value;
				  return;
			  }
		  }
	  }
	  public InventoryHandler getInventoryHandlerByNickname(String nickname){
		   for(InventoryHolder ih: inventories){
			   if(ih.ih.getUserName().equals(nickname)){
				   return ih.ih;
			   }
		   } 
		   return null;
	  }
	  private void deleteClosedInventories(){
		  ArrayList<InventoryHolder> toDelete = new ArrayList<InventoryHolder>();
		  
		  for(InventoryHolder ih: inventories){
			  if(!ih.isOpenned){
				    toDelete.add(ih);
			  }
		  }
		  for(InventoryHolder ih: toDelete){
			  inventories.remove(ih);
		  }
		  
		  
	  }
	  private Connection getConnection() throws SQLNonTransientConnectionException, SQLException, ClassNotFoundException, Exception{
		  String DB_URL = "jdbc:mariadb://mysql.armagedon132.myjino.ru";
		  
		  String USER = "armagedon132";
		  String PASS = "nC)a)DyD8Tt-";
		  
		  Class.forName("org.mariadb.jdbc.Driver");
		  DriverManager.setLoginTimeout(1);
		  return DriverManager.getConnection(DB_URL, USER, PASS);
	  }
	  protected boolean synchronize(boolean isDisabling){
		  if(isDisabling){
			  for(InventoryHolder ih: inventories){
				  if(ih.isOpenned == true){
					  if(Bukkit.getPlayer(ih.ih.getUserName()) != null){
						  Bukkit.getPlayer(ih.ih.getUserName()).closeInventory();
					  }
					  
					  ih.isOpenned = false;
				  }
			  }
			  return synchronize(false);
		  }

		  //при false
		  ArrayList<InventoryHolder> closedInventories = new ArrayList<InventoryHolder>();
		  for(InventoryHolder ih: this.inventories){
			  if(!ih.isOpenned){
				  closedInventories.add(ih);
			  }
		  }
		  
		  String sql = "SELECT nickname, inventory FROM user_table WHERE nickname IN(";
		  String in = "";
		  for(int i = 0; i < closedInventories.size(); i++){
			  if(closedInventories.size()-1 == i){
				  in += "'"+closedInventories.get(i).ih.getUserName()+"');";
				  break;
			  }
			  in += "'"+closedInventories.get(i).ih.getUserName()+"',";
		  }
		  sql += in;
		  Connection conn = null;
		  Statement stmt = null;
		  boolean hadException = false;
		  
		  try{
			  conn = getConnection();
			  stmt = conn.createStatement();
			  
			  String sql1 = "USE armagedon132;";
			  stmt.executeQuery(sql1);
			  
			  ResultSet rs = stmt.executeQuery(sql);
			  
			  Hashtable<String, String> assembledInventories = new Hashtable<String, String>();
			  
			  while(rs.next()){
				  String nickname = rs.getString("nickname");
				  String dbInventory = rs.getString("inventory");
				  
				  InventoryHandler ih = getInventoryHandlerByNickname(nickname);
				  
				  if(ih != null){
					  assembledInventories.put(nickname, ih.assemble(dbInventory));
				  }
			  }

			  
			  Enumeration<String> keys = assembledInventories.keys();
			  while(keys.hasMoreElements()){
				  String nickname = (String)keys.nextElement();
				  String batch = "UPDATE user_table SET inventory='"+assembledInventories.get(nickname)+"' WHERE nickname='"+nickname+"';";
				  stmt.addBatch(batch);
			  }
			  stmt.executeBatch();
			  
			  conn.close();
			  stmt.close();
			  rs.close();
			  
		  }catch(SQLNonTransientConnectionException e) {
			  l.info("----------------inventory SQLNonTransientConnectionException----------");
		  }catch(SQLException e){
			  e.printStackTrace();
			  hadException = true;
		  }catch(ClassNotFoundException e){
			  e.printStackTrace();
			  hadException = true;
		  }catch(Exception e){
			  hadException = true;
			  e.printStackTrace();
		  }finally{
			  try{
				  if(stmt != null){
					  stmt.close();
				  }
			  }catch(SQLException se){
				  se.printStackTrace();
				  hadException = true;
			  }
			
			  try{
				  if(conn != null){
					  conn.close();
				  }
			  }catch(SQLException se){
				  hadException = true;
				  se.printStackTrace();
			  }
		  }
		  
		  

		  if(hadException){
			  return false;
		  }else{
			  deleteClosedInventories();
			  return true;
		  }
	  }
	  protected boolean give(Player p, ItemStack item){
		  int index = hasFreeSlots(p);
		  if(index == -1){
			  return false;
		  }
		  p.getInventory().setItem(index, item);
		  
		  return true;
	  }
	  public int hasFreeSlots(Player p){
		  PlayerInventory pInv = p.getInventory();
		  
		  for(int i = 0; i < 36; i++){
			  if(pInv.getItem(i) == null){
				  return i;
			  }
		  }
		  
		  return -1;
	  }
	  
	  private void show(){
		  
	  }
}
