package org.dggdak47.mranks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.dggdak47.dutil.DUtil;
import org.dggdak47.manager.exceptions.PlayerNotExistException;
import org.dggdak47.mfractions.fraction.Fraction;
import org.dggdak47.mfractions.fraction.FractionPlayer;
import org.dggdak47.mranks.kits.Kit;
import org.dggdak47.mranks.kits.exceptions.KitNoExistException;
import org.dggdak47.mranks.ranks.Rank;
import org.dggdak47.mranks.ranks.exceptions.PlayerHasSameRankException;

public class PluginCommands {
	private MRanks plugin;
	
	//show
	public void show(CommandSender commandSender, String[] args){
		if(args[1].equals("score") && args.length <= 2){
			showScore(commandSender);
		}else if(args[1].equals("score") && args.length > 2){
			Player p = Bukkit.getPlayer(args[2]);
			showScoreSmb(commandSender, p);
		}else if(args[1].equals("fraction") && args.length <= 2){
			showFraction(commandSender);
		}else if(args[1].equals("fraction") && args.length > 2) {
			Player p = Bukkit.getPlayer(args[2]);
			showFractionSmb(commandSender, p);
		}else if(args[1].equals("rank") && args.length <= 2){
			showRank(commandSender);
		}else if(args[1].equals("rank") && args.length > 2) {
			Player p = Bukkit.getPlayer(args[2]);
			showRankSmb(commandSender, p);
		}else if(args[1].equals("kits")){
			showKits(commandSender);
		}else if(args[1].equals("ranks")) {
			showRanks(commandSender);
		}else{
			msg(commandSender, "Text.WrongCommand");
		}
	}
	private void showScore(CommandSender commandSender) {
		if(commandSender instanceof ConsoleCommandSender){
			msg(commandSender, "Text.ConsoleCant");
			return;
		}else if( !DUtil.hasPermissionPEX((Player)commandSender, MRanks.accessPermission) ){
			msg(commandSender, "Text.HaveNoPerms");
			return;
		}
		
		FractionPlayer fp = this.plugin.mfractionsApi.getPlayer((Player)commandSender);
		String message = this.plugin.config.getString("Text.ScoreInfo");
		message = String.format(message, fp.getScore());
		commandSender.sendMessage(message);
	}
	private void showScoreSmb(CommandSender commandSender, Player smb) {
		if( !(commandSender instanceof ConsoleCommandSender) && !DUtil.hasPermissionPEX((Player)commandSender, MRanks.adminPermission)){
			msg(commandSender, "Text.HaveNoPerms");
			return;
		}
		
		if(smb == null){
			msg(commandSender, "Text.NoSuchPlayer");
			return;
		}
		
		FractionPlayer fp = this.plugin.mfractionsApi.getPlayer(smb);
		String message = this.plugin.config.getString("Text.ScoreInfoA");
		message = String.format(message, fp.getPlayerName(), fp.getScore());
		commandSender.sendMessage(message);
	}
	private void showFractionSmb(CommandSender commandSender, Player smb) {
		if( !(commandSender instanceof ConsoleCommandSender) && !DUtil.hasPermissionPEX((Player)commandSender, MRanks.adminPermission)){
			msg(commandSender, "Text.HaveNoPerms");
			return;
		}
		
		if(smb == null){
			msg(commandSender, "Text.NoSuchPlayer");
			return;
		}
		
		FractionPlayer fp = this.plugin.mfractionsApi.getPlayer(smb);
		Fraction f = this.plugin.mfractionsApi.getFraction(fp.getFractionID());
		String message = this.plugin.config.getString("Text.FractionInfoA");
		message = String.format(message, fp.getPlayerName(), f.getName());
		commandSender.sendMessage(message);
	}
	private void showFraction(CommandSender commandSender) {
		if(commandSender instanceof ConsoleCommandSender){
			msg(commandSender, "Text.ConsoleCant");
			return;
		}else if(!DUtil.hasPermissionPEX((Player)commandSender, MRanks.accessPermission)){
			msg(commandSender, "Text.HaveNoPerms");
			return;
		}
		
		FractionPlayer fp = this.plugin.mfractionsApi.getPlayer((Player)commandSender);
		Fraction f = this.plugin.mfractionsApi.getFraction(fp.getFractionID());
		
		String message = this.plugin.config.getString("Text.FractionInfo");
		message = String.format(message, f.getName());
		commandSender.sendMessage(message);
	}
	private void showRankSmb(CommandSender commandSender, Player smb) {
		if( !(commandSender instanceof ConsoleCommandSender) && !DUtil.hasPermissionPEX((Player)commandSender, MRanks.adminPermission)){
			msg(commandSender, "Text.HaveNoPerms");
			return;
		}
		
		if(smb == null){
			msg(commandSender, "Text.NoSuchPlayer");
			return;
		}
		
		String message = this.plugin.config.getString("Text.RankInfoA");
	    Integer rankId = this.plugin.manager.hasPlayerRank(smb);
	    if(rankId == null){
	    	message = String.format(message, smb.getName(), "none");
	    }else {
	    	message = String.format(message, smb.getName(), rankId);
	    }
	    
		commandSender.sendMessage(message);
	}
	private void showKits(CommandSender commandSender){
		if( !(commandSender instanceof ConsoleCommandSender) && !DUtil.hasPermissionPEX((Player)commandSender, MRanks.accessPermission)){
			msg(commandSender, "Text.HaveNoPerms");
			return;
		}
		
		msg(commandSender, "Text.ListOfKits");
		ArrayList<Kit> kits = this.plugin.manager.getKits();
		for(Kit kit: kits){
			commandSender.sendMessage(kit.getName()+" | id: "+kit.getId()+" Perm:"+kit.getPermission());
		}
	}
	private void showRanks(CommandSender commandSender) {
		if( !(commandSender instanceof Player) && !DUtil.hasPermissionPEX((Player)commandSender, MRanks.accessPermission)){
			msg(commandSender, "Text.HaveNoPerms");
			return;
		}
		
		ArrayList<Rank> ranks = this.plugin.manager.getRanks();
		msg(commandSender, "Text.ListOfRanks");
		for(Rank rank: ranks){
			commandSender.sendMessage(rank.getName()+" | score:"+rank.getScore()+", id:"+rank.getId());
		}
	}
	private void showRank(CommandSender commandSender){
		if(commandSender instanceof ConsoleCommandSender){
			msg(commandSender, "Text.ConsoleCant");
			return;
		}else if(!DUtil.hasPermissionPEX((Player)commandSender, MRanks.accessPermission)){
			msg(commandSender, "Text.HaveNoPerms");
			return;
		}
		
		String message = this.plugin.config.getString("Text.RankInfo");
		Integer rankId = this.plugin.manager.hasPlayerRank((Player)commandSender);
		
		if(rankId == null){
			message = String.format(message, "none");
		}else {
			Rank rank = this.plugin.manager.getRank(rankId);
			message = String.format(message, rank.getName());
		}
		
		commandSender.sendMessage(message);
	}
	
