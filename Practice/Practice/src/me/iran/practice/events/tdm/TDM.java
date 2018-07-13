package me.iran.practice.events.tdm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.Practice;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.gametype.GameType;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import net.md_5.bungee.api.ChatColor;

public class TDM {

	private File file = null;
	
	@Getter
	@Setter
	public static ArrayList<UUID> team1 = new ArrayList<>();
	
	@Getter
	@Setter
	public static ArrayList<UUID> team2 = new ArrayList<>();
	
	@Getter
	@Setter
	public static int 
	score1 = 0,
	score2 = 0, 
	timer = 60,
	gameTimer = 0,
	max = 50, 
	min = 1;
	
	@Getter
	public int win = 35;
	
	@Getter
	@Setter
	public static GameType game = null;
	
	@Getter
	@Setter
	public static Location loc1, loc2;

	@Getter
	@Setter
	public static boolean active = false;
	
	@Getter
	@Setter
	public static boolean started = false;
	
	public void loadTDM() {
		
		file = new File(Practice.getInstance().getDataFolder(), "TDM.yml");
		
		if(file.exists()) {
			
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
			max = config.getInt("max-players");
			timer = config.getInt("timer");
			
			if(config.contains("loc1")) {
				double x = config.getDouble("loc1.x");
				double y = config.getDouble("loc1.y");
				double z = config.getDouble("loc1.z");
				float pitch = (float) config.getDouble("loc1.pitch");
				float yaw = (float) config.getDouble("loc1.yaw");
				String world = config.getString("loc1.world");
				
				loc1 = new Location(Bukkit.getWorld(world), x, y, z);
				loc1.setPitch(pitch);
				loc1.setYaw(yaw);
			}
			
			if(config.contains("loc2")) {
				double x = config.getDouble("loc2.x");
				double y = config.getDouble("loc2.y");
				double z = config.getDouble("loc2.z");
				float pitch = (float) config.getDouble("loc2.pitch");
				float yaw = (float) config.getDouble("loc2.yaw");
				String world = config.getString("loc2.world");
				
				loc2 = new Location(Bukkit.getWorld(world), x, y, z);
				loc2.setPitch(pitch);
				loc2.setYaw(yaw);
			}
		} else {
			createFile();
		}
	}

	public void createFile() {

		file = new File(Practice.getInstance().getDataFolder(), "TDM.yml");

		if(!file.exists()) {
			
			file = new File(Practice.getInstance().getDataFolder(), "TDM.yml");
			
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			config.createSection("max-players");
			config.createSection("timer");
			
			config.set("max-players", max);
			config.set("timer", timer);
			config.set("win-score", 100);
			
			if(loc1 != null) {
				config.createSection("loc1.x");
				config.createSection("loc1.y");
				config.createSection("loc1.z");
				
				config.createSection("loc1.pitch");
				config.createSection("loc1.yaw");
				
				config.createSection("loc1.world");
				
				config.set("loc1.x", loc1.getX());
				config.set("loc1.y", loc1.getY());
				config.set("loc1.z", loc1.getZ());
				
				config.set("loc1.pitch", loc1.getPitch());
				config.set("loc1.yaw", loc1.getYaw());
				
				config.set("loc1.world", loc1.getWorld().getName());
				
			}
			
			if(loc2 != null) {
				config.createSection("loc2.x");
				config.createSection("loc2.y");
				config.createSection("loc2.z");
				
				config.createSection("loc2.pitch");
				config.createSection("loc2.yaw");
				
				config.createSection("loc2.world");
				
				config.set("loc2.x", loc2.getX());
				config.set("loc2.y", loc2.getY());
				config.set("loc2.z", loc2.getZ());
				
				config.set("loc2.pitch", loc2.getPitch());
				config.set("loc2.yaw", loc2.getYaw());
				
				config.set("loc2.world", loc2.getWorld().getName());
			}
			
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			loadTDM();
			
		}
		
	}

