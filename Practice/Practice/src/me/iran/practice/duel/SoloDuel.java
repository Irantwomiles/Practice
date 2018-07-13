package me.iran.practice.duel;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.arena.Arena;
import me.iran.practice.enums.ArenaState;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.gametype.GameType;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;

public class SoloDuel extends Duel{

	@Getter
	@Setter
	private Player player1, player2, winner, loser;
	
    @Getter
    @Setter
    private Profile profile1, profile2;

    public SoloDuel(Player player1, Player player2, GameType game, Arena arena) {
    
    	super(game, arena);
    	
    	this.player1 = player1;
        
    	this.player2 = player2;
        
    	winner = null;
    	loser = null;
    	
        profile1 = ProfileManager.getManager().getProfileByPlayer(player1);
        profile2 = ProfileManager.getManager().getProfileByPlayer(player2);

        profile1.setState(PlayerState.IN_GAME);
        profile2.setState(PlayerState.IN_GAME);

        arena.setState(ArenaState.IN_USE);
    }
	
}
