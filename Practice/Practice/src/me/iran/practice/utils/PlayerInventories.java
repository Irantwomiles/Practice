package me.iran.practice.utils;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.iran.practice.Practice;
import me.iran.practice.events.lms.LMS;
import me.iran.practice.events.lms.LMSManager;
import me.iran.practice.gametype.GameType;
import me.iran.practice.gametype.GameTypeManager;
import me.iran.practice.kit.KitManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;

public class PlayerInventories {

	public void rankedInventory(Player player) {
		
		Inventory inv = null;
		
		if(GameTypeManager.getGames().size() <= 9) {
			inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.ranked")));
		} else if(GameTypeManager.getGames().size() > 9 && GameTypeManager.getGames().size() <= 18) {
			inv = Bukkit.createInventory(null, 18, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.ranked")));
		} else if(GameTypeManager.getGames().size() > 18 && GameTypeManager.getGames().size() <= 27) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.ranked")));
		} else if(GameTypeManager.getGames().size() > 27 && GameTypeManager.getGames().size() <= 36) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.ranked")));
		} else if(GameTypeManager.getGames().size() > 36 && GameTypeManager.getGames().size() <= 45) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.ranked")));
		} else if(GameTypeManager.getGames().size() > 45 && GameTypeManager.getGames().size() <= 54) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.ranked")));
		}
		
		for(int i = 0; i < GameTypeManager.getGames().size(); i++) {
			
			GameType game = GameTypeManager.getGames().get(i);
			
			if(!game.isRank()) continue;
			
			ItemStack item = game.getDisplay();
			ItemMeta dmeta = item.getItemMeta();
			
			dmeta.setDisplayName(ChatColor.GREEN + game.getName());
			dmeta.setLore(Arrays.asList("", ChatColor.GRAY + "In Game: " + ChatColor.YELLOW + game.getInGame(),
					ChatColor.GRAY + "In Queue: " + ChatColor.YELLOW + game.getRanked().size()));
		
			item.setItemMeta(dmeta);
			inv.setItem(i, item);
		}
		
		player.openInventory(inv);
	}
	
	public void unrankedInventory(Player player) {
		
		Inventory inv = null;
		
		if(GameTypeManager.getGames().size() <= 9) {
			inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.unranked")));
		} else if(GameTypeManager.getGames().size() > 9 && GameTypeManager.getGames().size() <= 18) {
			inv = Bukkit.createInventory(null, 18, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.unranked")));
		} else if(GameTypeManager.getGames().size() > 18 && GameTypeManager.getGames().size() <= 27) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.unranked")));
		} else if(GameTypeManager.getGames().size() > 27 && GameTypeManager.getGames().size() <= 36) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.unranked")));
		} else if(GameTypeManager.getGames().size() > 36 && GameTypeManager.getGames().size() <= 45) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.unranked")));
		} else if(GameTypeManager.getGames().size() > 45 && GameTypeManager.getGames().size() <= 54) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.unranked")));
		}
		
		for(int i = 0; i < GameTypeManager.getGames().size(); i++) {
			
			GameType game = GameTypeManager.getGames().get(i);
			
			ItemStack item = game.getDisplay();
			ItemMeta dmeta = item.getItemMeta();
			
			dmeta.setDisplayName(ChatColor.GREEN + game.getName());
			dmeta.setLore(Arrays.asList("", ChatColor.GRAY + "In Game: " + ChatColor.YELLOW + game.getInGame(),
					ChatColor.GRAY + "In Queue: " + ChatColor.YELLOW + game.getUnranked().size()));
		
			item.setItemMeta(dmeta);
			inv.setItem(i, item);
		}
		player.openInventory(inv);
	}
	
	public void createLMS(Player player) {
		
		Inventory inv = null;
		
		if(GameTypeManager.getGames().size() <= 9) {
			inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.lms")));
		} else if(GameTypeManager.getGames().size() > 9 && GameTypeManager.getGames().size() <= 18) {
			inv = Bukkit.createInventory(null, 18, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.lms")));
		} else if(GameTypeManager.getGames().size() > 18 && GameTypeManager.getGames().size() <= 27) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.lms")));
		} else if(GameTypeManager.getGames().size() > 27 && GameTypeManager.getGames().size() <= 36) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.lms")));
		} else if(GameTypeManager.getGames().size() > 36 && GameTypeManager.getGames().size() <= 45) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.lms")));
		} else if(GameTypeManager.getGames().size() > 45 && GameTypeManager.getGames().size() <= 54) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.lms")));
		}
		
		for(int i = 0; i < GameTypeManager.getGames().size(); i++) {
			
			GameType game = GameTypeManager.getGames().get(i);
			
			ItemStack item = game.getDisplay();
			ItemMeta dmeta = item.getItemMeta();
			
			dmeta.setDisplayName(ChatColor.BLUE + game.getName());
			dmeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to Start"));
			
			item.setItemMeta(dmeta);
			inv.setItem(i, item);
		}
		
		player.openInventory(inv);
	}
	
	public void premiumInventory(Player player) {
		
		Inventory inv = null;
		
		if(GameTypeManager.getGames().size() <= 9) {
			inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.premium")));
		} else if(GameTypeManager.getGames().size() > 9 && GameTypeManager.getGames().size() <= 18) {
			inv = Bukkit.createInventory(null, 18, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.premium")));
		} else if(GameTypeManager.getGames().size() > 18 && GameTypeManager.getGames().size() <= 27) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.premium")));
		} else if(GameTypeManager.getGames().size() > 27 && GameTypeManager.getGames().size() <= 36) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.premium")));
		} else if(GameTypeManager.getGames().size() > 36 && GameTypeManager.getGames().size() <= 45) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.premium")));
		} else if(GameTypeManager.getGames().size() > 45 && GameTypeManager.getGames().size() <= 54) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.premium")));
		}
		
		for(int i = 0; i < GameTypeManager.getGames().size(); i++) {
			
			GameType game = GameTypeManager.getGames().get(i);
			
			ItemStack item = game.getDisplay();
			ItemMeta dmeta = item.getItemMeta();
			
			dmeta.setDisplayName(ChatColor.GREEN + game.getName());
			dmeta.setLore(Arrays.asList("", ChatColor.GRAY + "In Game: " + ChatColor.YELLOW + game.getInGame(),
					ChatColor.GRAY + "In Queue: " + ChatColor.YELLOW + game.getPremium().size()));
		
			item.setItemMeta(dmeta);
			inv.setItem(i, item);
		}
		
		player.openInventory(inv);
	}
	
	public void kitEditorInventory(Player player) {
		
		Inventory inv = null;
		
		int place = 0;
		
		if(GameTypeManager.getGames().size() <= 9) {
			inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.editor")));
		} else if(GameTypeManager.getGames().size() > 9 && GameTypeManager.getGames().size() <= 18) {
			inv = Bukkit.createInventory(null, 18, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.editor")));
		} else if(GameTypeManager.getGames().size() > 18 && GameTypeManager.getGames().size() <= 27) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.editor")));
		} else if(GameTypeManager.getGames().size() > 27 && GameTypeManager.getGames().size() <= 36) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.editor")));
		} else if(GameTypeManager.getGames().size() > 36 && GameTypeManager.getGames().size() <= 45) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.editor")));
		} else if(GameTypeManager.getGames().size() > 45 && GameTypeManager.getGames().size() <= 54) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.editor")));
		}
		
		for(int i = 0; i < GameTypeManager.getGames().size(); i++) {
			
			GameType game = GameTypeManager.getGames().get(i);
			
			if(game.isEditable()) {
				
				ItemStack item = game.getDisplay();
				ItemMeta dmeta = item.getItemMeta();
				
				dmeta.setDisplayName(ChatColor.RED + game.getName());
				
				dmeta.setLore(Arrays.asList(ChatColor.GRAY + "• Click to Edit "));
			
				
				item.setItemMeta(dmeta);
				inv.setItem(place, item);
				place++;
			}
		}
		
		player.openInventory(inv);
	}

	public void kitOptionsInventory(Player player) {
		
		ItemStack save = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
		ItemStack delete = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
		ItemStack edit = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
		ItemStack cancel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
		
		ItemMeta smeta = save.getItemMeta();
		ItemMeta dmeta = delete.getItemMeta();
		ItemMeta emeta = edit.getItemMeta();
		ItemMeta cmeta = cancel.getItemMeta();
		
		smeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "SAVE");
		dmeta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "DELETE");
		emeta.setDisplayName(ChatColor.BLUE.toString() + ChatColor.BOLD + "EDIT");
		cmeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "CANCEL");
		
		save.setItemMeta(smeta);
		delete.setItemMeta(dmeta);
		edit.setItemMeta(emeta);
		cancel.setItemMeta(cmeta);
		
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.kit-options")));
		
		
		inv.setItem(1, save);
		inv.setItem(3, edit);
		inv.setItem(5, delete);
		inv.setItem(7, cancel);
	
		player.openInventory(inv);
		
	}

	public void editKitInventory(Player player, GameType game) {
	
		Inventory inv = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.edit-kit")));
		
		for(int i = 0; i < KitManager.getManager().listPlayerKits(player, game).size(); i++) {
			
			String kit = KitManager.getManager().listPlayerKits(player, game).get(i);
			
			ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
			ItemMeta dmeta = item.getItemMeta();

			dmeta.setDisplayName(ChatColor.BLUE + kit);

			item.setItemMeta(dmeta);
			inv.setItem(i, item);
		}
		
		for(int i = KitManager.getManager().listPlayerKits(player, game).size(); i < 9; i++) {
			ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
			ItemMeta dmeta = item.getItemMeta();

			dmeta.setDisplayName(" ");
			item.setItemMeta(dmeta);
			
			inv.setItem(i, item);
		}
		
		player.openInventory(inv);
	}

	public void deleteKitInventory(Player player, GameType game) {
		
		Inventory inv = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.delete-kit")));
		
		for(int i = 0; i < KitManager.getManager().listPlayerKits(player, game).size(); i++) {
			
			String kit = KitManager.getManager().listPlayerKits(player, game).get(i);
			
			ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
			ItemMeta dmeta = item.getItemMeta();

			dmeta.setDisplayName(ChatColor.RED + kit);

			item.setItemMeta(dmeta);
			inv.setItem(i, item);
		}
		
		for(int i = KitManager.getManager().listPlayerKits(player, game).size(); i < 9; i++) {
			ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
			ItemMeta dmeta = item.getItemMeta();

			dmeta.setDisplayName(" ");
			item.setItemMeta(dmeta);
			
			inv.setItem(i, item);
		}
		
		player.openInventory(inv);
		
	}

	public void saveKitIventory(Player player, GameType game) {
		
		Inventory inv = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.save-kit")));
		
		for(int i = 0; i < KitManager.getManager().listPlayerKits(player, game).size(); i++) {
			
			String kit = KitManager.getManager().listPlayerKits(player, game).get(i);
			
			ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
			ItemMeta dmeta = item.getItemMeta();

			dmeta.setDisplayName(ChatColor.GREEN + kit);
			dmeta.setLore(Arrays.asList(ChatColor.GRAY + "Click on this to replace it to your current kit"));	
			item.setItemMeta(dmeta);
			
			inv.setItem(i, item);
		}
		
		for(int i = KitManager.getManager().listPlayerKits(player, game).size(); i < 9; i++) {
			ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
			ItemMeta dmeta = item.getItemMeta();

			dmeta.setDisplayName(ChatColor.GREEN + "EMPTY");
			dmeta.setLore(Arrays.asList(ChatColor.GRAY + "Click on this to save a new kit"));	
			item.setItemMeta(dmeta);
			
			inv.setItem(i, item);
		}
		
		player.openInventory(inv);
		
	}

	public void gameTypes(Player player) {

		Inventory inv = null;
		
		if(GameTypeManager.getGames().size() <= 9) {
			inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.duel")));
		} else if(GameTypeManager.getGames().size() > 9 && GameTypeManager.getGames().size() <= 18) {
			inv = Bukkit.createInventory(null, 18, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.duel")));
		} else if(GameTypeManager.getGames().size() > 18 && GameTypeManager.getGames().size() <= 27) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.duel")));
		} else if(GameTypeManager.getGames().size() > 27 && GameTypeManager.getGames().size() <= 36) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.duel")));
		} else if(GameTypeManager.getGames().size() > 36 && GameTypeManager.getGames().size() <= 45) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.duel")));
		} else if(GameTypeManager.getGames().size() > 45 && GameTypeManager.getGames().size() <= 54) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.duel")));
		}
		
		for(int i = 0; i < GameTypeManager.getGames().size(); i++) {
			
			GameType game = GameTypeManager.getGames().get(i);
			
			ItemStack item = game.getDisplay();
			ItemMeta dmeta = item.getItemMeta();
			
			dmeta.setDisplayName(ChatColor.GREEN + game.getName());
		
			item.setItemMeta(dmeta);
			inv.setItem(i, item);
		}
		player.openInventory(inv);
	}
	
	public void TDM(Player player) {

		Inventory inv = null;
		
		if(GameTypeManager.getGames().size() <= 9) {
			inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.tdm")));
		} else if(GameTypeManager.getGames().size() > 9 && GameTypeManager.getGames().size() <= 18) {
			inv = Bukkit.createInventory(null, 18, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.tdm")));
		} else if(GameTypeManager.getGames().size() > 18 && GameTypeManager.getGames().size() <= 27) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.tdm")));
		} else if(GameTypeManager.getGames().size() > 27 && GameTypeManager.getGames().size() <= 36) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.tdm")));
		} else if(GameTypeManager.getGames().size() > 36 && GameTypeManager.getGames().size() <= 45) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.tdm")));
		} else if(GameTypeManager.getGames().size() > 45 && GameTypeManager.getGames().size() <= 54) {
			inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.tdm")));
		}
		
		for(int i = 0; i < GameTypeManager.getGames().size(); i++) {
			
			GameType game = GameTypeManager.getGames().get(i);
			
			ItemStack item = game.getDisplay();
			ItemMeta dmeta = item.getItemMeta();
			
			dmeta.setDisplayName(ChatColor.GREEN + game.getName());
		
			item.setItemMeta(dmeta);
			inv.setItem(i, item);
		}
		player.openInventory(inv);
	}
	
	public void partyQueue(Player player) {
		
		Inventory inv = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.party.queue")));
		
		ItemStack diamond = new ItemStack(Material.DIAMOND_SWORD);
		ItemStack iron = new ItemStack(Material.IRON_SWORD);
		ItemStack gold = new ItemStack(Material.GOLD_SWORD);
		
		ItemMeta dmeta = diamond.getItemMeta();
		ItemMeta imeta = iron.getItemMeta();
		ItemMeta gmeta = gold.getItemMeta();
		
		dmeta.setDisplayName(ChatColor.GREEN + "Ranked Duo Queue");
		imeta.setDisplayName(ChatColor.BLUE + "Unranked Duo Queue");
		gmeta.setDisplayName(ChatColor.GOLD + "Premium Duo Queue");
		
		diamond.setItemMeta(dmeta);
		iron.setItemMeta(imeta);
		gold.setItemMeta(gmeta);
		
		inv.setItem(1, diamond);
		inv.setItem(4, iron);
		inv.setItem(7, gold);
		
		player.openInventory(inv);
	}

	public void events(Player player) {
		
		Inventory inv = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.events")));
		
		ItemStack diamond = new ItemStack(Material.DIAMOND_AXE);
		ItemStack stone = new ItemStack(Material.STONE_SWORD);
		
		ItemMeta dmeta = diamond.getItemMeta();
		ItemMeta smeta = stone.getItemMeta();
		
		dmeta.setDisplayName(ChatColor.YELLOW + "LMS");
		smeta.setDisplayName(ChatColor.GREEN + "TDM");
		
		diamond.setItemMeta(dmeta);
		stone.setItemMeta(smeta);
		
		inv.setItem(1, diamond);
		inv.setItem(3, stone);
		
		player.openInventory(inv);
	}	
	
	public void host(Player player) {
		
		Inventory inv = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.host")));
		
		ItemStack diamond = new ItemStack(Material.DIAMOND_AXE);
		ItemStack stone = new ItemStack(Material.STONE_SWORD);
		
		ItemMeta dmeta = diamond.getItemMeta();
		ItemMeta smeta = stone.getItemMeta();
		
		dmeta.setDisplayName(ChatColor.YELLOW + "LMS");
		smeta.setDisplayName(ChatColor.GREEN + "TDM");
		
		diamond.setItemMeta(dmeta);
		stone.setItemMeta(smeta);
		
		inv.setItem(1, diamond);
		inv.setItem(3, stone);
		
		player.openInventory(inv);
	}
	
	public void lms(Player player) {
		
		Inventory inv = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.list-lms")));
		
		for(int i = 0; i < LMSManager.getLmsList().size(); i++) {
			
			LMS lms = LMSManager.getLmsList().get(i);
			
			if(lms.isActive()) {
				ItemStack item = new ItemStack(Material.REDSTONE_BLOCK, lms.getPlayers().size());
			
				ItemMeta meta = item.getItemMeta();
				
				meta.setDisplayName(ChatColor.RED.toString() + lms.getId());
				meta.setLore(Arrays.asList("", ChatColor.GRAY + "GameType: " + ChatColor.YELLOW + lms.getGame().getName()));
				
				item.setItemMeta(meta);
				
				inv.setItem(i, item);
			} else {
				ItemStack item = new ItemStack(Material.QUARTZ, lms.getPlayers().size());
				
				ItemMeta meta = item.getItemMeta();
				
				meta.setDisplayName(ChatColor.GREEN.toString() + lms.getId());
				meta.setLore(Arrays.asList("", ChatColor.GRAY + "GameType: " + ChatColor.YELLOW + lms.getGame().getName()));
				item.setItemMeta(meta);
				
				inv.setItem(i, item);
			}
		}
		
		player.openInventory(inv);
	}	

	public void queue(Player player) {
		
		Inventory inv = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.queue")));
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		ItemStack ranked = new ItemStack(Material.DIAMOND_SWORD);
		ItemStack unranked = new ItemStack(Material.IRON_SWORD);
		ItemStack premium = new ItemStack(Material.GOLD_SWORD);
		
		ItemMeta rmeta = ranked.getItemMeta();
		ItemMeta umeta = unranked.getItemMeta();
		ItemMeta pmeta = premium.getItemMeta();
		
		rmeta.setDisplayName(ChatColor.GREEN + "Ranked Queue");
		umeta.setDisplayName(ChatColor.BLUE + "Unranked Queue");
		pmeta.setDisplayName(ChatColor.GOLD + "Premium Queue");
		
		pmeta.setLore(Arrays.asList("", ChatColor.YELLOW + "Matches Left: " + ChatColor.WHITE + profile.getPremium()));
		
		ranked.setItemMeta(rmeta);
		unranked.setItemMeta(umeta);
		premium.setItemMeta(pmeta);
		
		inv.setItem(1, ranked);
		inv.setItem(4, unranked);
		inv.setItem(7, premium);
		
		player.openInventory(inv);
	}	

	public void options(Player player) {
		
		Inventory inv = Bukkit.createInventory(player, 36, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.options")));
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		ItemStack kdr = new ItemStack(Material.DIAMOND_SWORD);
		ItemStack premium = new ItemStack(Material.EMERALD);
		ItemStack token = new ItemStack(Material.DIAMOND);
		ItemStack duel = new ItemStack(Material.IRON_HELMET);
		ItemStack spec = new ItemStack(Material.COMPASS);
		ItemStack reset = new ItemStack(Material.REDSTONE_BLOCK);		
		
		ItemMeta kMeta = kdr.getItemMeta();
		ItemMeta pMeta = premium.getItemMeta();
		ItemMeta tMeta = token.getItemMeta();
		ItemMeta dMeta = duel.getItemMeta();
		ItemMeta sMeta = spec.getItemMeta();
		ItemMeta rMeta = reset.getItemMeta();
		
		kMeta.setDisplayName(ChatColor.RED + "Kill Death Ratio");
		kMeta.setLore(Arrays.asList(
				ChatColor.YELLOW + "Kills: " + ChatColor.GRAY + profile.getKill(),
				ChatColor.YELLOW + "Deaths: " + ChatColor.GRAY + profile.getDeath(),
				ChatColor.YELLOW + "KDR: " + ChatColor.GRAY + profile.KDR()));
		
		pMeta.setDisplayName(ChatColor.GOLD.toString() + "Premium Matches");
		pMeta.setLore(Arrays.asList(ChatColor.YELLOW + "Matches: " + ChatColor.GRAY + profile.getPremium()));
		
		tMeta.setDisplayName(ChatColor.AQUA.toString() + "Event Tokens");
		tMeta.setLore(Arrays.asList(ChatColor.YELLOW + "Tokens: " + ChatColor.GRAY + profile.getToken()));
		
		dMeta.setDisplayName(ChatColor.GREEN.toString() + "Duel Requests");
		dMeta.setLore(Arrays.asList(ChatColor.YELLOW + "Allow Duel Requests: " + ChatColor.GRAY + profile.isAllowDuel()));
		
		sMeta.setDisplayName(ChatColor.GREEN.toString() + "Spectator Message");
		sMeta.setLore(Arrays.asList(ChatColor.YELLOW + "Allow Spectator Message: " + ChatColor.GRAY + profile.isSpectatorMessage()));
		
		rMeta.setDisplayName(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Reset Elo");
		rMeta.setLore(Arrays.asList(ChatColor.GRAY.toString() + ChatColor.BOLD + "This will reset ALL of your Elo rankings", ChatColor.RED.toString() + ChatColor.BOLD + "This can NOT be undone"));
		
		kdr.setItemMeta(kMeta);
		premium.setItemMeta(pMeta);
		token.setItemMeta(tMeta);
		duel.setItemMeta(dMeta);
		spec.setItemMeta(sMeta);
		reset.setItemMeta(rMeta);
		
		inv.setItem(3, duel);
		inv.setItem(5, spec);
		inv.setItem(11, kdr);
		inv.setItem(13, premium);
		inv.setItem(15, token);
		inv.setItem(31, reset);
		player.openInventory(inv);
	}
}
