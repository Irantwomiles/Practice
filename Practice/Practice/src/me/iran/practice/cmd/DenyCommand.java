package me.iran.practice.cmd;

import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DenyCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("deny")) {
			
			if(args.length < 1) {
				player.sendMessage(ChatColor.GRAY + "/deny <Player>");
				return true;
			}
			
			Profile playerProfile = ProfileManager.getManager().getProfileByPlayer(player);
			
			if(playerProfile.getState() != PlayerState.LOBBY) {
				player.sendMessage(ChatColor.RED + "You can't do that while not in the lobby");
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null) {
				player.sendMessage(ChatColor.RED + "Couldn't find that player");
				return true;
			}
			
			if(!playerProfile.getDuelRequest().containsKey(target.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "That person has not dueled you");
				return true;
			}
			
			playerProfile.getDuelRequest().remove(target.getUniqueId());
			
			player.sendMessage(ChatColor.RED + "You have denied that duel request!");
			target.sendMessage(ChatColor.YELLOW + player.getName() + ChatColor.RED + " has denied your duel request!");
		}
		
		return true;
	}
	
}
