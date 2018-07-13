package me.iran.practice.profile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.iran.practice.Practice;

public class ProfileSetup implements Listener {

	/*
	 * load/create profile when they join
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ProfileManager.getManager().createProfile(player);
	}

	/*
	 * Save profile when they quit
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		
		Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Practice.getInstance(), new Runnable() {

			@Override
			public void run() {
				ProfileManager.getManager().saveProfile(player);				
			}
			
		}, 1);
		
	}
}