	//rank
	public void rank(CommandSender commandSender, String[] args){
		if(args[1].equals("join")){
			if(commandSender instanceof ConsoleCommandSender){
				msg(commandSender, "Text.ConsoleCant");
				return;
			}else{
				rankJoin((Player)commandSender, new Integer(args[2]) );
			}
		}else if(args[1].equals("set")){
			Player p = Bukkit.getPlayer(args[2]);
			
			rankSet(commandSender, p, new Integer(args[3]) );
		}else{
			msg(commandSender, "Text.WrongCommand");
		}
	}
	private void rankJoin(Player sender, Integer rankId){
		if(!DUtil.hasPermissionPEX(sender, MRanks.accessPermission)){
			msg(sender, "Text.HaveNoPerms");
			return;
		}
		
		Rank rank = this.plugin.manager.getRank(rankId);
		if(rank == null){
			msg(sender, "Text.NoSuchRank");
			return;
		}
		
		FractionPlayer fp = this.plugin.mfractionsApi.getPlayer(sender);
		if(fp.getScore() < rank.getScore()){
			String message = this.plugin.config.getString("Text.HaveNoEnoughScoreForRank");
			message = String.format(message, fp.getScore(), rank.getScore());
			sender.sendMessage(message);
			return;
		}
		
		try {
			this.plugin.manager.giveRank(sender, rank);
		} catch (PlayerHasSameRankException e) {
			msg(sender, "Text.SameRank");
			return;
		}
		msg(sender, "Text.OnRankJoining");
	}
    private void rankSet(CommandSender commandSender, Player p, Integer rankId){
    	
    	if( (commandSender instanceof Player) && !DUtil.hasPermissionPEX((Player)commandSender, MRanks.adminPermission)){
			msg(commandSender, "Text.HaveNoPerms");
			return;
		}
    	
    	if(p == null) {
    		msg(commandSender, "Text.NoSuchPlayer");
			return;
    	}
    	
    	Rank rank = this.plugin.manager.getRank(rankId);
    	if(rank == null){
    		msg(commandSender, "Text.NoSuchRank");
			return;
    	}
    	
    	try {
			this.plugin.manager.giveRank(p, rank);
		} catch (PlayerHasSameRankException e) {
			msg(commandSender, "Text.SameRank");
			return;
		}
    	msg(commandSender, "Text.OnRankSetting");
	}
    
