package org.dggdak47.database.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class PluginDB {
	private String pluginName;
	private String dbFileName;
	
	public Connection getConnection() throws SQLException {
		Properties p = System.getProperties();
		String sep = p.getProperty("file.separator");
		String dir = p.getProperty("user.dir");
		
		String url = "jdbc:sqlite:"+dir+sep+"plugins"+sep+this.pluginName+sep+this.dbFileName;
		
		return DriverManager.getConnection(url);
	}
	
	public ResultSet executeQuery(String query) throws SQLException{
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery(query);
		
		return rs;
	}
	
	public int executeUpdate(String query) throws SQLException{
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		
		int result = stmt.executeUpdate(query);
		stmt.close();
		conn.close();
		return result;
	}
	
	public boolean execute(String query) throws SQLException{
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		
		Boolean bool = stmt.execute(query);
		
		stmt.close();
		conn.close();
		return bool;
	}
	
	
	
	public PluginDB(String pluginName, String dbFileName) throws SQLException{
		this.pluginName = pluginName;
		this.dbFileName = dbFileName;
		
		Connection conn = getConnection();
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
