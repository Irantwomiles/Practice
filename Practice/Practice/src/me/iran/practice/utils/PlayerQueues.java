package me.iran.practice.utils;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.Practice;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.duel.SoloDuelManager;
import me.iran.practice.duel.team.TeamDuelManager;
import me.iran.practice.enums.DuelType;
import me.iran.practice.enums.PartyState;
import me.iran.practice.gametype.GameType;
import me.iran.practice.gametype.GameTypeManager;
import me.iran.practice.party.Party;
import me.iran.practice.party.PartyManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import net.md_5.bungee.api.ChatColor;

public class PlayerQueues {

	@Getter
	@Setter
	private static HashMap<UUID, Integer> increment = new HashMap<>();
	
	public void findUnrankedMatch() {
		for(GameType game : GameTypeManager.getGames()) {
			if(game.getUnranked().size() > 1) {
				if(ArenaManager.getAvailableArenas().size() > 0) {
					
					Player player1 = Bukkit.getPlayer(game.getUnranked().get(0));
					Player player2 = Bukkit.getPlayer(game.getUnranked().get(1));
					
					player1.getInventory().clear();
					player2.getInventory().clear();
					
					SoloDuelManager.getManager().createSoloDuel(player1 ,player2, game, ArenaManager.getManager().randomArena());
					
					SoloDuelManager.getManager().getDuelByPlayer(player1).setType(DuelType.SOLO_UNRANKED);
					
					game.getUnranked().remove(player1.getName());
					game.getUnranked().remove(player2.getName());
				}
			}
		}
	}
	
	public void findPremiumMatch() {
		for(GameType game : GameTypeManager.getGames()) {
			if(game.getPremium().size() > 1) {
				
				if(ArenaManager.getAvailableArenas().size() > 0) {
					
					Player player1 = Bukkit.getPlayer(game.getPremium().get(0));
					Player player2 = Bukkit.getPlayer(game.getPremium().get(1));
					
					Profile profile1 = ProfileManager.getManager().getProfileByPlayer(player1);
					Profile profile2 = ProfileManager.getManager().getProfileByPlayer(player2);
					
					SoloDuelManager.getManager().createSoloDuel(player1 ,player2, game, ArenaManager.getManager().randomArena());
					
					SoloDuelManager.getManager().getDuelByPlayer(player1).setType(DuelType.PREMIUM);
					
					profile1.setPremium(profile1.getPremium() - 1);
					profile2.setPremium(profile2.getPremium() - 1);
					
					game.getPremium().remove(player1.getName());
					game.getPremium().remove(player2.getName());
					
				}
			}
		}
	}
	
	public void findRankedMatch() {
		
		for(GameType game : GameTypeManager.getGames()) {
			
			if(game.getRanked().size() > 1) {
				
				//Player 1
				
				for(int i = 0; i < game.getRanked().size(); i++) {
					
					Player player1 = Bukkit.getPlayer(game.getRanked().get(i));
					
					Profile profile1 = ProfileManager.getManager().getProfileByPlayer(player1);
					
					if(player1 == null) return;
					
					for(int j = 0; j < game.getRanked().size(); j++) {
						
						Player player2 = Bukkit.getPlayer(game.getRanked().get(j));
						
						if(player2 == null) return;
						
						if(player1.getUniqueId() == player2.getUniqueId()) continue;
						
						Profile profile2 = ProfileManager.getManager().getProfileByPlayer(player2);
						
						int elo1 = profile1.getPlayerElo(player1, game.getName());
						int elo2 = profile2.getPlayerElo(player2, game.getName());
						
						int max = elo2 + getIncrement().get(player1.getUniqueId());
						int min = elo2 - getIncrement().get(player1.getUniqueId());
						
						if(elo1 >= min && elo1 <= max) {
							SoloDuelManager.getManager().createRankedSoloDuel(player1, player2, game, ArenaManager.getManager().randomArena());
						}
					}
					
				}
				
			}
			
		}
		
	}
	
	public void findPartyUnrankedMatch() {
		for(int i = 0; i < GameTypeManager.getGames().size(); i++) {
			
			GameType game = GameTypeManager.getGames().get(i);
			
			if(game.getPartyUnranked().size() > 1) {
				if(ArenaManager.getAvailableArenas().size() > 0) {
					
					Party party1 = game.getPartyUnranked().get(0);
					Party party2 = game.getPartyUnranked().get(1);
					
					party1.setState(PartyState.LOBBY);
					party2.setState(PartyState.LOBBY);
					
					TeamDuelManager.getManager().createTeamDuel(party1 , party2, game, ArenaManager.getManager().randomArena());
					
					//TeamDuelManager.getManager().getDuelByAlivePlayer(Bukkit.getPlayer(party1.getLeader())).setType(DuelType.TEAM_UNRANKED);
					
					game.getPartyUnranked().remove(party1);
					game.getPartyUnranked().remove(party2);
				}
			}
		}
	}
	
