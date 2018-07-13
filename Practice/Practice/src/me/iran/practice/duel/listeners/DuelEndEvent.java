package me.iran.practice.duel.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;

import me.iran.practice.Practice;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.arena.listeners.ArenaAvailable;
import me.iran.practice.duel.SoloDuel;
import me.iran.practice.duel.SoloDuelManager;
import me.iran.practice.enums.DuelType;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.spectator.Spectator;
import me.iran.practice.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class DuelEndEvent implements Listener {

	private Spectator spec = new Spectator();
	
	private Utils utils = new Utils();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEnd(final DuelEnd event) {
		
		final SoloDuel duel = event.getDuel();
		
		final Profile player1Profile = ProfileManager.getManager().getProfileByPlayer(duel.getWinner());
		final Profile player2Profile = ProfileManager.getManager().getProfileByPlayer(duel.getLoser());
		
		player2Profile.setState(PlayerState.SPECTATOR);
		
		duel.getWinner().setGameMode(GameMode.CREATIVE);
		
		spec.setSpectator(duel.getLoser(), duel.getWinner());
		
		if(duel.getType() == DuelType.SOLO_UNRANKED) {
			
			for(String msg : Practice.getInstance().getConfig().getStringList("unranked-duel-end")) {
				duel.getWinner().sendMessage(ChatColor.translateAlternateColorCodes('&', msg
						.replace("%gametype%", duel.getGame().getName())
						.replace("%arena%", duel.getArena().getName())
						.replace("%duration%", duel.getDuration() + "")
						.replace("%loser%", duel.getLoser().getName())
						.replace("%winner%", duel.getWinner().getName())));

				
			}
			
			for(UUID spec : event.getDuel().getArena().getSpectators()) {
				
				Player p = Bukkit.getPlayer(spec);
				
				for(String msg : Practice.getInstance().getConfig().getStringList("unranked-duel-end")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
							.replace("%gametype%", duel.getGame().getName())
							.replace("%arena%", duel.getArena().getName())
							.replace("%duration%", duel.getDuration() + "")
							.replace("%loser%", duel.getLoser().getName())
							.replace("%winner%", duel.getWinner().getName())));
				}
			}
			
			player1Profile.setUnrankedPlayed(player1Profile.getUnrankedPlayed() + 1);
			player2Profile.setUnrankedPlayed(player2Profile.getUnrankedPlayed() + 1);
			
			player1Profile.setUnrankedWon(player1Profile.getUnrankedWon() + 1);
			
		} else if(duel.getType() == DuelType.SOLO_RANKED) {
			
			double calc = 1.0 / (1.0 + Math.pow(10.0, (ProfileManager.getManager().getPlayerElo(duel.getLoser(), duel.getGame().getName()) - ProfileManager.getManager().getPlayerElo(duel.getWinner(), duel.getGame().getName())) / 400.0));
			int elo = (int) Math.round(5 * (3 - calc));
			
			player1Profile.setPlayerElo(duel.getWinner(), duel.getGame().getName(), player1Profile.getPlayerElo(duel.getWinner(), duel.getGame().getName()) + elo);
			player2Profile.setPlayerElo(duel.getLoser(), duel.getGame().getName(), player2Profile.getPlayerElo(duel.getLoser(), duel.getGame().getName()) - elo);
			
			utils.randomPremium(duel.getWinner(), player1Profile);
			
			for(String msg : Practice.getInstance().getConfig().getStringList("ranked-duel-end")) {
				duel.getWinner().sendMessage(ChatColor.translateAlternateColorCodes('&', msg
						.replace("%gametype%", duel.getGame().getName())
						.replace("%winner-elo%", player1Profile.getPlayerElo(duel.getWinner(), duel.getGame().getName()) + "")
						.replace("%loser-elo%", player2Profile.getPlayerElo(duel.getLoser(), duel.getGame().getName()) + "")
						.replace("%arena%", duel.getArena().getName())
						.replace("%duration%", duel.getDuration() + "")
						.replace("%loser%", duel.getLoser().getName())
						.replace("%elo%", elo + "")
						.replace("%winner%", duel.getWinner().getName())));
			}
			
			for(UUID spec : event.getDuel().getArena().getSpectators()) {
				
				Player p = Bukkit.getPlayer(spec);
				
				for(String msg : Practice.getInstance().getConfig().getStringList("unranked-duel-end")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
							.replace("%gametype%", duel.getGame().getName())
							.replace("%winner-elo%", player1Profile.getPlayerElo(duel.getWinner(), duel.getGame().getName()) + "")
							.replace("%loser-elo%", player2Profile.getPlayerElo(duel.getLoser(), duel.getGame().getName()) + "")
							.replace("%arena%", duel.getArena().getName())
							.replace("%duration%", duel.getDuration() + "")
							.replace("%loser%", duel.getLoser().getName())
							.replace("%elo%", elo + "")
							.replace("%winner%", duel.getWinner().getName())));
				}
			}
			
			player1Profile.setRankedPlayed(player1Profile.getRankedPlayed() + 1);
			player2Profile.setRankedPlayed(player2Profile.getRankedPlayed() + 1);
			
			player1Profile.setRankedWon(player1Profile.getRankedWon() + 1);
			
		} else if(duel.getType() == DuelType.PREMIUM) {
			
			utils.randomToken(duel.getWinner(), player1Profile);
			
			for(String msg : Practice.getInstance().getConfig().getStringList("unranked-duel-end")) {
				duel.getWinner().sendMessage(ChatColor.translateAlternateColorCodes('&', msg
						.replace("%gametype%", duel.getGame().getName())
						.replace("%arena%", duel.getArena().getName())
						.replace("%duration%", duel.getDuration() + "")
						.replace("%loser%", duel.getLoser().getName())
						.replace("%winner%", duel.getWinner().getName())));
			}
			
			for(UUID spec : event.getDuel().getArena().getSpectators()) {
				
				Player p = Bukkit.getPlayer(spec);
				
				for(String msg : Practice.getInstance().getConfig().getStringList("unranked-duel-end")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
							.replace("%gametype%", duel.getGame().getName())
							.replace("%arena%", duel.getArena().getName())
							.replace("%duration%", duel.getDuration() + "")
							.replace("%loser%", duel.getLoser().getName())
							.replace("%winner%", duel.getWinner().getName())));
				}
			}
			
			player1Profile.setPremiumPlayed(player1Profile.getPremiumPlayed() + 1);
			player2Profile.setPremiumPlayed(player2Profile.getPremiumPlayed() + 1);
			
			player1Profile.setPremiumWon(player1Profile.getPremiumWon() + 1);
			
		}

		TextComponent win = new TextComponent("Winner: ");
		win.setColor(ChatColor.YELLOW);
		
		TextComponent lose = new TextComponent("Loser: ");
		lose.setColor(ChatColor.YELLOW);
		
		TextComponent winner = new TextComponent(duel.getWinner().getName());
		winner.setColor(ChatColor.GREEN);
		
		TextComponent loser = new TextComponent(duel.getLoser().getName());
		loser.setColor(ChatColor.RED);
		
		TextComponent clickWinner = new TextComponent(" - (Click to view their Inventory)");
		clickWinner.setColor(ChatColor.GRAY);
		clickWinner.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/_ " + duel.getWinner().getName()));
		
		TextComponent clickLoser = new TextComponent(" - (Click to view their Inventory)");
		clickLoser.setColor(ChatColor.GRAY);
		clickLoser.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/_ " + duel.getLoser().getName()));
		
		String line = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------------------------";
		
		win.addExtra(winner);
		win.addExtra(clickWinner);
		
		lose.addExtra(loser);
		lose.addExtra(clickLoser);
		
		duel.getWinner().spigot().sendMessage(win);
		duel.getWinner().spigot().sendMessage(lose);
		duel.getWinner().sendMessage(line);
		
	/*	duel.getLoser().spigot().sendMessage(win);
		duel.getLoser().spigot().sendMessage(lose);
		duel.getLoser().sendMessage(line);*/
		
		for(UUID spec : event.getDuel().getArena().getSpectators()) {
			
			Player p = Bukkit.getPlayer(spec);
			
			p.spigot().sendMessage(win);
			p.spigot().sendMessage(lose);
			p.sendMessage(line);
		}
		
		duel.getGame().setInGame(duel.getGame().getInGame() - 2);
		
		//Set max health
		duel.getWinner().setHealth(20.0);
		duel.getLoser().setHealth(20.0);
		
		//Set max hunger
		duel.getWinner().setFoodLevel(20);
		duel.getLoser().setFoodLevel(20);
		
		/*
		 * Clear potion effects
		 */
		for(PotionEffect effect : duel.getWinner().getActivePotionEffects()) {
			duel.getWinner().removePotionEffect(effect.getType());
		}
		
		for(PotionEffect effect : duel.getLoser().getActivePotionEffects()) {
			duel.getLoser().removePotionEffect(effect.getType());
		}
		
		/*
		 * Clear inv and armor
		 */
		
		duel.getWinner().getInventory().clear();
		duel.getWinner().getInventory().setArmorContents(null);
		
		duel.getLoser().getInventory().clear();
		duel.getLoser().getInventory().setArmorContents(null);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
				
				for(Player p : Bukkit.getServer().getOnlinePlayers()) {
					if(event.getDuel().getArena().getSpectators().contains(p.getUniqueId())) {
						
						spec.leaveSpectator(p);
						
						duel.getWinner().showPlayer(p);
						duel.getLoser().showPlayer(p);
						
						p.showPlayer(duel.getWinner());
						p.showPlayer(duel.getLoser());
					}
				}
				
				for(Item item : duel.getArena().getDrops()) {
					item.remove();
				}
				
				if(player1Profile != null)
					player1Profile.setState(PlayerState.LOBBY);
				
				if(player2Profile != null)
					player2Profile.setState(PlayerState.LOBBY);
				
				ArenaManager.getAvailableArenas().add(duel.getArena());
				
				Bukkit.getServer().getPluginManager().callEvent(new ArenaAvailable(duel.getArena()));
				
				SoloDuelManager.getDuelSet().remove(duel);

				Bukkit.getServer().getPluginManager().callEvent(new DuelRemoved(duel));
				
				Practice.getInstance().teleportSpawn(duel.getPlayer1());
				Practice.getInstance().teleportSpawn(duel.getPlayer2());
				
			}
			
		}, 60);
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		
		if(event.getWhoClicked() instanceof Player) {
		
			Player player = (Player) event.getWhoClicked();
			
			if(SoloDuelManager.getManager().isPlayerInDuel(player)) {
				
				if(player.getGameMode() == GameMode.CREATIVE) {
					event.setCancelled(true);
				}
				
			}
		
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {

		Player player = event.getPlayer();

		if (SoloDuelManager.getManager().isPlayerInDuel(player)) {

			if (player.getGameMode() == GameMode.CREATIVE) {
				event.setCancelled(true);
			}

		}

	}
}
