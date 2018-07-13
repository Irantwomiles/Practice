package me.iran.practice.duel.listeners;

import me.iran.practice.duel.SoloDuelManager;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItemInDuel implements Listener {

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		
		Player player = event.getPlayer();
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(profile.getState() == PlayerState.IN_GAME) {
			
			//SoloDuel
			
			if(SoloDuelManager.getManager().getDuelByPlayer(player) != null) {
				SoloDuelManager.getManager().getDuelByPlayer(player).getArena().getDrops().add(event.getItemDrop());
			}
			
		}
		
	}
	
}
