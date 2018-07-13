package me.iran.practice.duel.team;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.iran.practice.arena.Arena;
import me.iran.practice.duel.team.listeners.TeamDuelEnd;
import me.iran.practice.duel.team.listeners.TeamDuelStart;
import me.iran.practice.enums.DuelType;
import me.iran.practice.enums.PartyState;
import me.iran.practice.gametype.GameType;
import me.iran.practice.party.Party;
import me.iran.practice.party.PartyManager;
import net.md_5.bungee.api.ChatColor;

public class TeamDuelManager {

	@Getter
    private static Set<TeamDuel> duelSet = new HashSet<>();

    private static TeamDuelManager td;

	private TeamDuelManager() {}

	public static TeamDuelManager getManager() {
		if (td == null)
			td = new TeamDuelManager();

		return td;
	}
	
	public void createTeamDuel(Party party1, Party party2, GameType game, Arena arena) {
		
		if(!canPartyDuel(party1)) {
			return;
		}
		
		if(!canPartyDuel(party2)) {
			return;
		}
		
		if(arena == null) {
			PartyManager.getManager().sendPartyMessage(party1, ChatColor.RED + "Error starting up the Arena, please try again!");
			PartyManager.getManager().sendPartyMessage(party2, ChatColor.RED + "Error starting up the Arena, please try again!");
			return;
		}
		
		TeamDuel duel = new TeamDuel(new Team(), new Team(), game, arena);
		
		for(UUID uuid : party1.getMembers()) {
			duel.getTeam1().getAlive().add(uuid);
			duel.getTeam1().getPlayers().add(uuid);
			duel.getTeam1().getAllPlayers().add(uuid);
		}
		
		for(UUID uuid : party2.getMembers()) {
			duel.getTeam2().getAlive().add(uuid);
			duel.getTeam2().getPlayers().add(uuid);
			duel.getTeam2().getAllPlayers().add(uuid);
		}
		
		party1.setState(PartyState.IN_GAME);
		party2.setState(PartyState.IN_GAME);
		
		duel.setType(DuelType.TEAM_UNRANKED);
		
		duelSet.add(duel);
		
		Bukkit.getServer().getPluginManager().callEvent(new TeamDuelStart(duel));
		
	}
	
	public void endTeamDuel(Player winner, TeamDuel duel) {
		
		if(duel == null) {
			return;
		}
		
		if(duel.getTeam1().getAlive().contains(winner.getUniqueId())) {
			duel.setWinner(duel.getTeam1());
			duel.setLoser(duel.getTeam2());
			
		} else if(duel.getTeam2().getAlive().contains(winner.getUniqueId())) {
			duel.setWinner(duel.getTeam2());
			duel.setLoser(duel.getTeam1());
		}
		
		Bukkit.getServer().getPluginManager().callEvent(new TeamDuelEnd(duel));
		
	}
	
	public boolean sameDuel(Player player1,TeamDuel duel) {
		
		if(duel.getTeam1().getAlive().contains(player1.getUniqueId()) || duel.getTeam2().getAlive().contains(player1.getUniqueId())) return true;
		
		return false;
	}
	
	public boolean canPartyDuel(Party party) {
		
		if(party.getState() != PartyState.LOBBY) {
			return false;
		}
		
		return true;
	}
	
	public TeamDuel getDuelByPlayer(Player player) {
		
		for(TeamDuel duel : duelSet) {
			if(duel.getPlayers().contains(player.getUniqueId())) {
				return duel;
			}
		}
		
		return null;
	}
	
	public TeamDuel getDuelByAlivePlayer(Player player) {
		for(TeamDuel duel : duelSet) {
			if(duel.getAlivePlayers().contains(player.getUniqueId())) {
				return duel;
			}
		}
		
		return null;
	}
	
	public boolean isPlayerAliveInDuel(Player player) {
		for(TeamDuel duel : duelSet) {
			if(duel.getAlivePlayers().contains(player.getUniqueId())) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isPlayerInDuel(Player player) {
		for(TeamDuel duel : duelSet) {
			if(duel.getPlayers().contains(player.getUniqueId())) {
				return true;
			}
		}
		
		return false;
	}
	
	public void sendPlayersMessage(TeamDuel duel, String message) {
		for(UUID uuid : duel.getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			
			if(player != null) {
				player.sendMessage(message);
			}
		}
	}
}
