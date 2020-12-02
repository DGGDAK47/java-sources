package org.dggdak47.mmutants.invasion;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.dggdak47.mmutants.MMutants;
import org.dggdak47.mmutants.wayPoint.WayPoint;

public class InvasionTask implements Runnable{
	private Invasion invasion;
	private MMutants plugin;
	private BukkitTask task;
	
	private ArrayList<Monster> monsters = new ArrayList<Monster>();
	private ArrayList<WayPoint> way;
	private Integer reachedWayPointIndex = 0;
	
	@Override
	public void run() {
		for(Monster m: monsters){
			if(m.isDead()){
				continue;
			}
			
			if(m.getTarget() == null && !m.getMetadata("HasReachedLastWayPoint").get(0).asBoolean() ){
				WayPoint wp = way.get(reachedWayPointIndex);
				m.teleport(wp.getSpawnPoint().getLocation());
				m.setTarget(wp.getEntityPoint().getEntity());
			}else if(m.getMetadata("HasReachedLastWayPoint").get(0).asBoolean()) {
				continue;
			}
			
			Integer currentWayPointTargetIndex = m.getMetadata("TargetWayPointIndex").get(0).asInt();
			WayPoint wayPointByIndex = way.get(currentWayPointTargetIndex);
			
			if(wayPointByIndex != null && !currentWayPointTargetIndex.equals(way.size()-1) && wayPointByIndex.getRegion().isLocationWithin(m.getLocation())){
				m.setMetadata("TargetWayPointIndex", new FixedMetadataValue(plugin, currentWayPointTargetIndex+1) );
				WayPoint newWayPoint = way.get(currentWayPointTargetIndex+1);
				m.setTarget(newWayPoint.getEntityPoint().getEntity());
				if(currentWayPointTargetIndex > reachedWayPointIndex){
					reachedWayPointIndex = currentWayPointTargetIndex;
				}
				
			}else if(currentWayPointTargetIndex.equals(way.size()-1) && wayPointByIndex.getRegion().isLocationWithin(m.getLocation())){
				reachedWayPointIndex = way.size()-1;
				m.setMetadata("HasReachedLastWayPoint", new FixedMetadataValue(plugin, true));
				m.setTarget(null);
			}
		}
	}
	
	public void start() {
		for(WayPoint w: way){
			w.getEntityPoint().spawn();
		}
		Hashtable<EntityType, Integer> attackers = this.invasion.getAttackers();
		Set<EntityType> attackersKeys = attackers.keySet();
		
		Integer attackersAmount;
		World world = Bukkit.getWorld(invasion.getWorldName());
		Entity entity;
		for(EntityType attacker: attackersKeys){
			attackersAmount = attackers.get(attacker);
			try{
				for(int i = 0; i < attackersAmount; i++){
					entity = world.spawnEntity(way.get(0).getSpawnPoint().getLocation(), attacker);
					if(entity == null){
						continue;
					}
					entity.setMetadata("TargetWayPointIndex", new FixedMetadataValue(this.plugin, 0));
					entity.setMetadata("HasReachedLastWayPoint", new FixedMetadataValue(this.plugin, false));
					this.monsters.add((Monster)entity);
				}
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
		}
		
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, this, 0, 50);
	}
	public void stop() {
		for(Monster monster: monsters){
			monster.remove();
		}
		for(WayPoint wp: way){
			wp.getEntityPoint().remove();
		}
		Bukkit.getScheduler().cancelTask(task.getTaskId());
	}
	
	public InvasionTask(Invasion invasion, MMutants plugin){
		this.invasion = invasion;
		this.plugin = plugin;
		
		this.way = invasion.getWay();
	}
}
