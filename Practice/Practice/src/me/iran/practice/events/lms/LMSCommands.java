package me.iran.practice.events.lms;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.iran.practice.Practice;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.utils.PlayerInventories;
import net.md_5.bungee.api.ChatColor;

public class LMSCommands implements CommandExecutor {

	private PlayerInventories inv = new PlayerInventories();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(cmd.getName().equalsIgnoreCase("lms")) {
			
			if(args.length < 1) {
				player.sendMessage(ChatColor.YELLOW + "/lms create");
				player.sendMessage(ChatColor.YELLOW + "/lms end <id>");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("create")) {
				
				if(LMSManager.getLmsList().size() >= 9) {
					player.sendMessage(ChatColor.RED + "Please wait for a spot to open up!");
					return true;
				}
				
				if(profile.getState() != PlayerState.LOBBY) {
					player.sendMessage(ChatColor.RED + "Must be in the Lobby to do this command!");
					return true;
				}
				
				if(Practice.getCooldown() > System.currentTimeMillis()) {
					
					int cooldown = (int) ((Practice.getCooldown() - System.currentTimeMillis()) / 1000);
					
					player.sendMessage(ChatColor.RED + "Please wait " + cooldown + " seconds before starting an Event!");
					
					return true;
				}
				
				inv.createLMS(player);
			
			}
			
			if(args[0].equalsIgnoreCase("end")) {
				
				if(!player.hasPermission("practice.event.end")) {
					player.sendMessage(ChatColor.RED + "You don't have permission to do this command!");
					return true;
				}
				
				try {
					LMSManager.getManager().forceEnd(Integer.parseInt(args[1]));
				} catch(Exception e) {
					player.sendMessage(ChatColor.RED + "/lms end <id>");
				}
				
			}
			
			if(args[0].equalsIgnoreCase("leave")) {
				
				if(LMSManager.getManager().isPlayerInLMS(player)) {
					
					LMSManager.getManager().leave(player);
					
				}
				
			}
			
			if(args[0].equalsIgnoreCase("join")) {
				
				if(args.length < 2) {
					return true;
				}
				
				if(!LMSManager.getManager().isPlayerInLMS(player)) {
					
					if(LMSManager.getManager().getLMSByArena(args[1]) != null) {
						LMSManager.getManager().join(player, LMSManager.getManager().getLMSByArena(args[1]).getId());
					}
					
				}
				
			}
			
		}
		
		return true;
	}
	
}
