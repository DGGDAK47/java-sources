package org.dggdak47.jobEvents;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.gamingmesh.jobs.api.JobsPaymentEvent;
import com.earth2me.essentials.api.Economy;

public class JobEvents extends JavaPlugin implements Listener{
	
	private Logger l;
	
	@Override
	public void onEnable(){
		this.l = Bukkit.getLogger();
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onPayment(JobsPaymentEvent e){
		try{
			String playerName = e.getPlayer().getName();
			double currentMoney = Economy.getMoney(playerName);
			
			
			Economy.setMoney(playerName, currentMoney + e.getAmount());
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}
}
