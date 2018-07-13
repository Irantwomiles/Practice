package me.iran.practice.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.iran.practice.enums.PlayerState;
import me.iran.practice.party.PartyManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.spectator.Spectator;
import net.md_5.bungee.api.ChatColor;

public class SpectateCommand implements CommandExecutor {

	private Spectator spec = new Spectator();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(cmd.getName().equalsIgnoreCase("spectate")) {
			
			if(args.length < 1) {
				player.sendMessage(ChatColor.YELLOW + "/spectate <Player>");
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null) {
				player.sendMessage(ChatColor.YELLOW + "Could not find that player");
				return true;
			}
			
			if(PartyManager.getManager().isPlayerInParty(player.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "Can't do that while in a party!");
				return true;
			}
			
			Profile targetProfile = ProfileManager.getManager().getProfileByPlayer(target);
			
			if(profile.getState() != PlayerState.LOBBY) {
				player.sendMessage(ChatColor.RED + "Can't do that while in " + profile.getState().toString());
				return true;
			}
			
			if(targetProfile.getState() == PlayerState.IN_GAME) {
				spec.setSpectator(player, target);
			} else if(targetProfile.getState() == PlayerState.IN_EVENT) {
				spec.setSpectator(player, target);
			} else {
				player.sendMessage(ChatColor.RED + "That player is not in a game at the moment!");
			}
			
		}
		
		return true;
	}
	
}
