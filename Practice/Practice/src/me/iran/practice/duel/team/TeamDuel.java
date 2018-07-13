package me.iran.practice.duel.team;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.arena.Arena;
import me.iran.practice.duel.Duel;
import me.iran.practice.gametype.GameType;

public class TeamDuel extends Duel {

	@Getter
	@Setter
	private Team team1, team2, winner, loser;
	
	public TeamDuel(Team team1, Team team2, GameType game, Arena arena) {
		
		super(game, arena);
		
		this.team1 = team1;
		this.team2 = team2;
		this.winner = null;
		this.loser = null;
	}
	
	/*
	 * Editing this wont do anything, it is purely for checks
	 * Edit the actual set by doing #getTeam1/2().getPlayers()
	 */
	public Set<UUID> getPlayers() {
		
		Set<UUID> players = new HashSet<>();
		
		for(UUID uuid : team1.getPlayers()) {
			players.add(uuid);
		}

		for(UUID uuid : team2.getPlayers()) {
			players.add(uuid);
		}
		
		return players;
	}
	
	/*
	 * Editing this wont do anything, it is purely for checks
	 * Edit the actual set by doing #getTeam1/2().getAlive()
	 */
	public Set<UUID> getAlivePlayers() {
		
		Set<UUID> alive = new HashSet<>();
		
		for(UUID uuid : team1.getAlive()) {
			alive.add(uuid);
		}
		
		for(UUID uuid : team2.getAlive()) {
			alive.add(uuid);
		}
		
		return alive;
	}
	
	public void sendMessage(String message) {
		for(UUID uuid : getAlivePlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			player.sendMessage(message);
		}
	}
}
