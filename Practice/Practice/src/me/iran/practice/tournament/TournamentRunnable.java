package me.iran.practice.tournament;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TournamentRunnable extends BukkitRunnable {

	public void run() {
		
		for(Tournament t : TournamentManager.getManager().getTournaments()) {
			
			if(t.getState() == TournamentState.WAITING && t.getPlayers().size() == 2) {
				t.setState(TournamentState.ACTIVE);
				
				Bukkit.broadcastMessage("Tournament has started " + t.getID());
				
				//Incase someone leaves, we reset everyones position so there are no gaps in the middle
				
				t.updatedPostion();
				
				t.createMatch();
			}
			
		}
		
	}
	
}
