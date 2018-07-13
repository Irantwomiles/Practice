package me.iran.practice.profile;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.duel.SendDuel;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.utils.StoreInventory;

public class Profile {

    @Getter
    @Setter
    private String uuid;

    @Getter
    @Setter
    private int kill, death, premium, token;

    @Getter
    @Setter
    private boolean spectatorMessage, allowDuel;
    
    @Getter
    @Setter
    private PlayerState state;

    @Getter
    @Setter
    private HashMap<UUID, SendDuel> duelRequest;
    
    @Getter
    @Setter
    private HashMap<UUID, StoreInventory> storeInv;
    
    @Getter
    @Setter
    private Set<Elo> elo;
    
    @Getter
    @Setter
    private int intTimer = 0;
    
    @Getter
    @Setter
    private int rankedPlayed, unrankedPlayed, premiumPlayed, rankedWon, unrankedWon, premiumWon;
    
    public Profile(String uuid) {
        this.uuid = uuid;
        this.state = PlayerState.LOBBY;
        
        duelRequest = new HashMap<>();
        storeInv = new HashMap<>();
        
        allowDuel = true;
        spectatorMessage = true;
        
        elo = new HashSet<>();
    }

    public String getName(){
        //avoids NPE if the player is offline for some reason
        String name = Bukkit.getOfflinePlayer(UUID.fromString(this.uuid)).getName();

        return name;
    }
    
    public String KDR() {
    	
    	NumberFormat formatter = new DecimalFormat("#0.00");  
    	
    	if(death == 0) {
    		return formatter.format(kill);
    	}
    	
    	double kdr = ((double)kill/(double)death);
    	
    	return formatter.format(kdr);
    }
    
  public int getPlayerElo(Player player, String game) {
    	for(Elo elo : getElo()) {
    		if(elo.getGame().getName().equalsIgnoreCase(game)) {
    			return elo.getElo();
    		}
    	}
    	
    	return -1;
    }
  
	public void setPlayerElo(Player player, String game, int newElo) {
		for (Elo elo : getElo()) {
			if (elo.getGame().getName().equalsIgnoreCase(game)) {
				elo.setElo(newElo);
				return;
			}
		}
	}
}
