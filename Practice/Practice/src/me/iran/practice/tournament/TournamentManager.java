package me.iran.practice.tournament;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import lombok.Getter;

public class TournamentManager {

	@Getter
	private List<Tournament> tournaments = new ArrayList<>();
	
	private static TournamentManager manager;
	
	private TournamentManager() {}
	
	public static TournamentManager getManager() { 
		
		if(manager == null) {
			manager = new TournamentManager();
		}
		
		return manager;
	}

	
	public Tournament getTournamentByID(int id) {
		for (Tournament t : tournaments) {
			if (t.getID() == id) {
				return t;
			}
		}
		return null;
	}
	
	public Tournament getTournamentByTPlayer(TPlayer player) {
		for (Tournament t : tournaments) {
			if(t.getPlayers().contains(player)) {
				return t;
			}
		}
		return null;
	}
	
	public TPlayer getTPlayer(Player player) {
		for (Tournament t : tournaments) {
			for(TPlayer tp : t.getPlayers()) {
				if(tp.getPlayer().getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
					return tp;
				}
			}
		}
		return null;
	}
	
	
}
