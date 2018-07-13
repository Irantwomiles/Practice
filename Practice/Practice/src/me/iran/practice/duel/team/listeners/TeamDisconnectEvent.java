package me.iran.practice.duel.team.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.iran.practice.duel.team.TeamDuel;
import me.iran.practice.duel.team.TeamDuelManager;
import me.iran.practice.party.PartyManager;
import net.md_5.bungee.api.ChatColor;

public class TeamDisconnectEvent implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		if(TeamDuelManager.getManager().isPlayerInDuel(player)) {
			
			TeamDuel duel = TeamDuelManager.getManager().getDuelByPlayer(player);
			
			if(duel.getTeam1().getAlive().contains(player.getUniqueId())) {
				duel.getTeam1().getAlive().remove(player.getUniqueId());
				
				TeamDuelManager.getManager().sendPlayersMessage(duel, ChatColor.RED + player.getName() + ChatColor.YELLOW + " has disconnected!");
				
				if(PartyManager.getManager().isPlayerInParty(player.getUniqueId())) {
					PartyManager.getManager().leave(player.getUniqueId());
				}
				
				if(duel.getTeam1().getAlive().size() <= 0) {
					TeamDuelManager.getManager().endTeamDuel(Bukkit.getPlayer(duel.getTeam2().getAlive().get(0)), duel);
				}
				
			} else if(duel.getTeam2().getAlive().contains(player.getUniqueId())) {
				duel.getTeam2().getAlive().remove(player.getUniqueId());
				
				TeamDuelManager.getManager().sendPlayersMessage(duel, ChatColor.RED + player.getName() + ChatColor.YELLOW + " has disconnected!");
				
				if(PartyManager.getManager().isPlayerInParty(player.getUniqueId())) {
					PartyManager.getManager().leave(player.getUniqueId());
				}
				
				if(duel.getTeam2().getAlive().size() <= 0) {
					TeamDuelManager.getManager().endTeamDuel(Bukkit.getPlayer(duel.getTeam1().getAlive().get(0)), duel);
				}
				
			}
			
			if(duel.getTeam1().getPlayers().contains(player.getUniqueId())) {
				duel.getTeam1().getPlayers().remove(player.getUniqueId());
			} else if(duel.getTeam2().getPlayers().contains(player.getUniqueId())) {
				duel.getTeam2().getPlayers().remove(player.getUniqueId());
			}
			
		}
	}
}
