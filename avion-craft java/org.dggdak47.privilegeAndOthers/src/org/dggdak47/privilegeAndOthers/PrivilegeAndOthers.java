package org.dggdak47.privilegeAndOthers;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PrivilegeAndOthers extends JavaPlugin implements Runnable{
	
	 private Logger l;
	 
	 public static String getPrivilegeGroupByOrderId(String orderId){
		   orderId = orderId.substring(0, orderId.length()-1);
		 
		   if(orderId.equals("fly")){
			   return "fly";
		   }else if(orderId.equals("vip")){
			   return "vip";
		   }else if(orderId.equals("Mod")) {
			   return "Moderator";
		   }else if(orderId.equals("Admin")) {
			   return "Administrator";
		   }else if(orderId.equals("Owner")) {
			   return "Owner";
		   }else {
			   return null;
		   }
	 }
	 public static String getPrivilegeExpirity(String orderId){
		 String privilegeExpirity = ""; 
		 
		 if(new Character(orderId.charAt(orderId.length()-1)).equals('F')){
			 privilegeExpirity = "forever";
		 }
		 
		 if(privilegeExpirity.equals("forever")){
			 return "2022-05-15";
		 }else{
			 LocalDateTime ldt = LocalDateTime.now();
			 
			 
			 int year = ldt.getYear();
			 int month = ldt.getMonthValue();
			 String sMonth = "";
			 
			 if(Integer.compare(month, 12) == 0){
				 year++;
				 month = 1;
			 }else{
				 month++;
			 }
			 
			 if(month <= 9){
				 sMonth = "0"+String.valueOf(month);
			 }else{
				 sMonth = String.valueOf(month);
			 }
			 return year+"-"+sMonth+"-"+ldt.getDayOfMonth();
			 
		 }
	 }
	 private static void setOthers(String playerName, String others){
		 
	 }
	 private Connection getConnection() throws SQLNonTransientConnectionException, SQLException, ClassNotFoundException, Exception{
		  String DB_URL = "jdbc:mariadb://mysql.armagedon132.myjino.ru";
		  
		  String USER = "armagedon132";
		  String PASS = "sabnkmvcD5=e";
		  
		  Class.forName("org.mariadb.jdbc.Driver");
		  DriverManager.setLoginTimeout(1);
		  return DriverManager.getConnection(DB_URL, USER, PASS);
	  }
	 private void setPlayerGroup(String playerName, String group){
		  PermissionUser user = PermissionsEx.getUser(playerName);
		  user.removeGroup(user.getGroupNames()[0]);
		  Player p = Bukkit.getPlayer(playerName);
		  
		  if(group == null){
			  String[] groupNames = user.getGroupNames();
			  
			  for(String s: groupNames){
				  user.removeGroup(s);
			  }
			  Bukkit.getPlayer(playerName).kickPlayer("Процесс перерождения в игрока");
			  return;
		  }
		  
		  String[] groups = {group, "default"};
		  Bukkit.getPlayer(playerName).kickPlayer("Процесс перерождения в "+group);
		  user.setGroups(groups);
	 }
	 
	 
     @Override
     public void run(){
    	 Connection conn = null;
    	 Statement stmt = null;
    	 
    	 try{
    		 conn = getConnection();
    		 stmt = conn.createStatement();
    		 
    		 stmt.executeQuery("USE armagedon132");
    		 
    		 String sql = "SELECT nickname, order_id FROM expectantpurchases WHERE isChecked = TRUE;";
    		 ResultSet rs = stmt.executeQuery(sql);
    		 String sql1 = "SELECT nickname FROM privilegedplayers;";
    		 ResultSet rs1 = stmt.executeQuery(sql1);
    		 
    		 String sql2 = "SELECT nickname FROM privilegedplayers WHERE privilegeExpiry <= CAST(now() AS DATE); ";
    		 ResultSet rs2 = stmt.executeQuery(sql2);
    		 
    		//Затем мы должны добавить на удаление из privilegedplayers тех строк где nickname равен
    		 //одному из nickname в rs2
    		 while(rs2.next()){
    			 String nickname = rs2.getString("nickname");
    			 if(Bukkit.getPlayer(nickname) == null){
    				 continue;
    			 }
    			 
    			 setPlayerGroup(nickname, null);
    			 stmt.addBatch("DELETE FROM privilegedplayers WHERE nickname='"+nickname+"';");
    		 }
    		 
    		 //Сперва мы перебираем rs, добавляя соответствующим игрокам группы из order_id и добавляя их
    		 //в batch запроса, для вставки их в privilegedPlayers
    		 while(rs.next()){
    			 String nickname = rs.getString("nickname");
    			 String order_id = rs.getString("order_id");
    			 
    			 if(Bukkit.getPlayer(nickname) == null){
    				 continue;
    			 }
    			
    			 
    			 setPlayerGroup(nickname, getPrivilegeGroupByOrderId(order_id));
    			 
    			 boolean isInPP = false;
    			 
    			 while(rs1.next()){
    				 String nickname2 = rs1.getString("nickname");
    				 if(nickname.equals(nickname2)){
    					 isInPP = true;
    				 }
    			 }
    			 
    			 stmt.addBatch("DELETE FROM expectantpurchases WHERE nickname='"+nickname+"';");
    			 if(isInPP){
    				 stmt.addBatch("UPDATE privilegedplayers SET privilege='"+order_id+"', privilegeExpiry='"+getPrivilegeExpirity(order_id)+"' WHERE nickname='"+nickname+"';");
    			 }else{
    				 stmt.addBatch("INSERT INTO privilegedplayers VALUES('"+nickname+"', '"+order_id+"', '"+getPrivilegeExpirity(order_id)+"');");
    			 }
    			 
    		 }
    		 
    		 
    		 //И просто удаляем из expectantpurchases те строки, где isChecked = false/true
    		 stmt.addBatch("DELETE FROM expectantpurchases WHERE isChecked = FALSE;");
    		 stmt.executeBatch();
    		 
    		 rs.close();
    	 }catch(LinkageError e){
    		  l.info("----------------PAO LinkageError -------------------------------");
    	 }catch(SQLNonTransientConnectionException e) {
			  l.info("----------------PAO SQLNonTransientConnectionException----------");
		  }catch(SQLException e){
			  e.printStackTrace();
		  }catch(ClassNotFoundException e){
			  e.printStackTrace();
		  }catch(Exception e){
			  e.printStackTrace();
		  }finally{
			  try{
				  if(stmt != null){
					  stmt.close();
				  }
			  }catch(SQLException se){
				  se.printStackTrace();
			  }
			  
			  try{
				  if(conn != null){
					  conn.close();
				  }
			  }catch(SQLException se){
				  se.printStackTrace();
			  }
		  }
     }
     
     @Override
     public void onEnable(){
    	 // 6000 - 5 мин
    	 this.l = Bukkit.getLogger();
    	 Bukkit.getScheduler().runTaskTimerAsynchronously(this, this, 20*10, 1200);
     }
}
