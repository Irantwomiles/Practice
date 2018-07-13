package me.iran.practice.duel.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.iran.practice.duel.SoloDuelManager;
import net.md_5.bungee.api.ChatColor;

public class DuelDamageEvent implements Listener {

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		
		if(event.getDamager() instanceof Player) {
			
			Player damager = (Player) event.getDamager();
			
			if(SoloDuelManager.getManager().isPlayerInDuel(damager)) {
				
				if(SoloDuelManager.getManager().getDuelByPlayer(damager).getDuration() <= 5) {
					damager.sendMessage(ChatColor.YELLOW + "The match has not started yet!");
					event.setCancelled(true);
				}
				
			}
			
		}
		
	}
	
}
