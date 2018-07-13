package me.iran.practice.utils;

import java.util.Arrays;

import me.iran.practice.Practice;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerItems {

	public void arenaEditing(final Player player) {
		
		final ItemStack diamond = new ItemStack(Material.DIAMOND_BLOCK);
		final ItemStack gold = new ItemStack(Material.GOLD_BLOCK);
		final ItemStack compass = new ItemStack(Material.COMPASS);
		
		ItemMeta dmeta = diamond.getItemMeta();
		ItemMeta gmeta = gold.getItemMeta();
		
		dmeta.setDisplayName(ChatColor.GOLD + "Set Locations");
		dmeta.setLore(Arrays.asList(ChatColor.GRAY + "� Right click in the direction that you want the player to be facing for player 1", ChatColor.GRAY + "� Left click in the direction that you want the player to be facing for player 2"));
		diamond.setItemMeta(dmeta);
		
		gmeta.setDisplayName(ChatColor.YELLOW + "Leave Edit Mode");
		gmeta.setLore(Arrays.asList(ChatColor.GRAY + "� Right click to leave editor mode"));
		gold.setItemMeta(gmeta);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
			
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				
				player.getInventory().setItem(1, compass);
				player.getInventory().setItem(4, diamond);
				player.getInventory().setItem(7, gold);
				
				player.updateInventory();
				
			}
			
		}, 2);
	}
	
	public void spawnItems(final Player player) {
		
		final ItemStack diamond = new ItemStack(Material.DIAMOND_SWORD);
		final ItemStack emerald = new ItemStack(Material.EMERALD);
		final ItemStack party = new ItemStack(Material.NAME_TAG);
		final ItemStack edit = new ItemStack(Material.BOOK);
		final ItemStack options = new ItemStack(Material.SKULL_ITEM, 1);
		
		ItemMeta rmeta = diamond.getItemMeta();
		ItemMeta emMeta = emerald.getItemMeta();
		ItemMeta partyMeta = party.getItemMeta();
		ItemMeta emeta = edit.getItemMeta();
		ItemMeta ometa = options.getItemMeta();
		
		rmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.queue.name")));
		emMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.events.name")));
		partyMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.party.name")));
		emeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.kit-editor.name")));
		ometa.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.options.name")));
		
		diamond.setItemMeta(rmeta);
		emerald.setItemMeta(emMeta);
		party.setItemMeta(partyMeta);
		edit.setItemMeta(emeta);
		options.setItemMeta(ometa);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
				
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("items.queue.position"), diamond);
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("items.events.position"), emerald);
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("items.party.position"), party);
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("items.kit-editor.position"), edit);
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("items.options.position"), options);
			
				player.updateInventory();
			
			}
			
		}, 2);
		
	}

	public void unrankedQueueItems(final Player player) {
		
		final ItemStack redstone = new ItemStack(Material.REDSTONE);
		ItemMeta rmeta = redstone.getItemMeta();
		
		rmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.unranked-queue.name")));
		redstone.setItemMeta(rmeta);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
				
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("items.unranked-queue.position"), redstone);
			
				player.updateInventory();
				
			}
			
		}, 2);
	}
	
	public void unrankedPartyQueueItems(final Player player) {
		
		final ItemStack redstone = new ItemStack(Material.REDSTONE);
		ItemMeta rmeta = redstone.getItemMeta();
		
		rmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.party-unranked-queue.name")));
		redstone.setItemMeta(rmeta);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
				
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("items.party-unranked-queue.position"), redstone);
			
				player.updateInventory();
				
			}
			
		}, 2);
	}
	
	public void rankedQueueItems(final Player player) {
		
		final ItemStack redstone = new ItemStack(Material.REDSTONE);
		ItemMeta rmeta = redstone.getItemMeta();
		
		rmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.ranked-queue.name")));
		redstone.setItemMeta(rmeta);
	
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
			
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("items.ranked-queue.position"), redstone);
		
				player.updateInventory();
				
			}
			
		}, 2);
		
	}
	
	public void premiumQueueItems(final Player player) {
		
		final ItemStack redstone = new ItemStack(Material.REDSTONE);
		ItemMeta rmeta = redstone.getItemMeta();
		
		rmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.premium-queue.name")));
		redstone.setItemMeta(rmeta);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
			
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("items.premium-queue.position"), redstone);
		
				player.updateInventory();
				
			}
			
		}, 2);
		
	}

	public void partyItemsMember(final Player player) {
		
		final ItemStack queue = new ItemStack(Material.DIAMOND_SWORD);
		final ItemStack team = new ItemStack(Material.GOLD_SWORD);
		final ItemStack leave = new ItemStack(Material.REDSTONE);
		final ItemStack info = new ItemStack(Material.PAPER);
		final ItemStack edit = new ItemStack(Material.BOOK);
		
		ItemMeta qmeta = queue.getItemMeta();
		ItemMeta tmeta = team.getItemMeta();
		ItemMeta dmeta = leave.getItemMeta();
		ItemMeta imeta = info.getItemMeta();
		ItemMeta emeta = edit.getItemMeta();
		
		qmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("party-items.queue.name")));
		tmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("party-items.party-events.name")));
		dmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("party-items.leave.name")));
		imeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("party-items.info.name")));
		emeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.kit-editor.name")));
	
		queue.setItemMeta(qmeta);
		team.setItemMeta(tmeta);
		leave.setItemMeta(dmeta);
		info.setItemMeta(imeta);
		edit.setItemMeta(emeta);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
				
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("party-items.queue.position"), queue);
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("party-items.party-events.position"), team);
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("party-items.leave.position"), leave);
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("party-items.info.position"), info);
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("party-items.kit-editor.position"), edit);
			
				player.updateInventory();
			
			}
			
		}, 2);
		
	}
	
	public void partyItemsLeader(final Player player) {
		
		final ItemStack queue = new ItemStack(Material.DIAMOND_SWORD);
		final ItemStack team = new ItemStack(Material.GOLD_SWORD);
		final ItemStack leave = new ItemStack(Material.REDSTONE);
		final ItemStack info = new ItemStack(Material.PAPER);
		final ItemStack edit = new ItemStack(Material.BOOK);
		
		ItemMeta qmeta = queue.getItemMeta();
		ItemMeta tmeta = team.getItemMeta();
		ItemMeta dmeta = leave.getItemMeta();
		ItemMeta imeta = info.getItemMeta();
		ItemMeta emeta = edit.getItemMeta();
		
		qmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("party-items.queue.name")));
		tmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("party-items.party-events.name")));
		dmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("party-items.leave.name")));
		imeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("party-items.info.name")));
		emeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.kit-editor.name")));
	
		queue.setItemMeta(qmeta);
		team.setItemMeta(tmeta);
		leave.setItemMeta(dmeta);
		info.setItemMeta(imeta);
		edit.setItemMeta(emeta);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
				
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("party-items.queue.position"), queue);
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("party-items.party-events.position"), team);
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("party-items.leave.position"), leave);
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("party-items.info.position"), info);
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("party-items.kit-editor.position"), edit);
			
				player.updateInventory();
			
			}
			
		}, 2);
		
	}

	public void spectatorItems(final Player player) {
		
		final ItemStack redstone = new ItemStack(Material.GHAST_TEAR);
		ItemMeta rmeta = redstone.getItemMeta();
		
		rmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.spectator-leave.name")));
		redstone.setItemMeta(rmeta);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
				
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				
				player.getInventory().setItem(Practice.getInstance().getConfig().getInt("items.spectator-leave.position"), redstone);
		
				player.updateInventory();
				
			}
			
		}, 2);
		
	}

	public void tdmItems(final Player player) {
		
		final ItemStack stick = new ItemStack(Material.STICK);
		final ItemStack blaze = new ItemStack(Material.BLAZE_ROD);
		final ItemStack redstone = new ItemStack(Material.REDSTONE);
		
		ItemMeta smeta = stick.getItemMeta();
		ItemMeta bmeta = blaze.getItemMeta();
		ItemMeta rmeta = redstone.getItemMeta();
		
		smeta.setDisplayName(ChatColor.BLUE.toString() + ChatColor.BOLD + "Team 1");
		bmeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Team 2");
		rmeta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "Leave TDM");
		
		stick.setItemMeta(smeta);
		blaze.setItemMeta(bmeta);
		redstone.setItemMeta(rmeta);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
				
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				
				player.getInventory().setItem(0, stick);
				player.getInventory().setItem(1, blaze);
				player.getInventory().setItem(8, redstone);
				
				player.updateInventory();
				
			}
			
		}, 2);
	}
	
}
