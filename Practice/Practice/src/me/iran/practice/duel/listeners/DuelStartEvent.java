package me.iran.practice.duel.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import me.iran.practice.Practice;
import me.iran.practice.duel.SoloDuel;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.kit.KitManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;

public class DuelStartEvent implements Listener {
	
	@EventHandler
	public void onStart(DuelStart event) {
		
		final SoloDuel duel = event.getDuel();
		
		Profile profile1 = ProfileManager.getManager().getProfileByPlayer(duel.getPlayer1());
		Profile profile2 = ProfileManager.getManager().getProfileByPlayer(duel.getPlayer2());
		
		profile1.setState(PlayerState.IN_GAME);
		profile2.setState(PlayerState.IN_GAME);
		
		duel.getGame().setInGame(duel.getGame().getInGame() + 2);
		
		for(PotionEffect effect : duel.getPlayer1().getActivePotionEffects())
		{
			duel.getPlayer1().removePotionEffect(effect.getType());
		}
		
		for(PotionEffect effect : duel.getPlayer2().getActivePotionEffects())
		{
			duel.getPlayer2().removePotionEffect(effect.getType());
		}
		
		duel.getPlayer1().setHealth(20.0);
		duel.getPlayer2().setHealth(20.0);
		
		duel.getPlayer1().setFoodLevel(20);
		duel.getPlayer2().setFoodLevel(20);
		
		duel.getPlayer1().teleport(duel.getArena().getLoc1());
		duel.getPlayer2().teleport(duel.getArena().getLoc2());
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			public void run() {
				KitManager.getManager().getPlayerKits(duel.getPlayer1(), duel.getGame());
				KitManager.getManager().getPlayerKits(duel.getPlayer2(), duel.getGame());
			}
		}, 5);
		
		duel.getPlayer1().sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("duel.unranked-start-message").replace("%gametype%", duel.getGame().getName()).replace("%player%", duel.getPlayer2().getName())));
		duel.getPlayer2().sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("duel.unranked-start-message").replace("%gametype%", duel.getGame().getName()).replace("%player%", duel.getPlayer1().getName())));
		
		
	}

}
