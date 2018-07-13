package me.iran.practice.duel.team.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.iran.practice.duel.team.TeamDuelManager;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;

public class DropItemInTeamDuel implements Listener {

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		
		Player player = event.getPlayer();
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(profile.getState() == PlayerState.IN_GAME) {
			
			if(TeamDuelManager.getManager().isPlayerInDuel(player)) {
				TeamDuelManager.getManager().getDuelByPlayer(player).getArena().getDrops().add(event.getItemDrop());
			}
			
		}
		
	}
	
}
