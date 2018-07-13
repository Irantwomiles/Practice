package me.iran.practice.duel.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.iran.practice.Practice;
import me.iran.practice.duel.SoloDuel;
import me.iran.practice.duel.SoloDuelManager;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;

public class DuelDeathEvent implements Listener {
	
	@EventHandler
	public void soloDeath(PlayerDeathEvent event) {
		
		final Player player = event.getEntity();
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(profile.getState() == PlayerState.LOBBY) {
			event.getDrops().clear();
		}
		
		final SoloDuel duel = SoloDuelManager.getManager().getDuelByPlayer(player);
		
		if(duel == null) {
			return;
		}
		
		//If player was killed by a player during a fight
		
		event.getDrops().clear();
		
		if(event.getEntity().getKiller() instanceof Player) {
			
			Player killer = event.getEntity().getKiller();
			
			if(duel.getPlayer1().getUniqueId() == killer.getUniqueId() || duel.getPlayer2().getUniqueId() == killer.getUniqueId()) {

				SoloDuelManager.getManager().endSoloDuel(killer, duel);
				
 			}
			
		} else {
			
			if(duel.getPlayer1().getUniqueId() == player.getUniqueId()) {
				SoloDuelManager.getManager().endSoloDuel(duel.getPlayer2(), duel);
			}
			
			if(duel.getPlayer2().getUniqueId() == player.getUniqueId()) {
				SoloDuelManager.getManager().endSoloDuel(duel.getPlayer1(), duel);
			}
			
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			public void run() {
				player.spigot().respawn();
				player.teleport(duel.getWinner().getLocation());
				duel.getWinner().setFireTicks(0);
				duel.getLoser().setFireTicks(0);
			}
		}, 5);
		
	}

}
