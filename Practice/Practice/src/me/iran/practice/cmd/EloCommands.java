package me.iran.practice.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.iran.practice.profile.Elo;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import net.md_5.bungee.api.ChatColor;

public class EloCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) return true;
		
		Player player = (Player) sender;
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(cmd.getName().equalsIgnoreCase("elo")) {
			
			if(args.length < 1) {
				
				player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------");
				
				int global = 0;
				
				for(Elo elo : profile.getElo()) {
					player.sendMessage(ChatColor.YELLOW + elo.getGame().getName() + ChatColor.GRAY + ": " + elo.getElo());
					
					global += elo.getElo();
				}
				
				if(profile.getElo().size() == 0) {
					global = 0;
				} else {
					global = (global/profile.getElo().size());
				}
				
				player.sendMessage("");
				player.sendMessage(ChatColor.GOLD + "Global Elo" + ChatColor.GRAY + ": " + global);
				
				player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------");
				
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reset")) {
				player.sendMessage(ChatColor.GOLD + "Right-Click the " + ChatColor.GRAY + "Options" + ChatColor.GOLD + " item in your hotbar in the lobby!");
			}
			
		}
		
		return true;
	}

}
