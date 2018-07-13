package me.iran.practice.cmd;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.duel.team.SendTeamDuel;
import me.iran.practice.duel.team.TeamDuelManager;
import me.iran.practice.enums.PartyState;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.party.Party;
import me.iran.practice.party.PartyManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.utils.PlayerInventories;

public class PartyCommands implements CommandExecutor {

	@Getter
	private static HashMap<Party, Party> duelRequest = new HashMap<>();
	
	private PlayerInventories inv = new PlayerInventories();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		Profile playerProfile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(cmd.getName().equalsIgnoreCase("party")) {
			
			if(args.length < 1) {
				player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------------------------");
				player.sendMessage(ChatColor.YELLOW + "• /party invite <Player>");
				player.sendMessage("");
				player.sendMessage(ChatColor.YELLOW + "• /party kick <Player>");
				player.sendMessage("");
				player.sendMessage(ChatColor.YELLOW + "• /party join <Party>");
				player.sendMessage("");
				player.sendMessage(ChatColor.YELLOW + "• /party duel <Party>");
				player.sendMessage("");
				player.sendMessage(ChatColor.YELLOW + "• /party accept <Party>");
				player.sendMessage("");
				player.sendMessage(ChatColor.YELLOW + "• /party deny <Party>");
				player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------------------------");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("invite")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.YELLOW + "• /party invite <Player>");
					return true;
				}

				Player target = Bukkit.getPlayer(args[1]);
				
				if(playerProfile.getState() != PlayerState.LOBBY) {
					player.sendMessage(ChatColor.RED + "Must be in the lobby to invite someone");
					return true;
				}
				
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Couldn't find that player");
					return true;
				}
				
				if(target == player) {
					return true;
				}
				
				Profile targetProfile = ProfileManager.getManager().getProfileByPlayer(target);
				
				if(targetProfile.getState() != PlayerState.LOBBY) {
					player.sendMessage(ChatColor.RED + "That player is currently busy");
					return true;
				}
				
				PartyManager.getManager().invite(player, target);
			}
			
			if(args[0].equalsIgnoreCase("join")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.YELLOW + "• /party join <Party>");
					return true;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				
				if(playerProfile.getState() != PlayerState.LOBBY) {
					player.sendMessage(ChatColor.RED + "Must be in the lobby to join a party");
					return true;
				}
				
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Couldn't find that player");
					return true;
				}
				
				PartyManager.getManager().join(target.getUniqueId(), player);
			}
			
			if(args[0].equalsIgnoreCase("kick")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.YELLOW + "• /party kick <Player>");
					return true;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Couldn't find that player");
					return true;
				}
				
				PartyManager.getManager().kick(player, target);
			}
			
			if(args[0].equalsIgnoreCase("leader")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.YELLOW + "• /party leader <Player>");
					return true;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Couldn't find that player");
					return true;
				}
				
				PartyManager.getManager().leader(player, target);
			}
			
			if(args[0].equalsIgnoreCase("duel")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.YELLOW + "• /party duel <Party>");
					return true;
				}
				
				if(PartyManager.getManager().getPartyByLeader(player.getUniqueId()) == null) {
					player.sendMessage(ChatColor.RED + "You must be in a party or a party leader to perform this command!");
					return true;
				}
				
				Party playerParty = PartyManager.getManager().getPartyByLeader(player.getUniqueId());
				
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Couldn't find that party");
					return true;
				}
				
				if(PartyManager.getManager().getPartyByLeader(target.getUniqueId()) == null) {
					player.sendMessage(ChatColor.RED + "Could not find that players party!");
					return true;
				}
				
				Party targetParty = PartyManager.getManager().getPartyByLeader(target.getUniqueId());
				
				if(targetParty.getLeader() == playerParty.getLeader()) {
					player.sendMessage(ChatColor.RED + "Can't duel yourself!");
					return true;
				}
				
				inv.gameTypes(player);
				
				duelRequest.put(playerParty, targetParty);
				
			}
			
			if(args[0].equalsIgnoreCase("accept")) {
				
				if(args.length < 2) {
					player.sendMessage(ChatColor.YELLOW + "• /party accept <Party>");
					return true;
				}
				
				if(PartyManager.getManager().getPartyByLeader(player.getUniqueId()) == null) {
					player.sendMessage(ChatColor.RED + "You must be in a party or a party leader to perform this command!");
					return true;
				}
				
				Party playerParty = PartyManager.getManager().getPartyByLeader(player.getUniqueId());
				
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Couldn't find that party");
					return true;
				}
				
				if(PartyManager.getManager().getPartyByLeader(target.getUniqueId()) == null) {
					player.sendMessage(ChatColor.RED + "Could not find that players party!");
					return true;
				}
				
				Party targetParty = PartyManager.getManager().getPartyByLeader(target.getUniqueId());
				
				if(playerParty.getState() != PartyState.LOBBY) {
					player.sendMessage(ChatColor.RED + "You can only accept duels while in the lobby!");
					return true;
				}
				
				if(targetParty.getState() != PartyState.LOBBY) {
					player.sendMessage(ChatColor.RED + "That party is not in the lobby and can't duel anyone right now!");
					return true;
				}
				
				if(playerParty.getRequests().containsKey(targetParty)) {
					
					SendTeamDuel request = playerParty.getRequests().get(targetParty);
					
					TeamDuelManager.getManager().createTeamDuel(playerParty, targetParty, request.getGame(), ArenaManager.getManager().randomArena());
					
					playerParty.getRequests().remove(targetParty);
					
				} else {
					player.sendMessage(ChatColor.RED + "That party has not dueled you!");
				}
			}
		}
		
		return true;
	}
	
}
