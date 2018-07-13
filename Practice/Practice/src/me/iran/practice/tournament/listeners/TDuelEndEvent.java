package me.iran.practice.tournament.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.iran.practice.Practice;
import me.iran.practice.duel.SoloDuel;
import me.iran.practice.duel.SoloDuelManager;
import me.iran.practice.duel.listeners.DuelEnd;
import me.iran.practice.duel.listeners.DuelRemoved;
import me.iran.practice.tournament.TPlayer;
import me.iran.practice.tournament.Tournament;
import me.iran.practice.tournament.TournamentManager;
import me.iran.practice.tournament.TournamentState;
import net.md_5.bungee.api.ChatColor;

public class TDuelEndEvent implements Listener {
	
	
	@EventHandler
	public void onEnd(DuelEnd event) {
		
		
		TPlayer twinner = TournamentManager.getManager().getTPlayer(event.getDuel().getWinner());
		TPlayer tloser = TournamentManager.getManager().getTPlayer(event.getDuel().getLoser());
		
		if(twinner == null || tloser == null) {
			return;
		}
		
		if((twinner.getPosition() % 2) == 0) {
			twinner.setPosition(twinner.getPosition() / 2);
		} else if((tloser.getPosition() % 2) == 0) {
			twinner.setPosition(tloser.getPosition() / 2);
		}
		
		Tournament tournament = TournamentManager.getManager().getTournamentByTPlayer(twinner);
		
		tournament.getPlayers().remove(tloser);
		
		Bukkit.broadcastMessage(ChatColor.RED + tloser.getPlayer().getName() + ChatColor.GOLD + " has been eliminated");
		
	}
	
	@EventHandler
	public void onRemoved(DuelRemoved event) {
		
		TPlayer twinner = TournamentManager.getManager().getTPlayer(event.getDuel().getWinner());
		
		Tournament tournament = TournamentManager.getManager().getTournamentByTPlayer(twinner);
		
		if (advance(tournament)) {

			if (tournament.getPlayers().size() == 1) {
				Bukkit.broadcastMessage(ChatColor.AQUA + twinner.getPlayer().getName() + ChatColor.DARK_AQUA + " has won the tournament!");
				tournament.getPlayers().clear();
				TournamentManager.getManager().getTournaments().remove(tournament);
				return;
			}

			tournament.setState(TournamentState.TRANSITION);

			Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {

				public void run() {
					tournament.setState(TournamentState.ACTIVE);
					tournament.setRound(tournament.getRound() + 1);

					tournament.createMatch();

					Bukkit.broadcastMessage(ChatColor.GREEN + "Tournament is now advancing to round " + ChatColor.BLUE + tournament.getRound());
				}

			}, 20 * 5);

		}
	}
	
	private boolean advance(Tournament tournament) {
		
		for(SoloDuel duel : SoloDuelManager.getDuelSet()) {
			
			for(TPlayer player : tournament.getPlayers()) {
				
				if(duel.getPlayer1().getName().equalsIgnoreCase(player.getPlayer().getName()) || duel.getPlayer2().getName().equalsIgnoreCase(player.getPlayer().getName())) {
					return false;
				}
			}
			
		}
		
		return true;
		
	}
	

}
