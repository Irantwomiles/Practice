package me.iran.practice.cmd;

import me.iran.practice.enums.PlayerState;
import me.iran.practice.party.PartyManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.utils.PlayerInventories;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.Getter;

public class DuelCommand implements CommandExecutor {

	private PlayerInventories inv = new PlayerInventories();
	
	@Getter
	private static HashMap<UUID, UUID> duelTarget = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		Profile senderProfile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(cmd.getName().equalsIgnoreCase("duel")) {
			
			if(args.length < 1) {
				player.sendMessage(ChatColor.GRAY + "/duel <Player>");
				return true;
			}
			
			if(senderProfile.getState() != PlayerState.LOBBY) {
				player.sendMessage(ChatColor.RED + "You can't duel people while in " + senderProfile.getState().toString() + " state");
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
			
			if(target == player) {
				player.sendMessage(ChatColor.RED + "Can't do that");
				return true;
			}
			
			if(PartyManager.getManager().isPlayerInParty(target.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "That player is in a party and can not accept duel requests!");
				return true;
			}
			
			Profile targetProfile = ProfileManager.getManager().getProfileByPlayer(target);

			if(!targetProfile.isAllowDuel()) {
				player.sendMessage(ChatColor.RED + "You can't duel " + target.getName() + " because they have their duel requests turned off");
				return true;
			}
			
			if(targetProfile.getState() != PlayerState.LOBBY) {
				player.sendMessage(ChatColor.RED + "You can't duel " + target.getName() + " because they are busy");
				return true;
			}
			
			inv.gameTypes(player);
			
			duelTarget.put(player.getUniqueId(), target.getUniqueId());
		}
		
		return true;
	}
}
