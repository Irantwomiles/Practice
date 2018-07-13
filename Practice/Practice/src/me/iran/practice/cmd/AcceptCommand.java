package me.iran.practice.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.iran.practice.arena.ArenaManager;
import me.iran.practice.duel.SendDuel;
import me.iran.practice.duel.SoloDuelManager;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.party.PartyManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import net.md_5.bungee.api.ChatColor;

public class AcceptCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("accept")) {
			
			if(args.length < 1) {
				player.sendMessage(ChatColor.GRAY + "/accept <Player>");
				return true;
			}
			
			
			Profile playerProfile = ProfileManager.getManager().getProfileByPlayer(player);
			
			if(playerProfile.getState() != PlayerState.LOBBY) {
				player.sendMessage(ChatColor.RED + "You can't do that while not in the lobby");
				return true;
			}
			
			if(PartyManager.getManager().isPlayerInParty(player.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "Can't do that while in a party!");
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null) {
				player.sendMessage(ChatColor.RED + "Couldn't find that player");
				return true;
			}
			
			Profile targetProfile = ProfileManager.getManager().getProfileByPlayer(target);
			
			if(targetProfile.getState() != PlayerState.LOBBY) {
				player.sendMessage(ChatColor.RED + "That player is currently busy");
				return true;
			}
			
			if(!playerProfile.getDuelRequest().containsKey(target.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "That person has not dueled you");
				return true;
			}
			
			if(PartyManager.getManager().isPlayerInParty(target.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "That player is in a party and can no longer be in a duel!");
				return true;
			}
			
			SendDuel request = playerProfile.getDuelRequest().get(target.getUniqueId());

			SoloDuelManager.getManager().createSoloDuel(
					player,
					target,
					request.getGame(), ArenaManager.getManager().randomArena());
			
			playerProfile.getDuelRequest().remove(target.getUniqueId());
			
		}
		
		return true;
	}

}
