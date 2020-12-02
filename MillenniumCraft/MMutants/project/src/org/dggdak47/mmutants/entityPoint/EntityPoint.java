package org.dggdak47.mmutants.entityPoint;

import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class EntityPoint {
	private LivingEntity entity = null;
	private Location loc;
	private EntityType entityType;
	
	public EntityType getType() {
		return entityType;
	}
	public Location getLocation() {
		return loc;
	}
	public LivingEntity getEntity() {
		return entity;
	}
	public void remove() {
		if(entity == null){
			return;
		}
		this.entity.remove();
	}
	public void update() {
		if(entity == null){
			return;
		}
		this.entity.remove();
		this.entity = (LivingEntity) loc.getWorld().spawnEntity(loc, entityType);
	}
	public void spawn() {
		this.entity = (LivingEntity)loc.getWorld().spawnEntity(loc, entityType);
		this.entity.setAI(false);
		this.entity.setInvulnerable(true);
		this.entity.setCollidable(false);
	}
	
	public EntityPoint(Location loc, EntityType type) {
		this.entityType = type;
		this.loc = loc;
		
		
	}
}