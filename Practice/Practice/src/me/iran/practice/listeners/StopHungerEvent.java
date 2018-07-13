package me.iran.practice.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import me.iran.practice.enums.PlayerState;
import me.iran.practice.events.lms.LMS;
import me.iran.practice.events.lms.LMSManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;

public class StopHungerEvent implements Listener {
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent event) {
		
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
			
			if(profile == null) {
				return;
			}
			
			if(profile.getState() == PlayerState.LOBBY || profile.getState() == PlayerState.IN_QUEUE || profile.getState() == PlayerState.EDITOR) {
				player.setFoodLevel(20);
				event.setCancelled(true);
			}
			
			if(profile.getState() == PlayerState.IN_EVENT) {
				if(LMSManager.getManager().isPlayerInLMS(player)) {
					
					LMS lms = LMSManager.getManager().getLMSByPlayer(player);
					
					if(!lms.isActive()) {
						player.setFoodLevel(20);
						event.setCancelled(true);
					}
					
				}
			}
			
		}
		
	}

}
