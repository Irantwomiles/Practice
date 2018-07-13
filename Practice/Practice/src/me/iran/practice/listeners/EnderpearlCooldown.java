package me.iran.practice.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.iran.practice.duel.SoloDuelManager;
import me.iran.practice.duel.team.TeamDuelManager;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import net.md_5.bungee.api.ChatColor;

public class EnderpearlCooldown implements Listener {

	HashMap<UUID, Long> cooldown = new HashMap<>();
	
	@EventHandler
	public void onThrow(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();

		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if (event.getAction() == null) {
			return;
		}

		if (player.getItemInHand() == null) {
			return;
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if(player.getItemInHand().getType() == Material.ENDER_PEARL) {
				
				if(profile.getState() == PlayerState.IN_EVENT || profile.getState() == PlayerState.IN_GAME) {
					
					if(SoloDuelManager.getManager().isPlayerInDuel(player)) {
						
						if(SoloDuelManager.getManager().getDuelByPlayer(player).getDuration() <= 5) {
							event.setCancelled(true);
							player.sendMessage(ChatColor.RED + "The match has not started yet!");
							return;
						}
						
					}
					
					if(TeamDuelManager.getManager().isPlayerAliveInDuel(player)) {
						
						if(TeamDuelManager.getManager().getDuelByPlayer(player).getDuration() <= 5) {
							event.setCancelled(true);
							player.sendMessage(ChatColor.RED + "The match has not started yet!");
							return;
						}
						
					}
					
					if(cooldown.containsKey(player.getUniqueId()) && cooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
						event.setCancelled(true);
						
						int time = (int) ((cooldown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000);
						
						player.sendMessage(ChatColor.YELLOW + "You are on a cooldown for another " + ChatColor.GRAY + time + ChatColor.YELLOW + " seconds!");
						
					} else {
						cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (16 * 1000));
					}
				} else {
					event.setCancelled(true);
				}
				
			}
			
		}
		
	}

	@EventHandler
	public void onLand(PlayerTeleportEvent event) {
		
		Player player = event.getPlayer();

		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(event.getCause() == TeleportCause.ENDER_PEARL) {
			
			if(profile.getState() == PlayerState.LOBBY || profile.getState() == PlayerState.IN_QUEUE || profile.getState() == PlayerState.EDITOR) {
				event.setCancelled(true);
			}
			
		}
		
	}
	
}