	public void saveFile() {

		file = new File(Practice.getInstance().getDataFolder(), "TDM.yml");

		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set("max-players", max);
		config.set("timer", 60);
		
		if (loc1 != null) {

			if(config.contains("loc1")) {
				config.set("loc1.x", loc1.getX());
				config.set("loc1.y", loc1.getY());
				config.set("loc1.z", loc1.getZ());

				config.set("loc1.pitch", loc1.getPitch());
				config.set("loc1.yaw", loc1.getYaw());

				config.set("loc1.world", loc1.getWorld().getName());
			} else {
				config.createSection("loc1.x");
				config.createSection("loc1.y");
				config.createSection("loc1.z");
				
				config.createSection("loc1.pitch");
				config.createSection("loc1.yaw");
				
				config.createSection("loc1.world");
				
				config.set("loc1.x", loc1.getX());
				config.set("loc1.y", loc1.getY());
				config.set("loc1.z", loc1.getZ());
				
				config.set("loc1.pitch", loc1.getPitch());
				config.set("loc1.yaw", loc1.getYaw());
				
				config.set("loc1.world", loc1.getWorld().getName());
			}

		} else {
			if(config.contains("loc1")) {
				config.set("loc1", null);
			}
		}

		if (loc2 != null) {

			if(config.contains("loc2")) {
				config.set("loc2.x", loc2.getX());
				config.set("loc2.y", loc2.getY());
				config.set("loc2.z", loc2.getZ());

				config.set("loc2.pitch", loc2.getPitch());
				config.set("loc2.yaw", loc2.getYaw());

				config.set("loc2.world", loc2.getWorld().getName());
			} else {
				config.createSection("loc2.x");
				config.createSection("loc2.y");
				config.createSection("loc2.z");
				
				config.createSection("loc2.pitch");
				config.createSection("loc2.yaw");
				
				config.createSection("loc2.world");
				
				config.set("loc2.x", loc2.getX());
				config.set("loc2.y", loc2.getY());
				config.set("loc2.z", loc2.getZ());
				
				config.set("loc2.pitch", loc2.getPitch());
				config.set("loc2.yaw", loc2.getYaw());
				
				config.set("loc2.world", loc2.getWorld().getName());
			}

		} else {
			if(config.contains("loc2")) {
				config.set("loc2", null);
			}
		}

		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void joinTDM(Player player) {
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(profile == null) {
			return;
		}
		
		if(!isActive()) {
			player.sendMessage(ChatColor.RED + "TDM event is not active right now!");
			return;
		}
		
		if(isStarted()) {
			player.sendMessage(ChatColor.RED + "TDM event has already started, wait for the next game!");
			return;
		}
		
		if(team1.size() >= max && team2.size() >= max) {
			player.sendMessage(ChatColor.RED + "All teams seem to be full, wait until the next round please!");
			return;
		}
		
		if(profile.getState() != PlayerState.LOBBY) {
			player.sendMessage(ChatColor.RED + "Must be in the lobby to do this!");
			return;
		}
		
		if(team1.size() > team2.size()) {
			team2.add(player.getUniqueId());
			
			player.teleport(loc2);
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("tdm-join-team2")));
		
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			
			profile.setState(PlayerState.IN_EVENT);
			
			Bukkit.getPluginManager().callEvent(new JoinTDM(player));
			
			return;
			
		} else if(team2.size() > team1.size()) {
			
			team1.add(player.getUniqueId());

			player.teleport(loc1);
	
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("tdm-join-team1")));
		
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			
			profile.setState(PlayerState.IN_EVENT);
			
			Bukkit.getPluginManager().callEvent(new JoinTDM(player));
			
			return;
			
		} if(team1.size() == team2.size()) {
			
			team1.add(player.getUniqueId());
		
			player.teleport(loc1);
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("tdm-join-team1")));
		
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			
			profile.setState(PlayerState.IN_EVENT);
			
			Bukkit.getPluginManager().callEvent(new JoinTDM(player));
			
			return;
		}
		
	}
	
	public void leaveTDM(Player player) {

		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);

		if (profile == null) {
			return;
		}
		
		if(!isActive()) {
			return;
		}

		if(!team1.contains(player.getUniqueId()) && !team2.contains(player.getUniqueId())) {
			player.sendMessage(ChatColor.GRAY + "Doesn't seem like you are in the TDM event");
			return;
		}

		if (team1.contains(player.getUniqueId())) {
			team1.remove(player.getUniqueId());
			
			if(team1.size() <= 0) {
				endGame(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("tdm-team2-win")));
				return;
			}
			
			sendTeamMessage(team1, ChatColor.GRAY + player.getName() + " has left the team");
		} else if (team2.contains(player.getUniqueId())) {
			team2.remove(player.getUniqueId());
			
			if(team2.size() <= 0) {
				endGame(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("tdm-team1-win")));
				return;
			}
			sendTeamMessage(team2, ChatColor.GRAY + player.getName() + " has left the team");
		}
		
		profile.setState(PlayerState.LOBBY);
		Practice.getInstance().teleportSpawn(player);
		
		Bukkit.getPluginManager().callEvent(new LeaveTDM(player));
	}
	
	public void sendTeamMessage(ArrayList<UUID> team, String msg) {
		
		for(UUID uuid : team ) {
			Player p = Bukkit.getPlayer(uuid);
			if(p!=null) {
				p.sendMessage(msg);
			}
		}
		
	}
	
	public void endGame(String msg) {
		
		if(!isActive()) {
			return;
		}
		
		if(!isStarted()) {
			return;
		}
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
				for(UUID uuid : team1) {
					Player p = Bukkit.getPlayer(uuid);
					if(p!=null) {
						
						p.getInventory().clear();
						p.getInventory().setArmorContents(null);
						
						Practice.getInstance().teleportSpawn(p);
						
						Bukkit.getPluginManager().callEvent(new EndTDM(p));
						
						
					}
				}
				
				for(UUID uuid : team2) {
					Player p = Bukkit.getPlayer(uuid);
					if(p!=null) {
						
						p.getInventory().clear();
						p.getInventory().setArmorContents(null);
						
						Practice.getInstance().teleportSpawn(p);
						
						Bukkit.getPluginManager().callEvent(new EndTDM(p));
						
						
					}
				}
				
				team1.clear();
				team2.clear();
				
				setActive(false);
				setStarted(false);
				
				score1 = 0;
				score2 = 0;
				
				timer = 60;
				
			}
			
		}, 60);

		Bukkit.broadcastMessage(msg);
	}
	
	public void forceEndGame(String msg) {
		
		if(!isActive()) {
			return;
		}
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
				for(int i = 0; i < team1.size(); i++) {
					Player p = Bukkit.getPlayer(team1.get(i));
					if(p!=null) {
						
						p.getInventory().clear();
						p.getInventory().setArmorContents(null);
						
						Practice.getInstance().teleportSpawn(p);
						
						Bukkit.getPluginManager().callEvent(new EndTDM(p));
					}
				}
				
				for(int i = 0; i < team2.size(); i++) {
					Player p = Bukkit.getPlayer(team2.get(i));
					if(p!=null) {
						
						p.getInventory().clear();
						p.getInventory().setArmorContents(null);
						
						Practice.getInstance().teleportSpawn(p);
						
						Bukkit.getPluginManager().callEvent(new EndTDM(p));
						
					}
				}
				
				team1.clear();
				team2.clear();
				
				setActive(false);
				setStarted(false);
				
				score1 = 0;
				score2 = 0;
				
				timer = 60;
				
			}
			
		}, 20);
		
		
		Bukkit.broadcastMessage(msg);
	}
	
	public boolean isPlayerInTDM(Player player) {
		
		if(team1.contains(player.getUniqueId())) {
			return true;
		}
		
		if(team2.contains(player.getUniqueId())) {
			return true;
		}
		
		return false;
		
	}

	public ArrayList<UUID> getPlayerTeam(Player player) {
		
		if(team1.contains(player.getUniqueId())) {
			return team1;
		} else if(team2.contains(player.getUniqueId())) {
			return team2;
		} 
		
		return null;
		
	}

	public void check() {
		
		if(!isActive()) {
			return;
		}
		
		if(!isStarted()) {
			return;
		}
		
		if(score1 == win) {
			endGame(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("tdm-team1-win")));
			return;
		} 
		
		if(score2 == win) {
			endGame(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("tdm-team1-win")));
			return;
		}
		
	}
}
