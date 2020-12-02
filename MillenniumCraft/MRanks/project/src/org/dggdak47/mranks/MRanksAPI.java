package org.dggdak47.mranks;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.dggdak47.mranks.kits.Kit;
import org.dggdak47.mranks.kits.exceptions.KitNoExistException;
import org.dggdak47.mranks.ranks.Rank;
import org.dggdak47.mranks.ranks.exceptions.PlayerHasSameRankException;

public class MRanksAPI {
	private MRanks plugin;
	
	//kits
	public ArrayList<Kit> getKits(){
		return this.plugin.manager.getKits();
	}
	public boolean hasKit(Integer id) {
		return this.plugin.manager.hasKit(id);
	}
	public void giveKit(Player p, Integer kitId, Boolean admin) throws KitNoExistException{
		this.plugin.manager.giveKit(p, kitId, admin);
	}
	public Kit getKit(Integer id) {
		return this.plugin.manager.getKit(id);
	}
	public Kit getKitByPermission(String permission) {
		return this.plugin.manager.getKitByPermission(permission);
	}
	public Integer hasPlayerKit(Player p) {
		return this.plugin.manager.hasPlayerKit(p);
	}
	
	//ranks
	public ArrayList<Rank> getRanks(){
		return this.plugin.manager.getRanks();
	}
	public boolean hasRank(Integer id){
		return this.plugin.manager.hasRank(id);
	}
	public Rank getRank(Integer id){
		return this.plugin.manager.getRank(id);
	}
	public void giveRank(Player p, Rank rank) throws PlayerHasSameRankException{
		this.plugin.manager.giveRank(p, rank);
	}
	public Integer hasPlayerRank(Player p){
		return this.plugin.manager.hasPlayerRank(p);
	}
	public Rank getPlayerRank(Player p){
		return this.plugin.manager.getPlayerRank(p);
	}
	
	public boolean doesPlayerRankSaves() {
		return this.plugin.manager.doesPlayerRankSaves();
	}
	
	
	public MRanksAPI(MRanks plugin){
		this.plugin = plugin;
	}
}