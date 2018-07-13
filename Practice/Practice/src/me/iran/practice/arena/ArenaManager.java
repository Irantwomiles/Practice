package me.iran.practice.arena;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import lombok.Getter;
import me.iran.practice.Practice;
import me.iran.practice.enums.ArenaType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ArenaManager {

	private File file = null;
	
	private Random rand = new Random();
	
    //List of all arenas
    @Getter
    private static ArrayList<Arena> arenas = new ArrayList<>();

    //List of all available arenas
    @Getter
    private static ArrayList<Arena> availableArenas = new ArrayList<>();

    //List of arenas selected for duels
    @Getter
    private static ArrayList<Arena> selectedArenas = new ArrayList<>();
    
    private static ArenaManager am;

	private ArenaManager() {}

	public static ArenaManager getManager() {
		if (am == null)
			am = new ArenaManager();

		return am;
	}

    public void loadArenas() {
    	
    	file = new File(Practice.getInstance().getDataFolder() + "/Arenas");
    	
    	if(file.isDirectory()) {
    		
    		File[] files = file.listFiles();
    		
    		if(files.length > 0) {
    			
    			for(int i = 0; i < files.length; i++) {
    				
    				file = new File(Practice.getInstance().getDataFolder() + "/Arenas", files[i].getName());
    				
    				YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    				
    				String id = config.getString("id");
    				String name = config.getString("name");
    				
    				ArenaType type = ArenaType.valueOf(config.getString("type"));
    				
    				Arena arena = new Arena(id);

    				arena.setName(name);
    				arena.setType(type);
    				
    				if(config.contains("loc1")) {
    					
    					double x = config.getDouble("loc1.x");
    					double y = config.getDouble("loc1.y");
    					double z = config.getDouble("loc1.z");
    					double pitch = config.getDouble("loc1.pitch");
    					double yaw = config.getDouble("loc1.yaw");
    					String world = config.getString("loc1.world");
    					
    					Location loc1 = new Location(Bukkit.getWorld(world), x, y, z);
    					loc1.setPitch((float) pitch);
    					loc1.setYaw((float) yaw);
    					
    					arena.setLoc1(loc1);
    				}
    				
    				if(config.contains("loc2")) {
    					
    					double x = config.getDouble("loc2.x");
    					double y = config.getDouble("loc2.y");
    					double z = config.getDouble("loc2.z");
    					double pitch = config.getDouble("loc2.pitch");
    					double yaw = config.getDouble("loc2.yaw");
    					String world = config.getString("loc2.world");
    					
    					System.out.println("x: " + x + "y: " + y + " z: " + z );
    					
    					Location loc2 = new Location(Bukkit.getWorld(world), x, y, z);
    					loc2.setPitch((float) pitch);
    					loc2.setYaw((float) yaw);
    					arena.setLoc2(loc2);
    				}
    				
    				
    				
    				arenas.add(arena);
    				
    				if(arena.getLoc1() != null & arena.getLoc2() != null) {
    					availableArenas.add(arena);
    				}
    				
    			}
    			
    			Practice.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[PRACTICE] LOADED IN " + files.length + " ARENA(S)!");
    			
    		}
    		
    	}
    }
    
    public void saveArenas() {
    	
    	for(Arena arena : arenas) {
    		
    		file = new File(Practice.getInstance().getDataFolder() + "/Arenas", arena.getId() + ".yml");
    		
    		if(!file.exists()) {
    		
    			file = new File(Practice.getInstance().getDataFolder() + "/Arenas", arena.getId() + ".yml");
    			
    			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    			
    			config.createSection("id");
    			config.createSection("name");
    			config.createSection("type");
    			
    			config.set("id", arena.getId());
    			config.set("name", arena.getName());
    			config.set("type", arena.getType().toString());
    			
    			//Has location 1 been set
    			if(arena.getLoc1() != null) {
    				config.createSection("loc1.x");
    				config.createSection("loc1.z");
    				config.createSection("loc1.y");
    				config.createSection("loc1.pitch");
    				config.createSection("loc1.yaw");
    				config.createSection("loc1.world");
    				
    				config.set("loc1.x", arena.getLoc1().getX());
    				config.set("loc1.y", arena.getLoc1().getY());
    				config.set("loc1.z", arena.getLoc1().getZ());
    				config.set("loc1.pitch", arena.getLoc1().getPitch());
    				config.set("loc1.yaw", arena.getLoc1().getYaw());
    				config.set("loc1.world", arena.getLoc1().getWorld().getName());
    			}
    			
    			//Has location 2 been set
    			if(arena.getLoc2() != null) {
    				config.createSection("loc2.x");
    				config.createSection("loc2.z");
    				config.createSection("loc2.y");
    				config.createSection("loc2.pitch");
    				config.createSection("loc2.yaw");
    				config.createSection("loc2.world");
    				
    				config.set("loc2.x", arena.getLoc2().getX());
    				config.set("loc2.y", arena.getLoc2().getY());
    				config.set("loc2.z", arena.getLoc2().getZ());
    				config.set("loc2.pitch", arena.getLoc2().getPitch());
    				config.set("loc2.yaw", arena.getLoc2().getYaw());
    				config.set("loc2.world", arena.getLoc2().getWorld().getName());
    			}
    			
    			try {
					config.save(file);
				} catch (Exception e) {
					Practice.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PRACTICE] ERROR WHILE SAVING ARENA " + arena.getId());
				}
    		} else {
    			
    			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    			
    			//config.createSection("id");
    			//config.createSection("name");
    			//config.createSection("type");
    			
    			config.set("id", arena.getId());
    			config.set("name", arena.getName());
    			config.set("type", arena.getType().toString());
    			
    			/*
    			 * If location 1 has been set update/create needed path
    			 */
    			if(arena.getLoc1() != null) {
    				
    				if(config.contains("loc1")) {
    					config.set("loc1.x", arena.getLoc1().getX());
        				config.set("loc1.y", arena.getLoc1().getY());
        				config.set("loc1.z", arena.getLoc1().getZ());
        				config.set("loc1.pitch", arena.getLoc1().getPitch());
        				config.set("loc1.yaw", arena.getLoc1().getYaw());
        				
        				if(arena.getLoc1().getWorld() != null) {
        					config.set("loc1.world", arena.getLoc1().getWorld().getName());
        				}
        				
        				
    				} else {
    	 				config.createSection("loc1.x");
        				config.createSection("loc1.z");
        				config.createSection("loc1.y");
        				config.createSection("loc1.pitch");
        				config.createSection("loc1.yaw");
        				config.createSection("loc1.world");
        				
        				config.set("loc1.x", arena.getLoc1().getX());
        				config.set("loc1.y", arena.getLoc1().getY());
        				config.set("loc1.z", arena.getLoc1().getZ());
        				config.set("loc1.pitch", arena.getLoc1().getPitch());
        				config.set("loc1.yaw", arena.getLoc1().getYaw());
        				config.set("loc1.world", arena.getLoc1().getWorld().getName());
    				}
    			} else {
    				config.set("loc1", null);
    			}
    			
    			/*
    			 * If location 1 has been set update/create needed path
    			 */
    			if(arena.getLoc2() != null) {
    				
    				if(config.contains("loc2")) {
    					config.set("loc2.x", arena.getLoc2().getX());
        				config.set("loc2.y", arena.getLoc2().getY());
        				config.set("loc2.z", arena.getLoc2().getZ());
        				config.set("loc2.pitch", arena.getLoc2().getPitch());
        				config.set("loc2.yaw", arena.getLoc2().getYaw());
        				
        				if(arena.getLoc2().getWorld() != null) {
        					config.set("loc2.world", arena.getLoc2().getWorld().getName());
        				}
    				} else {
    	 				config.createSection("loc2.x");
        				config.createSection("loc2.z");
        				config.createSection("loc2.y");
        				config.createSection("loc2.pitch");
        				config.createSection("loc2.yaw");
        				config.createSection("loc2.world");
        				
        				config.set("loc2.x", arena.getLoc2().getX());
        				config.set("loc2.y", arena.getLoc2().getY());
        				config.set("loc2.z", arena.getLoc2().getZ());
        				config.set("loc2.pitch", arena.getLoc2().getPitch());
        				config.set("loc2.yaw", arena.getLoc2().getYaw());
        				config.set("loc2.world", arena.getLoc2().getWorld().getName());
    				}
    			} else {
    				config.set("loc2", null);
    			}
    			
    			try {
					config.save(file);
				} catch (Exception e) {
					Practice.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PRACTICE] ERROR WHILE SAVING ARENA " + arena.getId());
				}
    		}
    	}
    	
    	arenas.clear();
    	availableArenas.clear();
    }
    
    /**
     * Create an arena
     * @param id
     * @param player
     */
    
    public void createArena(String id, Player player) {
    	
    	if(doesArenaExist(id)) {
    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("arena-exists").replace("%id%", id)));
    		return;
    	}
    	
    	Arena arena = new Arena(id);
    	
    	arenas.add(arena);
    	
    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("arena-create").replace("%arena%", arena.getId())));
    }
    
	public void deleteArena(Player player, String id) {
		
		if(!doesArenaExist(id)) {
			player.sendMessage(ChatColor.RED + "That arena does not exist");
			return;
		}
		
		Arena arena = getArenaById(id);
		
		file = new File(Practice.getInstance().getDataFolder() + "/Arenas", arena.getId() + ".yml");
		
		file.delete();
		
		arenas.remove(arena);
		
		if(availableArenas.contains(arena)) {
			availableArenas.remove(arena);
		}
		
		player.sendMessage(ChatColor.RED + "Deleted arena, please restart your server before allowing players to join!");
	}
    
	public Arena randomArena() {

		if(getAvailableArenas().size() < 1) {
			return null;
		}
		
		int random = rand.nextInt(availableArenas.size());

		Arena arena = availableArenas.get(random);

		return arena;
	}

	public ArrayList<String> listOfArenas() {

		ArrayList<String> list = new ArrayList<>();

		for (Arena arena : availableArenas) {

			if (!list.contains(arena.getName())) {
				list.add(arena.getName());
			}

		}

		return list;

	}
    
    /*
     * Check if there is an arena with that name available
     */
	public boolean isArenaAvailable(String name) {
		for (Arena arena : availableArenas) {
			if (arena.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
    
    /*
     * Get specific arena by ID
     */
    public Arena getArenaById(String id) {
    	for(Arena arena : arenas) {
    		if(arena.getId().equalsIgnoreCase(id)) {
    			return arena;
    		}
    	}
    	return null;
    }

    /*
     * Return the first arena that is available with this name
     */
    public Arena getArenaByName(String name) {
    	
    		for(Arena arena : availableArenas) {
    			if(arena.getName().equalsIgnoreCase(name)) {
    				return arena;
    			}
    		}
    	
    		return null;
    }
    
    public Arena getArenaBySpectator(UUID uuid) {
    	for(Arena arena : arenas) {
    		if(arena.getSpectators().contains(uuid)) {
    			return arena;
    		}
    	}
    	return null;
    }
    
	public boolean doesArenaExist(String id) {
		for (Arena arena : arenas) {
			if (arena.getId().equalsIgnoreCase(id)) {
				return true;
			}
		}
		return false;
	}
    
}
