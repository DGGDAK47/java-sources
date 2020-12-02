package org.dggdak47.dbinteract;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.v3.AuthMeApi;
import java.net.SocketTimeoutException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class dbinteract extends JavaPlugin implements Runnable{
	
	private Logger l;
	private AuthMeApi amp;
	
	public dbinteract(){
		this.l = Bukkit.getLogger();
	}
	
	private Connection getConnection() throws SQLNonTransientConnectionException, SQLException, ClassNotFoundException, Exception{
		  String DB_URL = "jdbc:mariadb://mysql.armagedon132.myjino.ru";
		  
		  String USER = "armagedon132";
		  String PASS = "nC)a)DyD8Tt-";
		  
		  Class.forName("org.mariadb.jdbc.Driver");
		  DriverManager.setLoginTimeout(1);
		  return DriverManager.getConnection(DB_URL, USER, PASS);
	}
	
	@Override
	public void onEnable(){
		this.amp = AuthMeApi.getInstance();
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, this, 20*10, 1200);
	}
	@Override
	public void onDisable(){
		
	}
	
	@Override
	public void run(){
		Connection conn = null;
   	    Statement stmt = null;
   	    
   	    try{
   	    	conn = getConnection();
   	    	stmt = conn.createStatement();
   	    	
   	    	stmt.executeQuery("USE armagedon132");
   	    	
   	    	ResultSet rs = stmt.executeQuery("SELECT nickname, password "
                     + "FROM user_registration "
                     + "WHERE ISNULL(is_correct_data)");
   	    	
   	    	ArrayList<String> nicknames = new ArrayList<String>();
			ArrayList<String> passwords = new ArrayList<String>();
			ArrayList<Boolean> isCorrectData = new ArrayList<Boolean>();
			
			while(rs.next()) {
				 nicknames.add(rs.getString("nickname"));
				 passwords.add(rs.getString("password"));
			}
			
			for(int i = 0; i < nicknames.size(); i++) {
				 if(amp.checkPassword(nicknames.get(i), passwords.get(i))) {
					 isCorrectData.add(true);
				 }else {
					 isCorrectData.add(false);
				 }
			}
			
			for(int i = 0; i < nicknames.size(); i++){
				 String nickname = nicknames.get(i);
				 boolean data = isCorrectData.get(i);
				 
				 if(data){
					 stmt.addBatch("UPDATE user_registration SET is_correct_data=TRUE WHERE nickname='"+nickname+"'; ");
				 }else{
					 stmt.addBatch("UPDATE user_registration SET is_correct_data=FALSE WHERE nickname='"+nickname+"'; ");
				 }
			}
			
			stmt.executeBatch();
			
			rs.close();	
   	    }catch(LinkageError e){
   	    	 l.info("---------------- dbinteract LinkageError ------------------------------");
   	    }catch(SQLNonTransientConnectionException e) {
			 l.info("----------------dbinteract SQLNonTransientConnectionException----------");
		}catch(SQLException se){
			 se.printStackTrace();
		}catch(Exception e) {
			 e.printStackTrace();
		}finally {
			 try {
				 if(stmt != null) stmt.close();
			 }catch(SQLException se2) {
			     //Nothing
			 }
			 try {
				 if(conn != null) conn.close();
			 }catch(SQLException se) {
				 se.printStackTrace();
			 }
		}
	}
}