package me.iran.practice.duel.team.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.iran.practice.Practice;
import me.iran.practice.duel.team.TeamDuel;
import me.iran.practice.duel.team.TeamDuelManager;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.spectator.Spectator;

public class TeamDeathEvent implements Listener {

	private Spectator spec = new Spectator();
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		
		event.setDeathMessage(null);
		
		final Player player = (Player) event.getEntity();
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(event.getEntity().getKiller() instanceof Player) {
			
			final Player killer = (Player) event.getEntity().getKiller();
			
			if(TeamDuelManager.getManager().isPlayerInDuel(killer) && TeamDuelManager.getManager().isPlayerInDuel(player)) {
				
				TeamDuel duel = TeamDuelManager.getManager().getDuelByAlivePlayer(killer);
				
				event.getDrops().clear();
				
				if(TeamDuelManager.getManager().sameDuel(player, duel)) {
					
					if (duel.getTeam1().getAlive().contains(player.getUniqueId())) {
						
						duel.getTeam1().getAlive().remove(player.getUniqueId());
						
						Bukkit.getPluginManager().callEvent(new TeamDeath(duel));
						
						duel.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("team-death-message").replace("%player%", player.getName()).replace("%killer%", killer.getName())));

						if (duel.getTeam1().getAlive().size() <= 0) {
							TeamDuelManager.getManager().endTeamDuel(killer, duel);
						} else {
							
							profile.setState(PlayerState.SPECTATOR);
							
							Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
								public void run() {
									player.spigot().respawn();
									spec.setSpectator(player, killer);
								}
							}, 3);
							
						}

					} else if (duel.getTeam2().getAlive().contains(player.getUniqueId())) {
						
						duel.getTeam2().getAlive().remove(player.getUniqueId());
						
						Bukkit.getPluginManager().callEvent(new TeamDeath(duel));
						
						duel.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("team-death-message").replace("%player%", player.getName()).replace("%killer%", killer.getName())));

						if (duel.getTeam2().getAlive().size() <= 0) {
							TeamDuelManager.getManager().endTeamDuel(killer, duel);
						} else {
							
							profile.setState(PlayerState.SPECTATOR);
							
							Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
								public void run() {
									player.spigot().respawn();
									spec.setSpectator(player, killer);
								}
							}, 3);
						}
						
					}
				}
			}

		} else {
			
			if(TeamDuelManager.getManager().isPlayerInDuel(player)) {
				
				final TeamDuel duel = TeamDuelManager.getManager().getDuelByAlivePlayer(player);
				
				event.getDrops().clear();
				
				if (duel.getTeam1().getAlive().contains(player.getUniqueId())) {
					
					duel.getTeam1().getAlive().remove(player.getUniqueId());
					
					Bukkit.getPluginManager().callEvent(new TeamDeath(duel));
					
					duel.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("team-death-message").replace("%player%", player.getName()).replace("%killer%", "Unknown Reason")));
	
					if (duel.getTeam1().getAlive().size() <= 0) {
						TeamDuelManager.getManager().endTeamDuel(Bukkit.getPlayer(duel.getTeam2().getAlive().get(0)), duel);
					} else {
						profile.setState(PlayerState.SPECTATOR);
						spec.setSpectator(player, Bukkit.getPlayer(duel.getTeam1().getAlive().get(0)));
					}

				} else if (duel.getTeam2().getAlive().contains(player.getUniqueId())) {
					
					duel.getTeam2().getAlive().remove(player.getUniqueId());
					
					Bukkit.getPluginManager().callEvent(new TeamDeath(duel));
					
					duel.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("team-death-message").replace("%player%", player.getName()).replace("%killer%", "Unknown Reason")));

					if (duel.getTeam2().getAlive().size() <= 0) {
						TeamDuelManager.getManager().endTeamDuel(Bukkit.getPlayer(duel.getTeam1().getAlive().get(0)), duel);
					} else {
						profile.setState(PlayerState.SPECTATOR);
						
						//Remove the delayed task and the respawn if it causes problems
						
						Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
							public void run() {
								player.spigot().respawn();
								spec.setSpectator(player, Bukkit.getPlayer(duel.getTeam2().getAlive().get(0)));
							}
						}, 3);
						
					}
					
				}
				
			}
			
		}

	}

}
