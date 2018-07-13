package me.iran.practice.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.iran.practice.duel.team.TeamDuel;
import me.iran.practice.duel.team.TeamDuelManager;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.events.lms.LMS;
import me.iran.practice.events.lms.LMSManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import net.md_5.bungee.api.ChatColor;

public class DamageListener implements Listener {

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		
		if(event.getDamager() instanceof Player) {
			
			Player player = (Player) event.getDamager();
			
			Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
			
			if(profile == null) {
				return;
			}
			
			if(profile.getState() == PlayerState.LOBBY || profile.getState() == PlayerState.IN_QUEUE || profile.getState() == PlayerState.SPECTATOR || profile.getState() == PlayerState.EDITOR) {
				event.setCancelled(true);
			}
			
			if(event.getEntity() instanceof Player) {
				
				Player hit = (Player) event.getEntity();
				
				if(TeamDuelManager.getManager().isPlayerAliveInDuel(player)) {
					
					TeamDuel duel = TeamDuelManager.getManager().getDuelByAlivePlayer(player);
					
					if(duel.getTeam1().getAlive().contains(player.getUniqueId()) && duel.getTeam1().getAlive().contains(hit.getUniqueId())) {
						event.setCancelled(true);
					} else if(duel.getTeam2().getAlive().contains(player.getUniqueId()) && duel.getTeam2().getAlive().contains(hit.getUniqueId())) {
						event.setCancelled(true);
					} else if(duel.getDuration() <= 5) {
						player.sendMessage(ChatColor.YELLOW + "The match has not started yet!");
						event.setCancelled(true);
					}
				}
			}
			
		}
	}
	
	@EventHandler
	public void onDamaged(EntityDamageEvent event) {
		
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			
			Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
			
			if(profile == null) {
				return;
			}
			
			if(profile.getState() == PlayerState.LOBBY || profile.getState() == PlayerState.IN_QUEUE || profile.getState() == PlayerState.SPECTATOR || profile.getState() == PlayerState.EDITOR) {
				event.setCancelled(true);
			}
			
			if(profile.getState() == PlayerState.IN_EVENT) {
				if(LMSManager.getManager().isPlayerInLMS(player)) {
					
					LMS lms = LMSManager.getManager().getLMSByPlayer(player);
					
					if(!lms.isActive()) {
						event.setCancelled(true);
					}
				}
			}
		}
		
	}
}
