package me.iran.practice.duel.team.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import me.iran.practice.Practice;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.kit.KitManager;
import me.iran.practice.listeners.ClickListeners;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import net.md_5.bungee.api.ChatColor;

public class TeamDuelStartEvent implements Listener {

	@EventHandler
	public void onStart(final TeamDuelStart event) {
		
		//Team 1
		for (UUID uuid : event.getDuel().getTeam1().getAlive()) {

			final Player player = Bukkit.getPlayer(uuid);

			Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
			
			profile.setState(PlayerState.IN_GAME);
			
			for (PotionEffect effect : player.getActivePotionEffects()) {
				player.removePotionEffect(effect.getType());
			}

			if(ClickListeners.getGameSelected().containsKey(player.getName())) {
				ClickListeners.getGameSelected().remove(player.getName());
			}
			
			if(ClickListeners.getNaming().contains(player.getName())) {
				ClickListeners.getNaming().remove(player.getName());
			}
			
			player.setHealth(20.0);
			player.setFoodLevel(20);
			
			player.teleport(event.getDuel().getArena().getLoc1());
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
				public void run() {
					KitManager.getManager().getPlayerKits(player, event.getDuel().getGame());
				}
			}, 5);
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("team-duel-start").replace("%arena%", event.getDuel().getArena().getName()).replace("%gametype%", event.getDuel().getGame().getName())));
		}
		
		//Team 2
		for (UUID uuid : event.getDuel().getTeam2().getAlive()) {

			final Player player = Bukkit.getPlayer(uuid);

			Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
			
			profile.setState(PlayerState.IN_GAME);
			
			for (PotionEffect effect : player.getActivePotionEffects()) {
				player.removePotionEffect(effect.getType());
			}

			if(ClickListeners.getGameSelected().containsKey(player.getName())) {
				ClickListeners.getGameSelected().remove(player.getName());
			}
			
			if(ClickListeners.getNaming().contains(player.getName())) {
				ClickListeners.getNaming().remove(player.getName());
			}
			
			player.setHealth(20.0);
			player.setFoodLevel(20);
			
			player.teleport(event.getDuel().getArena().getLoc2());
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
				public void run() {
					KitManager.getManager().getPlayerKits(player, event.getDuel().getGame());
				}
			}, 5);
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("team-duel-start").replace("%arena%", event.getDuel().getArena().getName()).replace("%gametype%", event.getDuel().getGame().getName())));
		}
		
		for (UUID uuid1 : event.getDuel().getTeam1().getAlive()) {

			final Player player1 = Bukkit.getPlayer(uuid1);

			for (UUID uuid2 : event.getDuel().getTeam2().getAlive()) {

				final Player player2 = Bukkit.getPlayer(uuid2);

				player1.showPlayer(player2);
				player2.showPlayer(player1);
				
			}
			
		}
	}
	
}
