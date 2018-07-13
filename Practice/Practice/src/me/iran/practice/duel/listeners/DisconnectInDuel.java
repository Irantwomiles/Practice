package me.iran.practice.duel.listeners;

import me.iran.practice.duel.SoloDuel;
import me.iran.practice.duel.SoloDuelManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectInDuel implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		
		event.setQuitMessage(null);
		
		Player player = event.getPlayer();
		
		if(SoloDuelManager.getManager().getDuelByPlayer(player) == null) {
			return;
		}
		
		SoloDuel duel = SoloDuelManager.getManager().getDuelByPlayer(player);
		
		if(duel.getPlayer1().getUniqueId() == player.getUniqueId()) {
			SoloDuelManager.getManager().endSoloDuel(duel.getPlayer2(), duel);
		} else {
			SoloDuelManager.getManager().endSoloDuel(duel.getPlayer1(), duel);
		}
		
	}

}
