package me.iran.practice.runnable;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.iran.practice.Practice;
import me.iran.practice.events.lms.LMS;
import me.iran.practice.events.lms.LMSManager;
import me.iran.practice.gametype.GameType;
import me.iran.practice.gametype.GameTypeManager;

public class InventoryRunnables extends BukkitRunnable {
	
	@SuppressWarnings("deprecation")
	public void run() {
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			
			//Ranked Inventory
			if(player.getOpenInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.ranked")))) {
				for(int i = 0; i < GameTypeManager.getGames().size(); i++) {
					
					GameType game = GameTypeManager.getGames().get(i);
					
					ItemStack item = game.getDisplay();
					ItemMeta dmeta = item.getItemMeta();
					
					dmeta.setDisplayName(ChatColor.GREEN + game.getName());
					dmeta.setLore(Arrays.asList("", ChatColor.GRAY + "In Game: " + ChatColor.YELLOW + game.getInGame(),
							ChatColor.GRAY + "In Queue: " + ChatColor.YELLOW + game.getRanked().size()));
				
					item.setItemMeta(dmeta);
					player.getOpenInventory().setItem(i, item);
				}
			}
			
			//Unranked Inventory
			if(player.getOpenInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.unranked")))) {
				
				for(int i = 0; i < GameTypeManager.getGames().size(); i++) {
					
					GameType game = GameTypeManager.getGames().get(i);
					
					ItemStack item = game.getDisplay();
					ItemMeta dmeta = item.getItemMeta();
					
					dmeta.setDisplayName(ChatColor.GREEN + game.getName());
					dmeta.setLore(Arrays.asList("", ChatColor.GRAY + "In Game: " + ChatColor.YELLOW + game.getInGame(),
							ChatColor.GRAY + "In Queue: " + ChatColor.YELLOW + game.getUnranked().size()));
				
					item.setItemMeta(dmeta);
					player.getOpenInventory().setItem(i, item);
					
				}
			}
			
			//Premium Inventory
			if(player.getOpenInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.premium")))) {
				for(int i = 0; i < GameTypeManager.getGames().size(); i++) {
					
					GameType game = GameTypeManager.getGames().get(i);
					
					ItemStack item = game.getDisplay();
					ItemMeta dmeta = item.getItemMeta();
					
					dmeta.setDisplayName(ChatColor.GREEN + game.getName());
					dmeta.setLore(Arrays.asList("", ChatColor.GRAY + "In Game: " + ChatColor.YELLOW + game.getInGame(),
							ChatColor.GRAY + "In Queue: " + ChatColor.YELLOW + game.getPremium().size()));
				
					item.setItemMeta(dmeta);
					player.getOpenInventory().setItem(i, item);
					
				}
			}
		
			//LMS Inventory
			if(player.getOpenInventory().getTitle().equals(ChatColor.BLUE + "LMS Events")){
				for(int i = 0; i < LMSManager.getLmsList().size(); i++) {
					
					LMS lms = LMSManager.getLmsList().get(i);
					
					if(lms.getTimer() > 0) {
						
						ItemStack item = new ItemStack(Material.QUARTZ, lms.getPlayers().size());
					
						ItemMeta meta = item.getItemMeta();
						
						meta.setDisplayName(ChatColor.GREEN.toString() + lms.getId());
						meta.setLore(Arrays.asList("", ChatColor.GRAY + "GameType: " + ChatColor.YELLOW + lms.getGame().getName(), ChatColor.GRAY + "Players: " + lms.getPlayers().size(), ChatColor.GRAY + "Status: " + ChatColor.GREEN + lms.isActive()));
						
						item.setItemMeta(meta);
						
						player.getOpenInventory().setItem(i, item);
					
					} else {
						
						ItemStack item = new ItemStack(Material.REDSTONE);
						
						ItemMeta meta = item.getItemMeta();
						
						meta.setDisplayName(ChatColor.RED.toString() + "Started");
						meta.setLore(Arrays.asList("", ChatColor.GRAY + "GameType: " + ChatColor.YELLOW + lms.getGame().getName(), ChatColor.GRAY + "Players: " + lms.getPlayers().size(), ChatColor.GRAY + "Status: " + ChatColor.GREEN + lms.isActive()));
						item.setItemMeta(meta);
						
						player.getOpenInventory().setItem(i, item);
					}
				}
			}
		}
		
	}

}
