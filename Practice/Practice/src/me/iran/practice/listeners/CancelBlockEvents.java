package me.iran.practice.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;

public class CancelBlockEvents implements Listener {

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {

		Player player = event.getPlayer();
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);

		if(profile.getState() != PlayerState.EDIT) {
			event.setCancelled(true);
		}
		
	}
	

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(profile.getState() == PlayerState.LOBBY || profile.getState() == PlayerState.IN_QUEUE || profile.getState() == PlayerState.SPECTATOR) {
				event.setCancelled(true);
		}
		
		if(profile.getState() == PlayerState.IN_EVENT || profile.getState() == PlayerState.EDITOR) {
			event.getItemDrop().remove();
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);

		if(profile.getState() != PlayerState.EDIT) {
			event.setCancelled(true);
		}
	}

}
