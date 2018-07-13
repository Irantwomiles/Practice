package me.iran.practice.gametype;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import me.iran.practice.Practice;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GameTypeManager {

	@Getter
	private static ArrayList<GameType> games = new ArrayList<>();
	
	private File file = null;
	
	private static GameTypeManager gm;

	private GameTypeManager() {}

	public static GameTypeManager getManager() {
		if (gm == null)
			gm = new GameTypeManager();

		return gm;
	}
	
	public void loadGameTypes() {
		
		file = new File(Practice.getInstance().getDataFolder() + "/Gametypes");
		
		if (file.isDirectory()) {
			
			File[] files = file.listFiles();
			
			if (files.length > 0) {
				for (int i = 0; i < files.length; i++) {

					file = new File(Practice.getInstance().getDataFolder() + "/Gametypes", files[i].getName());

					YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

					String name = config.getString("name");
					ItemStack display = (ItemStack) config.get("display");
					boolean edit = config.getBoolean("edit");
					boolean ranked = config.getBoolean("ranked");
					
					List<?> inv = config.getList("inv");
					List<?> armor = config.getList("armor");
					List<?> chest = config.getList("chest");
					
					ItemStack[] inventory = inv.toArray(new ItemStack[inv.size()]);
					ItemStack[] gear = armor.toArray(new ItemStack[armor.size()]);
					ItemStack[] editor = chest.toArray(new ItemStack[chest.size()]);
					
					GameType game = new GameType(name, inventory, gear);
					
					game.setChest(editor);
					game.setEditable(edit);
					game.setDisplay(display);
					game.setRank(ranked);
					
					games.add(game);
					
				}

			}
		}
	}
	
	public void saveGameTypes() {
		
		if(games.size() > 0) {
			
			for(GameType game : games) {
				
				file = new File(Practice.getInstance().getDataFolder() + "/Gametypes", game.getName() + ".yml");
				
				if(file.exists()) {
					YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
					
					config.set("name", game.getName());
					config.set("display", game.getDisplay());
					config.set("edit", game.isEditable());
					config.set("ranked", game.isRank());
					config.set("chest", game.getChest());
					
					config.set("inv", game.getInv());
					config.set("armor", game.getArmor());
					
					try {
						config.save(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} else {
					
					file = new File(Practice.getInstance().getDataFolder() + "/Gametypes", game.getName() + ".yml");
					
					YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
					
					config.createSection("name");
					config.createSection("display");
					config.createSection("edit");
					config.createSection("ranked");
					config.createSection("inv");
					config.createSection("armor");
					config.createSection("chest");
					
					config.set("name", game.getName());
					config.set("display", game.getDisplay());
					config.set("edit", game.isEditable());
					config.set("ranked", game.isRank());
					config.set("chest", game.getChest());
					config.set("inv", game.getInv());
					config.set("armor", game.getArmor());
					
					try {
						config.save(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
		}
	}
	
	public void createGameType(Player player, String name) {
		
		if(gameTypeExist(name)) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("gametype-exists").replace("%gametype%", getGameType(name).getName())));
			return;
		}
		
		GameType game = new GameType(name, player.getInventory().getContents(), player.getInventory().getArmorContents());
		
		games.add(game);
		
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("gametype-created").replace("%gametype%", getGameType(name).getName())));
		
	}
	
	public void deleteGameType(Player player, String name) {
		
		if(!gameTypeExist(name)) {
			player.sendMessage(ChatColor.RED + "That gametype does not exist");
			return;
		}
		
		GameType game = getGameType(name);
		
		file = new File(Practice.getInstance().getDataFolder() + "/Gametypes", game.getName() + ".yml");
		
		file.delete();
		
		games.remove(game);
		
		player.sendMessage(ChatColor.RED + "Deleted gametype " + name);
	}
	
	
	public void setRanked(Player player, String name) {
		
		if(!gameTypeExist(name)) {
			player.sendMessage(ChatColor.RED + "That gametype does not exist");
			return;
		}
		
		GameType game = getGameType(name);
		
		if(game.isRank()) {
			game.setRank(false);
		} else {
			game.setRank(true);
		}
		
		player.sendMessage(ChatColor.GOLD + "Raned Mode for Gametype " + name + " is now " + ChatColor.GRAY + game.isRank());
	}
	
	public GameType getGameType(String name) {
		for(GameType game : games) {
			if(game.getName().equalsIgnoreCase(name)) {
				return game;
			}
		}
		return null;
	}
	
	public boolean gameTypeExist(String name) {
		for(GameType game : games) {
			if(game.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
}
