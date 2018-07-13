package me.iran.practice.cmd;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import lombok.Getter;
import me.iran.practice.Practice;
import me.iran.practice.gametype.GameType;
import me.iran.practice.gametype.GameTypeManager;

public class GameTypeCommands implements CommandExecutor {

	@Getter
	private static HashMap<String, GameType> editor = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("gametype")) {

			if(!player.hasPermission("gametype.admin")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			
			if(args.length < 1) {
				player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------------------------");
				player.sendMessage(ChatColor.GOLD + "/gametype create <Name>");
				player.sendMessage(ChatColor.GRAY + " • To avoid errors the plugin will set the default display icon as a Diamond Sword.");
				player.sendMessage(ChatColor.GRAY + " • The items in your inventory and armor slots will be set by default to the kit, you can change this.");
				player.sendMessage("");
				player.sendMessage(ChatColor.GOLD + "/gametype defaultkit <Name>");
				player.sendMessage(ChatColor.GRAY + " • Use this command to set the default kit to what you'd like.");
				player.sendMessage(ChatColor.GRAY + " • Arrange your inventory in the way you want the kit to be set.");
				player.sendMessage("");
				player.sendMessage(ChatColor.GOLD + "/gametype seteditor <Name>");
				player.sendMessage(ChatColor.GRAY + " • This is the inventory players will see when creating a Kit.");
				player.sendMessage(ChatColor.GRAY + " • Arrange the inventory that pops up in the way you want everyone to see it.");
				player.sendMessage("");
				player.sendMessage(ChatColor.GOLD + "/gametype seteditorloc <Name>");
				player.sendMessage(ChatColor.GRAY + " • This is where the player will go to edit their Kit.");
				player.sendMessage("");
				player.sendMessage(ChatColor.GOLD + "/gametype seteditable <Name>");
				player.sendMessage(ChatColor.GRAY + " • You can choose to allow players to make Kits or not.");
				player.sendMessage(ChatColor.GRAY + " • By typing the command you will see a message that will tell you if its enabled/disabled.");
				player.sendMessage("");
				player.sendMessage(ChatColor.GOLD + "/gametype setdisplay <Name>");
				player.sendMessage(ChatColor.GRAY + " • This command will set the display icon of the game mode to the item in your hand.");
				player.sendMessage("");
				player.sendMessage(ChatColor.GOLD + "/gametype setranked <Name>");
				player.sendMessage(ChatColor.GRAY + " • This command will toggle between ranked or unranked.");
				player.sendMessage("");
				player.sendMessage(ChatColor.GOLD + "/gametype delete <Name>");
				player.sendMessage(ChatColor.RED + " • This command will delete the specified game mode.");
				player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------------------------");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("create")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/gametype create <Name>");
					return true;
				}
				
				String name = args[1];
				
				if(GameTypeManager.getManager().getGameType(name) != null) {
					player.sendMessage(ChatColor.RED + "That GameType already exists");
					return true;
				}
				
				GameTypeManager.getManager().createGameType(player, name);
				
			}
			
			if(args[0].equalsIgnoreCase("defaultkit")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/gametype defaultkit <Name>");
					return true;
				}
				
				String name = args[1];
				
				if(GameTypeManager.getManager().getGameType(name) == null) {
					player.sendMessage(ChatColor.RED + "Could not find that gametype");
					return true;
				}
				
				GameType game = GameTypeManager.getManager().getGameType(name);
				
				game.setArmor(player.getInventory().getArmorContents());
				game.setInv(player.getInventory().getContents());
				
				player.sendMessage(ChatColor.GREEN + "You have updated this gametype's default-kit");
				
			}
			
			if(args[0].equalsIgnoreCase("seteditor")) {

				if (args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/gametype seteditor <Name>");
					return true;
				}
				
				String name = args[1];
				
				if(GameTypeManager.getManager().getGameType(name) == null) {
					player.sendMessage(ChatColor.RED + "Could not find that gametype");
					return true;
				}
				
				GameType game = GameTypeManager.getManager().getGameType(name);
				
				Inventory inv = Bukkit.createInventory(player, 54, game.getName());
				
				inv.setContents(game.getChest());
				
				player.openInventory(inv);
				
				editor.put(player.getName(), game);
			}
			
			if(args[0].equalsIgnoreCase("seteditorloc")) {
				
				Practice.getInstance().getConfig().set("editor.x", player.getLocation().getX());
				Practice.getInstance().getConfig().set("editor.y", player.getLocation().getY());
				Practice.getInstance().getConfig().set("editor.z", player.getLocation().getZ());
				Practice.getInstance().getConfig().set("editor.pitch", player.getLocation().getPitch());
				Practice.getInstance().getConfig().set("editor.yaw", player.getLocation().getYaw());
				Practice.getInstance().getConfig().set("editor.world", player.getLocation().getWorld().getName());
				
				player.sendMessage(ChatColor.GREEN + "You have set this as the location of the editor");
				
			}
			
			if(args[0].equalsIgnoreCase("setdisplay")) {

				if (args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/gametype setdisplay <Name>");
					return true;
				}
				
				String name = args[1];
				
				if(GameTypeManager.getManager().getGameType(name) == null) {
					player.sendMessage(ChatColor.RED + "Could not find that gametype");
					return true;
				}
				
				GameType game = GameTypeManager.getManager().getGameType(name);

				if(player.getItemInHand() != null) {
					game.setDisplay(player.getItemInHand());
					player.sendMessage(ChatColor.GREEN + "Updated the display for " + game.getName());
				} else {
					player.sendMessage(ChatColor.RED + "You must be holding something in your hand");
				}
				
			}
			
			if(args[0].equalsIgnoreCase("seteditable")) {
				if (args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/gametype seteditable <Name>");
					return true;
				}
				
				String name = args[1];
				
				if(GameTypeManager.getManager().getGameType(name) == null) {
					player.sendMessage(ChatColor.RED + "Could not find that gametype");
					return true;
				}
				
				GameType game = GameTypeManager.getManager().getGameType(name);
				
				if(game.isEditable()) {
					game.setEditable(false);
					
					player.sendMessage(ChatColor.GRAY + "That GameType is not longer editable!");
				} else {
					game.setEditable(true);
					
					player.sendMessage(ChatColor.GRAY + "That GameType is now editable!");
				}
			}
			
			if(args[0].equalsIgnoreCase("delete")) {
				if (args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/gametype delete <Name>");
					return true;
				}
				
				GameTypeManager.getManager().deleteGameType(player, args[1]);
			}
			
			if(args[0].equalsIgnoreCase("setranked")) {
				if (args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "/gametype setranked <Name>");
					return true;
				}
				
				GameTypeManager.getManager().setRanked(player, args[1]);
			}
		}
		
		return true;
	}
}
