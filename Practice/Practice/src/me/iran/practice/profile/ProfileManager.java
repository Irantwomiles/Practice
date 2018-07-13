package me.iran.practice.profile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.iran.practice.Practice;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.gametype.GameType;
import me.iran.practice.gametype.GameTypeManager;

public class ProfileManager {

    private File file = null;

    private static Set<Profile> profileSet = new HashSet<>();

    private static ProfileManager manager;

    private ProfileManager() {};
    
    public static ProfileManager getManager() {
        if(manager == null) {
            manager = new ProfileManager();
        }

        return manager;
    }

    /**
     * get player profile using the Player object
     * @param player
     * @return profile
     */
    public Profile getProfileByPlayer(Player player) {

        for(Profile profile : profileSet) {
            if(profile.getUuid().equalsIgnoreCase(player.getUniqueId().toString())){
                return profile;
            }
        }
        return null;
    }
    
    public Profile getProfileByOfflinePlayer(OfflinePlayer player) {

        for(Profile profile : profileSet) {
            if(profile.getUuid().equalsIgnoreCase(player.getUniqueId().toString())){
                return profile;
            }
        }
        return null;
    }

    /**
     * get player profile using UUID in String format
     * @param uuid
     * @return profile
     */
    public Profile getProfileByUuid(String uuid) {

        for(Profile profile : profileSet) {
            if(profile.getUuid().equalsIgnoreCase(uuid)){
                return profile;
            }
        }
        return null;
    }

