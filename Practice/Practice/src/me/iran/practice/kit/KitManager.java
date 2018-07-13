package me.iran.practice.kit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.iran.practice.Practice;
import me.iran.practice.gametype.GameType;
import me.iran.practice.gametype.GameTypeManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitManager {

	private File file = null;
	
    private static KitManager km;

	private KitManager() {}

	public static KitManager getManager() {
		if (km == null)
			km = new KitManager();

		return km;
	}
	
	public void generateFile(Player player) {
		
		file = new File(Practice.getInstance().getDataFolder() + "/Kit/" + player.getUniqueId().toString());
		
		if(file.exists()) {
			
			if(file.isDirectory()) {
				File[] files = file.listFiles();
				
				if(files.length > 0) {

					for(File f : files) {
						
						for(GameType game : GameTypeManager.getGames()) {
							
							if(f.getName().equalsIgnoreCase(game.getName() + ".yml")) {
								continue;
							}
							
							file = new File(Practice.getInstance().getDataFolder() + "/Kit/" + player.getUniqueId().toString(), game.getName() + ".yml");

							YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
							
							config.createSection(game.getName() + ".size");
							config.set(game.getName() + ".size", 0);
							try {
								config.save(file);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

		} else {
			
			for(GameType game : GameTypeManager.getGames()) {
				
				file = new File(Practice.getInstance().getDataFolder() + "/Kit/" + player.getUniqueId().toString(), game.getName() + ".yml");

				YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
				
				config.createSection(game.getName() + ".size");
				config.set(game.getName() + ".size", 0);
				try {
					config.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void createKit(String name, GameType game, Player player) {
		
		file = new File(Practice.getInstance().getDataFolder() + "/Kit/" + player.getUniqueId().toString(), game.getName() + ".yml");
		
		if(file.exists()) {
			
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			int size = config.getInt(game.getName() + ".size") + 1;
			
			if(doesKitExist(player, game, name)) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("kit-exists").replace("%gametype%", game.getName()).replace("%kit%", name)));
				return;
			}
			
			if(name.equalsIgnoreCase("size")) {
				player.sendMessage(ChatColor.GRAY + "Please avoid using '" + ChatColor.RED + "size" + ChatColor.GRAY + "' as your Kit name");
				return;
			}
			
			if(size > 9) {
				player.sendMessage(ChatColor.GRAY + "You have reached your maximum number of Kits you can create");
				return;
			}
			
			config.createSection(game.getName() + "." + name + ".inv");
			config.createSection(game.getName() + "." + name + ".armor");
			
			config.set(game.getName() + "." + name + ".inv", player.getInventory().getContents());
			config.set(game.getName() + "." + name + ".armor", player.getInventory().getArmorContents());
			
			
			
			config.set(game.getName() + ".size", size);
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("kit-created").replace("%kit%", name)));
			
			try {
				config.save(file);
			} catch (Exception e) {
				Practice.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PRACTICE] ERROR WHILE SAVING FILE IN createKit()");
			}
		}
		
	}
	
	public boolean doesKitExist(Player player, GameType game, String name) {
		
		file = new File(Practice.getInstance().getDataFolder() + "/Kit/" + player.getUniqueId().toString(), game.getName() + ".yml");
		
		if(file.exists()) {
			
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			for(String str : config.getConfigurationSection(game.getName()).getKeys(false)) {
				
				if(str.equalsIgnoreCase(name)) {
					return true;
				}
				
			}
		}
		return false;
	}

	public void getPlayerKits(Player player, GameType game) {

		file = new File(Practice.getInstance().getDataFolder() + "/Kit/" + player.getUniqueId().toString(), game.getName() + ".yml");
		
		ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta bmeta = book.getItemMeta();
		
		if(file.exists()) {
			
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			
			List<String> kits = new ArrayList<>();
			
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			for(String str : config.getConfigurationSection(game.getName()).getKeys(false)) {
				
				if(str.equalsIgnoreCase("size")) {
					continue;
				}
				kits.add(str);
			}
			
			for(int i = 0; i < kits.size(); i++) {
				bmeta.setDisplayName(ChatColor.RED + kits.get(i));
				book.setItemMeta(bmeta);
				player.getInventory().setItem(i+1, book);
			}
			
			bmeta.setDisplayName(ChatColor.YELLOW + "Default");
			book.setItemMeta(bmeta);
			player.getInventory().setItem(0, book);
			
			player.setHealth(20.0);
			player.setFoodLevel(20);
		}
	}

	public void loadPlayerKit(final Player player, final GameType game, String name) {

		if(name.equalsIgnoreCase("default")) {
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
				public void run() {
					player.getInventory().setContents(game.getInv());
					player.getInventory().setArmorContents(game.getArmor());
				}
				
			}, 3);
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("kit-loaded").replace("%kit%", "Default")));
			return;
		}
		
		file = new File(Practice.getInstance().getDataFolder() + "/Kit/" + player.getUniqueId().toString(), game.getName() + ".yml");
		
		if(file.exists()) {
			
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			if(config.contains(game.getName() + "." + name)) {
				
				List<?> inv = config.getList(game.getName() + "." + name + ".inv");
				List<?> armor = config.getList(game.getName() + "." + name + ".armor");
				
				ItemStack[] inventory = inv.toArray(new ItemStack[inv.size()]);
				ItemStack[] gear = armor.toArray(new ItemStack[armor.size()]);
			
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				
				player.getInventory().setContents(inventory);
				player.getInventory().setArmorContents(gear);
				
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("kit-loaded").replace("%kit%", name)));
				
			}
		}
	}

	public void updatePlayerKit(Player player, GameType game, String name) {

		file = new File(Practice.getInstance().getDataFolder() + "/Kit/" + player.getUniqueId().toString(), game.getName() + ".yml");
		
		if(file.exists()) {
			
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			if(config.contains(game.getName() + "." + name)) {
				config.set(game.getName() + "." + name + ".inv", player.getInventory().getContents());
				config.set(game.getName() + "." + name + ".armor", player.getInventory().getArmorContents());
				
				try {
					config.save(file);
				} catch (Exception e) {
					Practice.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PRACTICE] ERROR WHILE SAVING FILE IN createKit()");
				}
			
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("kit-updated").replace("%kit%", name)));
				
			} else {
				player.sendMessage(ChatColor.RED + "Couldn't find that kit");
			}
			
		}
	}

	public void deletePlayerKit(Player player, GameType game, String name) {

		file = new File(Practice.getInstance().getDataFolder() + "/Kit/" + player.getUniqueId().toString(), game.getName() + ".yml");
		
		if(file.exists()) {
			
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			if(config.contains(game.getName() + "." + name)) {
				
				int size = config.getInt(game.getName() + ".size") + 1;
				
				config.set(game.getName() + "." + name, null);
				
				config.set(game.getName() + ".size", size);
				
				try {
					config.save(file);
				} catch (Exception e) {
					Practice.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PRACTICE] ERROR WHILE SAVING FILE IN createKit()");
				}
			
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("kit-deleted").replace("%kit%", name)));
				
			} else {
				player.sendMessage(ChatColor.RED + "Couldn't find that kit");
			}
			
		}
	}
	
	public List<String> listPlayerKits(Player player, GameType game) {

		file = new File(Practice.getInstance().getDataFolder() + "/Kit/" + player.getUniqueId().toString(), game.getName() + ".yml");
		
		if(file.exists()) {
			
			List<String> kits = new ArrayList<>();
			
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			for(String str : config.getConfigurationSection(game.getName()).getKeys(false)) {
				
				if(str.equalsIgnoreCase("size")) {
					continue;
				}
				kits.add(str);
			}
			
			return kits;
		}
		
		return null;
	}
}
