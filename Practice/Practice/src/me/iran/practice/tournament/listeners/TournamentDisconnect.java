package me.iran.practice.tournament.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.iran.practice.tournament.TPlayer;
import me.iran.practice.tournament.Tournament;
import me.iran.practice.tournament.TournamentManager;
import me.iran.practice.tournament.TournamentState;

public class TournamentDisconnect implements Listener {
	
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		TPlayer tplayer = TournamentManager.getManager().getTPlayer(player);
		
		if(tplayer != null) {
			
			/*
			 * If the tournament is active, we will just set the player to null and get removed
			 * once a new round starts, but if its at the waiting stage we will just remove
			 * them.
			 */
			
			Tournament tournament = TournamentManager.getManager().getTournamentByTPlayer(tplayer);
			
			if(tournament != null) {
				
				if(tournament.getState() == TournamentState.WAITING) {
			
					if(tournament.getPlayers().contains(tplayer)) {
						tournament.getPlayers().remove(tplayer);
					}
					
					return;
					
				} else if(tournament.getState() == TournamentState.TRANSITION){
					if(tournament.getPlayers().contains(tplayer)) {
						tournament.getPlayers().remove(tplayer);
					}
				} else {
					tplayer.setPlayer(null);
				}
			}
		
		}
		
	}

}
