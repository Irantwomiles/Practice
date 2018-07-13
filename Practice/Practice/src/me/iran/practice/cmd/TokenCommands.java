package me.iran.practice.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.iran.practice.Practice;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;

public class TokenCommands implements CommandExecutor {
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player) {
			
			Player player = (Player) sender;
			
			Profile playerProfile = ProfileManager.getManager().getProfileByPlayer(player);
			
			if(cmd.getName().equalsIgnoreCase("token")) {
				
				if(args.length < 1) {
					player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------------------------");
					player.sendMessage(ChatColor.GRAY + "• /token give <player> <amount>");
					player.sendMessage(ChatColor.GRAY + " • This will take Tokens away from you and gives it to the <player>.");
					player.sendMessage("");
					player.sendMessage(ChatColor.GRAY + " • /token set <player> <amount>");
					player.sendMessage(ChatColor.GRAY + " • This will override the <player>'s current token count to what you set.");
					player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------------------------");
					return true;
				}
				
				if(args[0].equalsIgnoreCase("give")) {
					
					if(args.length < 3) {
						player.sendMessage(ChatColor.GRAY + "/token give <player> <amount>");
						return true;
					}
					
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
					
					if(target == null) {
						player.sendMessage(ChatColor.RED + "Couldn't find that player");
						return true;
					}
					
					//If player is online
					
					if(target.isOnline()) {
						
						Player t = Bukkit.getPlayer(target.getName());
						
						Profile targetProfile = ProfileManager.getManager().getProfileByPlayer(t);
						
						try {
							
							int amount = Integer.parseInt(args[2]);
							
							if(amount <= 0) {
								player.sendMessage(ChatColor.RED + "Please enter a number greater than 0");
								return true;
							}
							
							if(amount <= playerProfile.getToken()) {
								targetProfile.setToken(targetProfile.getToken() + amount);
								playerProfile.setToken(playerProfile.getToken() - amount);
								
								t.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("sent-token").replace("%amount%", amount + "").replace("%sender%", player.getName())));
								player.sendMessage(ChatColor.GRAY + "You have sent " + amount + " tokens to " + t.getName());
							
							} else {
								player.sendMessage(ChatColor.RED + "You don't have enough Tokens to give to " + t.getName());
							}
							
						} catch (Exception e) {
							player.sendMessage(ChatColor.GRAY + "/token give <player> <amount>");
						}
						
					} else {
						
						Profile targetProfile = ProfileManager.getManager().getProfileByOfflinePlayer(target);
						
						if(targetProfile == null) {
							player.sendMessage(ChatColor.RED + "That player has never logged on before!");
							return true;
						}
						
						try {
							
							int amount = Integer.parseInt(args[2]);
							
							if(amount <= playerProfile.getToken()) {
								
								targetProfile.setToken(targetProfile.getToken() + amount);
								playerProfile.setToken(playerProfile.getToken() - amount);
								
								player.sendMessage(ChatColor.GRAY + "You have sent " + amount + " tokens to " + targetProfile.getName() + " [Offline]");
							
							} else {
								player.sendMessage(ChatColor.RED + "You don't have enough Tokens to give to " + targetProfile.getName());
							}
							
						} catch (Exception e) {
							player.sendMessage(ChatColor.GRAY + "/token give <player> <amount>");
						}
					}
				}
				
				if(args[0].equalsIgnoreCase("set")) {
					
					if(!player.hasPermission("token.admin")) {
						return true;
					}
					
					if(args.length < 3) {
						player.sendMessage(ChatColor.GRAY + "/token set <player> <amount>");
						return true;
					}
					
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
					
					if(target == null) {
						player.sendMessage(ChatColor.RED + "Couldn't find that player");
						return true;
					}
					
					if(target.isOnline()) {
						
						Player t = Bukkit.getPlayer(target.getName());
						
						Profile targetProfile = ProfileManager.getManager().getProfileByPlayer(t);
						
						try {
							
							int amount = Integer.parseInt(args[2]);
							
							if(amount < 0) {
								player.sendMessage(ChatColor.RED + "Please enter a number greater than or equal to 0");
								return true;
							}
							
							targetProfile.setToken(amount);
								
							player.sendMessage(ChatColor.GRAY + "You have set " + t.getName() + "'s Tokens to " + amount);
							
							
						} catch (Exception e) {
							player.sendMessage(ChatColor.GRAY + "/token set <player> <amount>");
						}
						
					} else {
						
						ProfileManager.getManager().loadProfile(target);
						
						Profile targetProfile = ProfileManager.getManager().getProfileByOfflinePlayer(target);
						
						if(targetProfile == null) {
							player.sendMessage(ChatColor.RED + "That player has never logged on before!");
							return true;
						}
						
						try {
							
							int amount = Integer.parseInt(args[2]);
							
							if(amount < 0) {
								player.sendMessage(ChatColor.RED + "Please enter a number greater than or equal to 0");
								return true;
							}
							
							targetProfile.setToken(amount);
								
							player.sendMessage(ChatColor.GRAY + "You have set " + targetProfile.getName() + "'s Tokens to " + amount + " [Offline]");
							
							ProfileManager.getManager().saveProfile(target);
							
						} catch (Exception e) {
							player.sendMessage(ChatColor.GRAY + "/token set <player> <amount>");
						}
					}
				}
				
			}
		} else {
			
			/*
			 * CONSOLE
			 */
			
			if(cmd.getName().equalsIgnoreCase("token")) {
				
				if(args.length < 1) {
					sender.sendMessage(ChatColor.RED + "[CONSOLE] /token set <player> <amount> (This will override the current token count to what you set it as)");
					sender.sendMessage(ChatColor.RED + "[CONSOLE] /token give <player> <amount> (This will add to the current token count)");
					return true;
				}
				
				if(args[0].equalsIgnoreCase("give")) {
					
					if(args.length < 3) {
						sender.sendMessage(ChatColor.GRAY + "/token give <player> <amount>");
						return true;
					}
					
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
					
					if(target == null) {
						sender.sendMessage(ChatColor.RED + "Couldn't find that player");
						return true;
					}
					
					//If player is online
					
					if(target.isOnline()) {
						
						Player t = Bukkit.getPlayer(target.getName());
						
						Profile targetProfile = ProfileManager.getManager().getProfileByPlayer(t);
						
						try {
							
							int amount = Integer.parseInt(args[2]);
							
							if(amount <= 0) {
								sender.sendMessage(ChatColor.RED + "Please enter a number greater than 0");
								return true;
							}
							
							targetProfile.setToken(targetProfile.getToken() + amount);
								
							t.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("sent-token").replace("%amount%", amount + "").replace("%sender%", "[CONSOLE]")));
							sender.sendMessage(ChatColor.GRAY + "You have sent " + amount + " tokens to " + t.getName());
							
							
						} catch (Exception e) {
							sender.sendMessage(ChatColor.GRAY + "/token give <player> <amount>");
						}
						
					} else {
						
						ProfileManager.getManager().loadProfile(target);
						
						Profile targetProfile = ProfileManager.getManager().getProfileByOfflinePlayer(target);
						
						if(targetProfile == null) {
							sender.sendMessage(ChatColor.RED + "That player has never logged on before!");
							return true;
						}
						
						try {
							
							int amount = Integer.parseInt(args[2]);
							
							if(amount < 1) {
								sender.sendMessage(ChatColor.RED + "Amount must be greater than 0");
								return true;
							}
							
							targetProfile.setToken(targetProfile.getToken() + amount);
								
							sender.sendMessage(ChatColor.GRAY + "You have sent " + amount + " tokens to " + targetProfile.getName() + " [Offline]");
							
							ProfileManager.getManager().saveProfile(target);
							
						} catch (Exception e) {
							sender.sendMessage(ChatColor.GRAY + "/token give <player> <amount>");
						}
					}
				}
				
				if(args[0].equalsIgnoreCase("set")) {
					
					if(args.length < 3) {
						sender.sendMessage(ChatColor.GRAY + "/token set <player> <amount>");
						return true;
					}
					
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
					
					if(target == null) {
						sender.sendMessage(ChatColor.RED + "Couldn't find that player");
						return true;
					}
					
					if(target.isOnline()) {
						
						Player t = Bukkit.getPlayer(target.getName());
						
						Profile targetProfile = ProfileManager.getManager().getProfileByPlayer(t);
						
						try {
							
							int amount = Integer.parseInt(args[2]);
							
							if(amount < 0) {
								sender.sendMessage(ChatColor.RED + "Please enter a number greater than or equal to 0");
								return true;
							}
							
							targetProfile.setToken(amount);
								
							sender.sendMessage(ChatColor.GRAY + "You have set " + t.getName() + "'s Tokens to " + amount);
							
							
						} catch (Exception e) {
							sender.sendMessage(ChatColor.GRAY + "/token set <player> <amount>");
						}
						
					} else {
						
						ProfileManager.getManager().loadProfile(target);
						
						Profile targetProfile = ProfileManager.getManager().getProfileByOfflinePlayer(target);
						
						if(targetProfile == null) {
							sender.sendMessage(ChatColor.RED + "That player has never logged on before!");
							return true;
						}
						
						try {
							
							int amount = Integer.parseInt(args[2]);
							
							if(amount < 0) {
								sender.sendMessage(ChatColor.RED + "Please enter a number greater than or equal to 0");
								return true;
							}
							
							targetProfile.setToken(amount);
								
							sender.sendMessage(ChatColor.GRAY + "You have set " + targetProfile.getName() + "'s Tokens to " + amount + " [Offline]");
							
							ProfileManager.getManager().saveProfile(target);
							
						} catch (Exception e) {
							sender.sendMessage(ChatColor.GRAY + "/token set <player> <amount>");
						}
					}
				}
				
			}
			
		}
		
		return true;
	}

}
