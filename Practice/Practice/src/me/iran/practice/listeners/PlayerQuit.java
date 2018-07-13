package me.iran.practice.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.iran.practice.arena.Arena;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.utils.PlayerQueues;

public class PlayerQuit implements Listener {

	private PlayerQueues queue = new PlayerQueues();
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		if(ClickListeners.getGameSelected().containsKey(player.getName())) {
			ClickListeners.getGameSelected().remove(player.getName());
		}
		
		if(InteractListeners.getWait().containsKey(player.getName())) {
			InteractListeners.getWait().remove(player.getName());
		}
		
		for(Arena arena : ArenaManager.getArenas()) {
			if(arena.getSpectators().contains(player.getUniqueId())) {
				arena.getSpectators().remove(player.getUniqueId());
			}
		}
		
		queue.removeFromQueue(player);
	}
	
}