	public void joinUnranked(Player player, GameType game) {
		
		if(!game.getUnranked().contains(player.getName())) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("join-unranked-queue").replace("%size%", game.getUnranked().size() + "").replace("%gametype%", game.getName())));
			game.getUnranked().add(player.getName());
		}
		
	}
	
	public void joinPartyUnranked(Party party, GameType game) {
		
		if(!game.getPartyUnranked().contains(party)) {
			
			if(party.getMembers().size() != 1) {
				Bukkit.getPlayer(party.getLeader()).sendMessage(ChatColor.RED + "You must have exactly 2 people in your party to join 2v2 Queue!");
				return;
			}
			
			party.setState(PartyState.IN_QUEUE);
			
			PartyManager.getManager().sendPartyMessage(party, ChatColor.YELLOW + "Your party has joined Unranked 2v2 Queue for " + ChatColor.RED + game.getName());
			game.getPartyUnranked().add(party);
		}
		
	}
	
	public void joinRanked(Player player, GameType game) {
		
		if(!game.getRanked().contains(player.getName())) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("join-ranked-queue").replace("%size%", game.getRanked().size() + "").replace("%gametype%", game.getName())));
			game.getRanked().add(player.getName());
		}
		
	}
	
	public void joinPremium(Player player, GameType game) {
		
		if(!game.getPremium().contains(player.getName())) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("join-premium-queue").replace("%size%", game.getPremium().size() + "").replace("%gametype%", game.getName())));
			game.getPremium().add(player.getName());
		}
		
	}
	
	public void leaveUnrankedQueue(Player player) {
		for(GameType game : GameTypeManager.getGames()) {
			if(game.getUnranked().contains(player.getName())) {
				game.getUnranked().remove(player.getName());
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("leave-queue.unranked").replace("%gametype%", game.getName())));
				return;
			}
		}
	}
	
	public void leavePartyUnrankedQueue(Party party) {
		for (GameType game : GameTypeManager.getGames()) {
			if (game.getPartyUnranked().contains(party)) {
				game.getPartyUnranked().remove(party);
				
				party.setState(PartyState.LOBBY);
				
				PartyManager.getManager().sendPartyMessage(party, ChatColor.YELLOW
						+ "Your party has left Unranked 2v2 Queue for " + ChatColor.RED + game.getName());
				return;
			}
		}
	}
	
	public void leaveRankedQueue(Player player) {
		for(GameType game : GameTypeManager.getGames()) {
			if(game.getRanked().contains(player.getName())) {
				game.getRanked().remove(player.getName());
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("leave-queue.ranked").replace("%gametype%", game.getName())));
				return;
			}
		}
	}
	
	public void leavePremiumQueue(Player player) {
		for(GameType game : GameTypeManager.getGames()) {
			if(game.getPremium().contains(player.getName())) {
				game.getPremium().remove(player.getName());
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("leave-queue.premium").replace("%gametype%", game.getName())));
				return;
			}
		}
	}
	
	public boolean isPlayerInQueue(Player player) {
		for(GameType game : GameTypeManager.getGames()) {
			if(game.getUnranked().contains(player.getName())) {
				return true;
			}
			
			if(game.getRanked().contains(player.getName())) {
				return true;
			}
			
			if(game.getPremium().contains(player.getName())) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isPartyInQueue(Party party) {
		for(GameType game : GameTypeManager.getGames()) {
			if(game.getPartyUnranked().contains(party)) {
				return true;
			}
			
			if(game.getPartyRanked().contains(party)) {
				return true;
			}
			
			if(game.getPartyPremium().contains(party)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void removeFromQueue(Player player) {
		for(GameType game : GameTypeManager.getGames()) {
			if(game.getUnranked().contains(player.getName())) {
				game.getUnranked().remove(player.getName());
			}
			
			if(game.getRanked().contains(player.getName())) {
				game.getRanked().remove(player.getName());
			}
			
			if(game.getPremium().contains(player.getName())) {
				game.getPremium().remove(player.getName());
			}
		}
	}
}
