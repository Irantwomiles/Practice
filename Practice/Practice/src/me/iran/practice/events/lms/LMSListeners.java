package me.iran.practice.events.lms;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import lombok.Getter;
import me.iran.practice.Practice;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.spectator.Spectator;
import net.md_5.bungee.api.ChatColor;

public class LMSListeners implements Listener {

	@Getter
	private static HashMap<UUID, Integer> winners = new HashMap<>();
	
	private Spectator spec = new Spectator();
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		if(LMSManager.getManager().isPlayerInLMS(player)) {
			LMSManager.getManager().disconnect(player);
		}
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {		
		Player player = event.getEntity();
		
		LMS lms = LMSManager.getManager().getLMSByPlayer(player);
		
		if(lms == null) {
			return;
		}
		
		if(lms.isActive()) {
			LMSManager.getManager().remove(player);
			event.getDrops().clear();
			
			Practice.getInstance().teleportSpawn(player);
		}
		
	}
	
	@EventHandler
	public void lmsEnd(LeaveLMS event) {
		
		LMS lms = event.getLms();
		
		if(lms.getPlayers().size() == 1) {
			if(lms.isActive()) {
			
				Player winner = Bukkit.getPlayer(lms.getPlayers().get(0));
				
				winners.put(winner.getUniqueId(), 10);
				
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("win-lms").replace("%player%", winner.getName())));
				
				LMSManager.getManager().end(lms);
	
				for(int i = 0; i < lms.getArena().getSpectators().size(); i++) {
					
					Player player = Bukkit.getPlayer(lms.getArena().getSpectators().get(i));
					
					if(player == null) continue;
					
					spec.leaveSpectator(player);
					
				}
				
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		
		if(event.getEntity() instanceof Player) {
			
			Player player = (Player) event.getEntity();
			
			Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
			
			if(LMSManager.getManager().isPlayerInLMS(player)) {
				
				LMS lms = LMSManager.getManager().getLMSByPlayer(player);
				
				if(!lms.isActive() && profile.getState() == PlayerState.IN_EVENT) {
					event.setCancelled(true);
				} else if(lms.isActive() && lms.getTimer() > 0) {
					event.setCancelled(true);
				}
			}
		}
	}
}
