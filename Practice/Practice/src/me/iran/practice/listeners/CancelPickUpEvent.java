package me.iran.practice.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;

public class CancelPickUpEvent implements Listener {

	@EventHandler
	public void onPickUp(PlayerPickupItemEvent event) {
		
		Player player = event.getPlayer();
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(profile.getState() == PlayerState.EDITOR || profile.getState() == PlayerState.LOBBY || profile.getState() == PlayerState.IN_QUEUE || profile.getState() == PlayerState.SPECTATOR) {
			if(!player.hasPermission("lobby.admin")) {
				event.setCancelled(true);
			}
		}
		
	}
	
}
