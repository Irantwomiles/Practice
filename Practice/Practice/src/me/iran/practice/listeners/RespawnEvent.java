package me.iran.practice.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

import me.iran.practice.Practice;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;

public class RespawnEvent implements Listener {

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(event.getPlayer());
		
		if(profile.getState() == PlayerState.LOBBY || profile.getState() == PlayerState.EDITOR) {
			Practice.getInstance().teleportSpawn(event.getPlayer());
		}
		
		for(PotionEffect effect : event.getPlayer().getActivePotionEffects()) {
			event.getPlayer().removePotionEffect(effect.getType());
		}
		
		event.getPlayer().setFireTicks(0);
		
	}
	
}
