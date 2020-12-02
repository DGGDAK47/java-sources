package org.dggdak47.mranks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.dutil.timelabels.exceptions.SuchLabelAlreadyExistsException;
import org.dggdak47.dutil.timeslabels.TimesLabels;
import org.dggdak47.mranks.events.PlayerJoinRankEvent;
import org.dggdak47.mranks.events.PlayerReceiveKitEvent;
import org.dggdak47.mranks.kits.Kit;
import org.dggdak47.mranks.kits.exceptions.KitConstructorDataException;
import org.dggdak47.mranks.kits.exceptions.KitNoExistException;
import org.dggdak47.mranks.ranks.Rank;
import org.dggdak47.mranks.ranks.exceptions.PlayerHasSameRankException;
import org.dggdak47.mranks.ranks.exceptions.RankConstructorDataException;
import org.dggdak47.mranks.ranks.exceptions.RankNoExistsException;

public class Manager {
	private MRanks plugin;
	private ArrayList<Kit> kits = new ArrayList<Kit>();
	private ArrayList<Rank> ranks = new ArrayList<Rank>();
	
	private TimesLabels tlK;
	private Integer kitDelayMinutes;
	private boolean savePlayerRank;
	
	//Kits
	public ArrayList<Kit> getKits(){
		return (ArrayList)this.kits.clone();
	}
	public boolean hasKit(Integer id) {
		for(Kit kit: kits){
			if(kit.getId().equals(id)){
				return true;
			}
		}
		
		return false;
	}
    public void giveKit(Player p, Integer kitId, Boolean admin) throws KitNoExistException{
    	String message;
    	
    	if(!DUtil.hasPermissionPEX(p, MRanks.kitDelayExemptPermission) && !admin){
    		if( tlK.hasLabel(p.getName()) && tlK.hasTimeExpiredForLabel(p.getName())){
    			//время ограничения истекло и следует удалить
    			tlK.removeLabel(p.getName());
    		}else{
    			// время ограничения не истекло (код return и вывод сообщ) или ограничения нет
    			LocalDateTime nextAvailableKitTime = tlK.expiryLabelDate(p.getName());
    			message = this.plugin.config.getString("Text.NextKitTime");
    			if(nextAvailableKitTime != null){
    				message = String.format(message, nextAvailableKitTime.getHour()+":"+nextAvailableKitTime.getMinute()+":"+nextAvailableKitTime.getSecond() );
    				p.sendMessage(message);
    				return;
    			}
    		}
    		//по верхней логике вызовется, если в хранилище нет такого (label)
    		tlK.addLabel(p.getName());
    	}
    	
    	//выдача кита
		PlayerInventory inv = p.getInventory();
		Kit kit = getKit(kitId);
		if(kit == null){
			throw new KitNoExistException("Kit with id: "+kitId+" doesn't exist!");
		}
		ArrayList<ItemStack> kitContent = kit.getContent();
		
		for(ItemStack item: kitContent){
			inv.addItem(item);
		}
		Bukkit.getPluginManager().callEvent(new PlayerReceiveKitEvent(p, kit));
		
		//сообщение о следующем доступном наборе, после 
		if(!DUtil.hasPermissionPEX(p, MRanks.kitDelayExemptPermission) && !admin){
			LocalDateTime nextAvailableKitTime = tlK.expiryLabelDate(p.getName());
			message = this.plugin.config.getString("Text.NextKitTime");
			if(nextAvailableKitTime != null){
        		message = String.format(message, nextAvailableKitTime.getHour()+":"+nextAvailableKitTime.getMinute()+":"+nextAvailableKitTime.getSecond() );
        		p.sendMessage(message);
    		}
		}
	}
	public Kit getKit(Integer id) {
		for(Kit kit: kits){
			if(kit.getId().equals(id)){
				return kit;
			}
		}
		
		return null;
	}
	public Kit getKitByPermission(String permission){
		for(Kit kit: kits){
			if(kit.getPermission().equals(permission)){
				return kit;
			}
		}
		
		return null;
	}
	public Integer hasPlayerKit(Player p) {
		if(!DUtil.hasPermissionPEX(p, MRanks.kitAccessPermission)){
			return null;
		}
		
		for(Kit kit: kits){
			if(DUtil.hasPermissionPEX(p, kit.getPermission())){
				return new Integer(kit.getId());
			}
		}
		
		return null;
	}
	
	//Ranks
	public ArrayList<Rank> getRanks(){
		return (ArrayList)this.ranks.clone();
	}
    public boolean hasRank(Integer id) {
		for(Rank rank: ranks){
			if(rank.getId().equals(id)){
				return true;
			}
		}
		
		return false;
	}
    public Rank getRank(Integer id){
    	for(Rank rank: ranks){
			if(rank.getId().equals(id)){
				return rank;
			}
		}
		
		return null;
    }
    public void giveRank(Player p, Rank rank) throws PlayerHasSameRankException{
		Integer currentRankId = hasPlayerRank(p);
		if(currentRankId != null && currentRankId.equals(rank.getId())){
			throw new PlayerHasSameRankException("Player with name: "+p.getName()+" has same rank already!");
		}
		
		this.plugin.mfractionsApi.resetPermissionGroups(p, true);
		DUtil.addPermissionGroupsBottom(p, rank.getPermissionGroups());
		Bukkit.getPluginManager().callEvent(new PlayerJoinRankEvent(p, rank));
		
		Integer kit = hasPlayerKit(p);
		if(kit != null){
			try {
				giveKit(p, kit, false);
			} catch (KitNoExistException e) {
				e.printStackTrace();
			}
		}
	}
	public Integer hasPlayerRank(Player p){
		for(Rank rank: ranks){
			if(DUtil.hasPermissinGroups(p, rank.getPermissionGroups())){
				return rank.getId();
			}
		}
		
		return null;
	}
	public Rank getPlayerRank(Player p){
		for(Rank rank: ranks){
			if(DUtil.hasPermissinGroups(p, rank.getPermissionGroups())){
				return rank;
			}
		}
		
		return null;
	}
	
	
	public boolean doesPlayerRankSaves() {
		return this.savePlayerRank;
	}
	public Manager(MRanks plugin){
		this.plugin = plugin;
		
		//kits initializing
		Map kits = this.plugin.config.getMap("Kits");
		Set kitsKeys = kits.keySet();
		for(Object key: kitsKeys){
			try{
				this.kits.add(new Kit( (Map)(kits.get(key)) ));
			}catch(KitConstructorDataException e){
				e.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//ranks initializing
		Map ranks = this.plugin.config.getMap("Ranks");
		Set ranksKeys = ranks.keySet();
		for(Object key: ranksKeys){
			try{
				this.ranks.add( new Rank( (Map)(ranks.get(key)) ) );
			}catch(RankConstructorDataException e){
				e.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		this.savePlayerRank = this.plugin.config.getBoolean("GeneralOptions.SavePlayerRank");
		this.kitDelayMinutes = this.plugin.config.getInt("GeneralOptions.KitDelayMinutes");
		tlK = new TimesLabels(new Long(0), new Long(0), new Long(0), new Long(0), new Long(this.kitDelayMinutes), new Long(0));
	}
}
