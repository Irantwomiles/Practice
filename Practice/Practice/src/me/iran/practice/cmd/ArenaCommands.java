package me.iran.practice.cmd;import java.util.HashMap;

import me.iran.practice.Practice;
import me.iran.practice.arena.Arena;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.enums.ArenaState;
import me.iran.practice.utils.PlayerItems;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommands implements CommandExecutor {

	static HashMap<String, Arena> editing = new HashMap<>();
	
	private PlayerItems items = new PlayerItems();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("arena")) {
			
			if(!player.hasPermission("arena.admin")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			
			if(args.length < 1) {
				player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------------------------");
				player.sendMessage(ChatColor.GOLD + "/arena create <ID>");
				player.sendMessage(ChatColor.GRAY + " • The ID should be the name of the arena and then a number after it ex) Snow1.");
				player.sendMessage(ChatColor.GRAY + " • You can then change the name of the arena to whatever you'd like ex) Snow.");
				player.sendMessage(ChatColor.GRAY + " • Players will only see the name and not the ID.");
				player.sendMessage("");
				player.sendMessage(ChatColor.GOLD + "/arena edit <ID>");
				player.sendMessage(ChatColor.GRAY + " • Make sure you use the ID and not the name.");
				player.sendMessage(ChatColor.GRAY + " • Use the items in your hotbar to edit the arena to your needs.");
				player.sendMessage("");
				player.sendMessage(ChatColor.GOLD + "/arena rename <ID> <Name>");
				player.sendMessage(ChatColor.GRAY + " • Make sure you use the ID and not the name.");
				player.sendMessage(ChatColor.GRAY + " • By renaming the arena you are not changing the ID.");
				player.sendMessage("");
				player.sendMessage(ChatColor.GOLD + "/arena delete <ID>");
				player.sendMessage(ChatColor.GRAY + " • Make sure you use the ID and not the name.");
				player.sendMessage(ChatColor.GRAY + " • This will delete the arena FOREVER, can NOT be undone!");
				player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------------------------");
				return true;
			}
			
			/*
			 * Create Command
			 */
			
			if(args[0].equalsIgnoreCase("create")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.GRAY + "You can view all of the commands by doing " + ChatColor.YELLOW + "/arena");
					return true;
				}
				
				try {
					ArenaManager.getManager().createArena(args[1], player);
				} catch (Exception e) {
					player.sendMessage(ChatColor.GRAY + "Looks like something is wrong try " + ChatColor.YELLOW + "/arena");
				}
				
			}
			
			/*
			 * Delete Command
			 */
			
			if(args[0].equalsIgnoreCase("delete")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.GRAY + "You can view all of the commands by doing " + ChatColor.YELLOW + "/arena");
					return true;
				}
				
				String id = args[1];
				
				if(!ArenaManager.getManager().doesArenaExist(id)) {
					player.sendMessage(ChatColor.RED + "That arena doesn't exists");
					return true;
				}
				
				ArenaManager.getManager().deleteArena(player, id);
				
			}
			
			/*
			 * Rename command
			 */
			
			if(args[0].equalsIgnoreCase("rename")) {
				
				if(args.length < 3) {
					player.sendMessage(ChatColor.GRAY + "You can view all of the commands by doing " + ChatColor.YELLOW + "/arena");
					return true;
				}
				
				String id = args[1];
				
				Arena arena = ArenaManager.getManager().getArenaById(id);
				
				if(arena == null) {
					player.sendMessage(ChatColor.RED + "Couldn't find that arena");
					return true;
				}
				
				String name = args[2];
				
				arena.setName(name);
				
				player.sendMessage(ChatColor.GRAY + "You have renamed arena ID:" + id + " to " + ChatColor.YELLOW + name);
				
			}
			
			/*
			 * Edit Command
			 */
			
			if(args[0].equalsIgnoreCase("edit")) {
				
				if(args.length < 2) {
					if(editing.containsKey(player.getName())) {
						player.sendMessage(ChatColor.RED + "Stopped editing arena " + editing.get(player.getName()).getId());
						editing.get(player.getName()).setState(ArenaState.OPEN);
					} else {
						player.sendMessage(ChatColor.GRAY + "You can view all of the commands by doing " + ChatColor.YELLOW + "/arena");
					}
					return true;
				}
				
				try {
					
					Arena arena = ArenaManager.getManager().getArenaById(args[1]);
					
					if(arena != null) {
						if(editing.containsKey(player.getName())) {
							player.sendMessage(ChatColor.RED + "Stopped editing arena " + editing.get(player.getName()).getId());
							editing.get(player.getName()).setState(ArenaState.OPEN);
							
							editing.put(player.getName(), arena);
							editing.get(player.getName()).setState(ArenaState.EDITOR);
							player.sendMessage(ChatColor.GOLD + "Started editing arena " + editing.get(player.getName()).getId());
							
							items.arenaEditing(player);
							
						} else {
							editing.put(player.getName(), arena);
							editing.get(player.getName()).setState(ArenaState.EDITOR);
							player.sendMessage(ChatColor.GOLD + "Started editing arena " + editing.get(player.getName()).getId());
						
							items.arenaEditing(player);
						}
					} else {
						player.sendMessage(ChatColor.RED + "That arena could not be found");
					}
	
				} catch (Exception e) {
					player.sendMessage(ChatColor.GRAY + "You can view all of the commands by doing " + ChatColor.YELLOW + "/arena");
				}
				
			}
		}
		
		return true;
	}
	
	public static HashMap<String, Arena> getEditing() {
		return editing;
	}
	
	
}
