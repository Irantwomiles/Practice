package me.iran.practice.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.iran.practice.Practice;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import net.md_5.bungee.api.ChatColor;

public class EditCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("edit")) {
			
			if(!player.hasPermission("practice.edit")) {
				return true;
			}
			
			Profile profile = ProfileManager.getManager().getProfileByPlayer(player);

			if(profile.getState() == PlayerState.LOBBY) {
				
				profile.setState(PlayerState.EDIT);
				
				player.getInventory().clear();
				
				player.sendMessage(ChatColor.GRAY + "You are now in Edit Mode");
				
			} else if(profile.getState() == PlayerState.EDIT) {
				
				profile.setState(PlayerState.LOBBY);
				
				player.getInventory().clear();
				
				Practice.getInstance().teleportSpawn(player);
				
				player.sendMessage(ChatColor.GRAY + "You have left Edit Mode");
				
			} else {
				player.sendMessage(ChatColor.RED + "You can't do this while in " + profile.getState().toString());
			}
			
		}
		
		return true;
	}
	
}