    //kit
    public void kit(CommandSender commandSender, String[] args) {
    	if(args[1].equals("give")){
    		Player p = Bukkit.getPlayer(args[2]);
    		kitGive(commandSender, p, new Integer(args[3]));
    	}else if(args[1].equals("require")){
    		kitRequire(commandSender, args);
    	}else{
    		msg(commandSender, "Text.WrongCommand");
    	}
    }
    private void kitGive(CommandSender commandSender, Player p, Integer kitId){
    	if( !(commandSender instanceof ConsoleCommandSender) && !DUtil.hasPermissionPEX((Player)commandSender, MRanks.adminPermission)){
    		msg(commandSender, "Text.HaveNoPerms");
    		return;
    	}
    	
    	if(p == null){
    		msg(commandSender, "Text.NoSuchPlayer");
    		return;
    	}
    	
    	Boolean hasKit = this.plugin.manager.hasKit(kitId);
    	if(!hasKit){
    		msg(commandSender, "Text.NoSuchKit");
    		return;
    	}else{
    		try {
				this.plugin.manager.giveKit(p, kitId, true);
				msg(commandSender, "Text.KitGivenA");
			} catch (KitNoExistException e) {
				e.printStackTrace();
			}
    	}
    }
    private void kitRequire(CommandSender cs, String[] args){
    	if( cs instanceof ConsoleCommandSender ){
    		msg(cs, "Text.ConsoleCant");
    		return;
    	}else if(!DUtil.hasPermissionPEX((Player)cs, MRanks.accessPermission)){
    		msg(cs, "Text.HaveNoPerms");
    		return;
    	}
    	//отправитель - игрок и он имеет право на команду
    	Player p = (Player)cs;
    	Integer kitId = this.plugin.manager.hasPlayerKit(p);
    	if(kitId != null){
    		try {
				this.plugin.manager.giveKit(p, kitId, false);
			} catch (KitNoExistException e) {
				e.printStackTrace();
			}
    	}else{
    		msg(p, "Text.PlayerHasNoKits");
    	}
    }
    
    //Add
    public void add(CommandSender commandSender, String[] args) throws PlayerNotExistException {
    	if(args[1].equals("score")){
    		Player p = Bukkit.getPlayer(args[2]);
    		Integer scoreToAdd = new Integer(args[3]);
    		
    		addScore(commandSender, p, scoreToAdd);
    	}else {
    		msg(commandSender, "Text.WrongCommand");
    	}
    }
    private void addScore(CommandSender cs, Player p, Integer score) throws PlayerNotExistException{
    	if(!(cs instanceof ConsoleCommandSender) && !DUtil.hasPermissionPEX((Player)cs, MRanks.adminPermission)){
    		msg(cs, "Text.HaveNoPerms");
    		return;
    	}
    	
    	if(p == null){
    		msg(cs, "Text.NoSuchPlayer");
    		return;
    	}
    	
		this.plugin.mfractionsApi.addScore(p, score);
		String message = this.plugin.config.getString("Text.ScoreAddedA");
		message = String.format(message, score, p.getName());
		cs.sendMessage(message);
    }
	
    protected void msg(CommandSender dest,String msgWay) {
    	dest.sendMessage(this.plugin.config.getString(msgWay));
    }
	public PluginCommands(MRanks plugin){
		this.plugin = plugin;
	}
}