    /**
     * create profile for a new player
     * @param player
     */
    public void createProfile(Player player) {

        file = new File(Practice.getInstance().getDataFolder() + "/Profile", player.getUniqueId().toString() + ".yml");

        if(!file.exists()) {

        	file = new File(Practice.getInstance().getDataFolder() + "/Profile", player.getUniqueId().toString() + ".yml");

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            config.createSection("match");
            config.createSection("token");
            config.createSection("kill");
            config.createSection("death");
            config.createSection("spectator-message");
            config.createSection("duel-request");
            
            config.createSection("ranked.played");
            config.createSection("ranked.won");
            
            config.createSection("unranked.played");
            config.createSection("unranked.won");
            
            config.createSection("premium-game.played");
            config.createSection("premium-game.won");
            
            if(GameTypeManager.getGames().size() > 0) {
                for(GameType game : GameTypeManager.getGames()) {
                	
                	if(game.isRank()) {
                		config.createSection("game." + game.getName());
                		config.set("game." + game.getName(), 1600);
                	}
                }
            }
            
            config.set("match", 0);
            config.set("token", 0);
            config.set("kill", 0);
            config.set("death", 0);
            config.set("spectator-message", true);
            config.set("duel-request", true);
            
            config.set("ranked.played", 0);
            config.set("ranked.won", 0);
            
            config.set("unranked.played", 0);
            config.set("unranked.won", 0);
            
            config.set("premium-game.played", 0);
            config.set("premium-game.won", 0);
            
            try {
                config.save(file);
            } catch (IOException e) {
                Practice.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PRACTICE] ERROR WHILE CREATING PROFILE FOR " + ChatColor.YELLOW + player.getName());
            }

            loadProfile(player);

        } else {
            loadProfile(player);
        }
    }

    /**
     * load the profile of an online player
     * @param player
     */
    public void loadProfile(final Player player) {

        file = new File(Practice.getInstance().getDataFolder() + "/Profile", player.getUniqueId().toString() + ".yml");

        if(file.exists()) {

            if(getProfileByPlayer(player) != null) {
                /*
                Kicked player because profile already exists

                TODO: remove profile from list
                 */

            	profileSet.remove(getProfileByPlayer(player));
            	
            	  Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
                  	public void run() {
                  		 player.kickPlayer(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "[SERVER]" + ChatColor.RED + " Force kicked because profile already exists, you may join back!");
                  	}
                  }, 5);
            	
                 return;
            }

            Profile profile = new Profile(player.getUniqueId().toString());

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            int premium = config.getInt("match");
            int token = config.getInt("token");
            int kill = config.getInt("kill");
            int death = config.getInt("death");
            
            int rankPlayed = config.getInt("ranked.played");
            int rankWon = config.getInt("ranked.won");
            
            int unrankedPlayed = config.getInt("unranked.played");
            int unrankedWon = config.getInt("unranked.won");
            
            int premiumPlayed = config.getInt("premium-game.played");
            int premiumWon = config.getInt("premium-game.won");
            
            boolean spectatorMessage = config.getBoolean("spectator-message");
            boolean duelRequest = config.getBoolean("duel-request");
            
            profile.setToken(token);
            profile.setPremium(premium);
            profile.setKill(kill);
            profile.setDeath(death);
            profile.setSpectatorMessage(spectatorMessage);
            profile.setAllowDuel(duelRequest);
            
            profile.setRankedPlayed(rankPlayed);
            profile.setRankedWon(rankWon);
            
            profile.setUnrankedPlayed(unrankedPlayed);
            profile.setUnrankedWon(unrankedWon);
            
            profile.setPremiumPlayed(premiumPlayed);
            profile.setPremiumWon(premiumWon);
            
            if(GameTypeManager.getGames().size() > 0) {
                
             	for(GameType game : GameTypeManager.getGames()) {
             		
             		if(config.contains("game." + game.getName())) {
                 		
             			if(game.isRank()) {
                			
             				Elo elo = new Elo(config.getInt("game." + game.getName()), game);
                 			
                 			profile.getElo().add(elo);
             			} else {
             				config.set("game." + game.getName(), null);
             			}
                 	
             		} else {
             			
             			if(game.isRank()) {
                      		
             				config.createSection("game." + game.getName());
                     		
                     		config.set("game." + game.getName(), 1600);
                     		
                     		Elo elo = new Elo(config.getInt("game." + game.getName()), game);
                 			
                 			profile.getElo().add(elo);
             			}
                 	}
             		
             	}
             	
             }
            
            try {
				config.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            profile.setState(PlayerState.LOBBY);

            profileSet.add(profile);

            player.sendMessage(ChatColor.GREEN + "Profile has been loaded! ");

        }
    }

    /**
     * load the profile of onine/offline players
     * can be used for adding premium matches if the player is offline
     * @param OfflinePlayer
     */
    public void loadProfile(OfflinePlayer player) {
    	
    	file = new File(Practice.getInstance().getDataFolder() + "/Profile", player.getUniqueId().toString() + ".yml");

    	 if(file.exists()) {

             Profile profile = new Profile(player.getUniqueId().toString());

             YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

             int premium = config.getInt("match");
             int token = config.getInt("token");
             int kill = config.getInt("kill");
             int death = config.getInt("death");
             
             int rankPlayed = config.getInt("ranked.played");
             int rankWon = config.getInt("ranked.won");
             
             int unrankedPlayed = config.getInt("unranked.played");
             int unrankedWon = config.getInt("unranked.won");
             
             int premiumPlayed = config.getInt("premium-game.played");
             int premiumWon = config.getInt("premium-game.won");
             
             boolean spectatorMessage = config.getBoolean("spectator-message");
             boolean duelRequest = config.getBoolean("duel-request");
             
             profile.setToken(token);
             profile.setPremium(premium);
             profile.setKill(kill);
             profile.setDeath(death);
             profile.setSpectatorMessage(spectatorMessage);
             profile.setAllowDuel(duelRequest);
             
             profile.setRankedPlayed(rankPlayed);
             profile.setRankedWon(rankWon);
             
             profile.setUnrankedPlayed(unrankedPlayed);
             profile.setUnrankedWon(unrankedWon);
             
             profile.setPremiumPlayed(premiumPlayed);
             profile.setPremiumWon(premiumWon);
             
             if(GameTypeManager.getGames().size() > 0) {
                 
             	for(GameType game : GameTypeManager.getGames()) {
             		
             		if(config.contains("game." + game.getName())) {
                 		
             			if(game.isRank()) {
                			
             				Elo elo = new Elo(config.getInt("game." + game.getName()), game);
                 			
                 			profile.getElo().add(elo);
             			} else {
             				config.set("game." + game.getName(), null);
             			}
                 	
             		} else {
             			
             			if(game.isRank()) {
                      		
             				config.createSection("game." + game.getName());
                     		
                     		config.set("game." + game.getName(), 1600);
                     		
                     		Elo elo = new Elo(config.getInt("game." + game.getName()), game);
                 			
                 			profile.getElo().add(elo);
             			}
                 	}
             		
             	}
             	
             }
             
             profileSet.add(profile);

             try {
 				config.save(file);
 			} catch (IOException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
             
         }
    	
    }
    
    /**
     * save profile of an online player
     * @param player
     */
    public void saveProfile(Player player) {

    	 file = new File(Practice.getInstance().getDataFolder() + "/Profile", player.getUniqueId().toString() + ".yml");

    	 if(!file.exists()) {
    		 
    		 createProfile(player);
    		 
    		 try {
    			 profileSet.remove(getProfileByPlayer(player));
    		 } catch (Exception e) {
    			 Practice.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PRACTICE] ERROR WHILE SAVING (REMOVING FROM SET) PROFILE FOR " + ChatColor.YELLOW + player.getName());
    		 }
		} else {

			if (getProfileByPlayer(player) != null) {

				Profile profile = getProfileByPlayer(player);

				YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
				
				config.set("match", profile.getPremium());
				
				config.set("token", profile.getToken());
				
				config.set("kill", profile.getKill());	
				
				config.set("death", profile.getDeath());
				
				config.set("ranked.played", profile.getRankedPlayed());

				config.set("ranked.won", profile.getRankedWon());

				config.set("unranked.played", profile.getUnrankedPlayed());

				config.set("unranked.won", profile.getUnrankedWon());

				config.set("premium-game.played", profile.getPremiumPlayed());

				config.set("premium-game.won", profile.getPremiumWon());

				config.set("spectator-message", profile.isSpectatorMessage());
				
				config.set("duel-request", profile.isAllowDuel());
				
				if(config.contains("game")) {
					
					for(String str : config.getConfigurationSection("game").getKeys(false)) {
						
						GameType game = GameTypeManager.getManager().getGameType(str);
						
						if(config.contains("game." + str)) {
							
							if(game == null) {
								config.set("game." + str, null);
								continue;
							}
							
							if(game.isRank()) {
								config.set("game." + str, getPlayerElo(player, str));
							} else {
								config.set("game." + str, null);
							}
							
							if(config.getInt("game." + str) == - 1) {
								config.set("game." + str, null);
							}
							
						}
						
					}
					
				}
	
    			try {
					config.save(file);
				} catch (IOException e) {
					Practice.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PRACTICE] ERROR WHILE SAVING PROFILE FOR " + ChatColor.YELLOW + player.getName());
				}
    			
    			profileSet.remove(profile);
    			
			} else {
				Practice.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PRACTICE] ERROR WHILE SAVING PROFILE FOR " + ChatColor.YELLOW + player.getName() + ChatColor.RED + " FILE EXISTS, BUT PROFILE WAS NOT LOADED");
			}

		}

	}

    /**
     * save profile of online/offline players
     * @param OfflinePlayer
     */
    public void saveProfile(OfflinePlayer player) {

		file = new File(Practice.getInstance().getDataFolder() + "/Profile", player.getUniqueId().toString() + ".yml");

		if (file.exists()) {
			if (getProfileByOfflinePlayer(player) != null) {

				Profile profile = getProfileByOfflinePlayer(player);

				YamlConfiguration config = YamlConfiguration
						.loadConfiguration(file);

				config.set("match", profile.getPremium());
				
				config.set("token", profile.getToken());
				
				config.set("kill", profile.getKill());	
				
				config.set("death", profile.getDeath());
				
				config.set("ranked.played", profile.getRankedPlayed());

				config.set("ranked.won", profile.getRankedWon());

				config.set("unranked.played", profile.getUnrankedPlayed());

				config.set("unranked.won", profile.getUnrankedWon());

				config.set("premium-game.played", profile.getPremiumPlayed());

				config.set("premium-game.won", profile.getPremiumWon());
				
				config.set("spectator-message", profile.isSpectatorMessage());
				
				config.set("duel-request", profile.isAllowDuel());
				
				if(config.contains("game")) {
					
					for(String str : config.getConfigurationSection("game").getKeys(false)) {
						
						GameType game = GameTypeManager.getManager().getGameType(str);
						
						if(config.contains("game." + str)) {
							
							if(game == null) {
								config.set("game." + str, null);
								continue;
							}
							
							if(game.isRank()) {
								config.set("game." + str, getOfflinePlayerElo(player, str));
							} else {
								config.set("game." + str, null);
							}
							
							if(config.getInt("game." + str) == - 1) {
								config.set("game." + str, null);
							}
							
						}
					}
				}
				
				try {
					config.save(file);
				} catch (IOException e) {
				Practice.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PRACTICE] ERROR WHILE SAVING PROFILE FOR " + ChatColor.YELLOW + player.getName());
				}

				profileSet.remove(profile);

				profile = null;

			}
		} else {
			Practice.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PRACTICE] ERROR WHILE SAVING PROFILE FOR " + ChatColor.YELLOW + player.getName() + ChatColor.RED + " COULD NOT FIND PROFILE");
		}

	}
    
    /**
     * get online players elo
     * @param player
     * @param game
     * @return
     */
    public int getPlayerElo(Player player, String game) {
    	
    	Profile profile = getProfileByPlayer(player);
    	
    	for(Elo elo : profile.getElo()) {
    		if(elo.getGame().getName().equalsIgnoreCase(game)) {
    			return elo.getElo();
    		}
    	}
    	
    	return -1;
    }
    
    public int getOfflinePlayerElo(OfflinePlayer player, String game) {
    	
    	loadProfile(player);
    	
    	Profile profile = getProfileByOfflinePlayer(player);
    	
    	for(Elo elo : profile.getElo()) {
    		if(elo.getGame().getName().equalsIgnoreCase(game)) {
    			return elo.getElo();
    		}
    	}
    	return -1;
    }
}
