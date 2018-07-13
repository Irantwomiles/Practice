package me.iran.practice.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.iran.practice.Practice;
import me.iran.practice.utils.PlayerItems;

public class TeleportSpawnOnJoin implements Listener {

	private PlayerItems items = new PlayerItems();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		event.setJoinMessage(null);
		
		Player player = event.getPlayer();
		
		Practice.getInstance().teleportSpawn(player);
		
		items.spawnItems(player);

	}
}
