package me.iran.practice.tournament;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.duel.SoloDuelManager;
import me.iran.practice.gametype.GameType;
import net.md_5.bungee.api.ChatColor;

@Getter
@Setter
public class Tournament {

	private ArrayList<TPlayer> players, advancing;
	
	private int round;
	
	@Getter
	@Setter
	private static int ID;
	
	private GameType game;
	
	private TournamentState state;
	
	public Tournament(GameType game) {
		
		ID++;
		
		this.game = game;
		
		round = 1;
		
		players = new ArrayList<>();
		advancing = new ArrayList<>();
		
		state = TournamentState.WAITING;
	}
	
	/*
	 * Need to check if any of the players are null, if they are they should
	 * advance or get removed
	 */
	
	public void createMatch() {
		
		for(int i = 0; i < advancing.size(); i++) {
			
			TPlayer player = advancing.get(i);
			
			if((player.getPosition() % 2) != 0) {
				
				TPlayer player2 = advancing.get(i + 1);
				
				if(player2 == null) {
					player.getPlayer().sendMessage(ChatColor.GOLD + "Your opponent could not be found, advancing to the next round!");
					advancing.remove(player2);
					continue;
				}
				
				SoloDuelManager.getManager().createSoloDuel(player.getPlayer(), player2.getPlayer(), game, ArenaManager.getManager().randomArena());

				advancing.remove(player);
				players.add(player);
			}
		}
	}
	
	public void updatedPostion() {
		for(int i = 0; i < advancing.size(); i++) {
			
			TPlayer player = advancing.get(i);
			
			player.setPosition(i + 1);
		
		}
	}
	
}
