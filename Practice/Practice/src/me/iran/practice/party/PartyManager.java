package me.iran.practice.party;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.iran.practice.Practice;
import me.iran.practice.enums.PartyState;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.party.listeners.PartyCreate;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.utils.PlayerItems;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PartyManager {

	private PlayerItems items = new PlayerItems();
	
	private static ArrayList<Party> parties = new ArrayList<>();
	
    private static PartyManager pm;

	private PartyManager() {}

	public static PartyManager getManager() {
		if (pm == null)
			pm = new PartyManager();

		return pm;
	}
	
	public void createParty(Player player) {
		
		if(doesPartyExist(player.getUniqueId())) {
			return;
		}

		Party party = new Party(player.getUniqueId());
		
		parties.add(party);
		
		Bukkit.getServer().getPluginManager().callEvent(new PartyCreate(party));
		
		player.sendMessage(ChatColor.YELLOW + "You have created a Party! For more info do " + ChatColor.GRAY + "/party");
	}
	
	public void invite(Player leader, Player target) {
		
		if(!doesPartyExist(leader.getUniqueId())) {
			leader.sendMessage(ChatColor.RED + "You must be the leader in a party to invite people!");
			return;
		}
		
		Party party = getPartyByLeader(leader.getUniqueId());
		
		if(party.getState() != PartyState.LOBBY) {
			leader.sendMessage(ChatColor.RED + "Your party is currently busy!");
			return;
		}
		
		if(!party.getInvites().contains(target.getUniqueId())) {
			party.getInvites().add(target.getUniqueId());
			
			leader.sendMessage(ChatColor.YELLOW + "Party invite sent to " + ChatColor.GRAY + target.getName());
			
			TextComponent leaderName = new TextComponent(leader.getName());
			leaderName.setColor(ChatColor.GRAY);
			
			TextComponent message = new TextComponent(" has invited you to their party");
			message.setColor(ChatColor.YELLOW);
			
			TextComponent join = new TextComponent(" [Join]");
			join.setColor(ChatColor.GREEN);
			join.setBold(true);
			join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party join " + leader.getName()));
			
			leaderName.addExtra(message);
			leaderName.addExtra(join);
			
			target.spigot().sendMessage(leaderName);
		} else {
			leader.sendMessage(ChatColor.RED + "You have already invited that player!");
		}
		
	}
	
	public void kick(Player leader, Player target) {
		
		if(!doesPartyExist(leader.getUniqueId())) {
			leader.sendMessage(ChatColor.RED + "You must be the leader in a party to kick people!");
			return;
		}
		
		Party party = getPartyByLeader(leader.getUniqueId());
		
		if(party.getState() != PartyState.LOBBY) {
			leader.sendMessage(ChatColor.RED + "Your party is currently busy!");
			return;
		}
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(target);
		
		if(leader.getUniqueId() == target.getUniqueId()) {
			return;
		}
		
		if(profile.getState() != PlayerState.LOBBY) {
			leader.sendMessage(ChatColor.RED + "Can't do that while in that player is in " + profile.getState().toString() + " state!");
		}
		
		if(party.getMembers().contains(target.getUniqueId())) {
			
			sendPartyMessage(party, ChatColor.YELLOW + target.getName() + ChatColor.RED + " has been kicked from the party!");
		
			party.getMembers().remove(target.getUniqueId());
			
			Practice.getInstance().teleportSpawn(target);
			
		} else {
			leader.sendMessage(ChatColor.RED + "That player is not in your party!");
		}
		
	}
	
	public void disband(Player leader) {
		
		if(getPartyByPlayer(leader.getUniqueId()) == null) {
			return;
		}
		
		Party party = getPartyByLeader(leader.getUniqueId());
		
		//if(party.getState() == PartyState.LOBBY) {
			
			for(UUID uuid : party.getMembers()) {
				
				Player p = Bukkit.getPlayer(uuid);
				
				if(p == null) {
					continue;
				}
				
				Profile profile = ProfileManager.getManager().getProfileByPlayer(p);
				
				if(profile.getState() != PlayerState.IN_GAME) {
					items.spawnItems(p);
				}
				
				p.sendMessage(ChatColor.RED + "Party has been disbanded!");
				
			}
			
			party.getMembers().clear();
			
			party.getInvites().clear();
			
			parties.remove(party);
			
		//}
	}
	
	public void join(UUID leader, Player player) {
		
		if(!doesPartyExist(leader)) {
			player.sendMessage(ChatColor.RED + "Can't find that party!");
			return;
		}
		
		if(isPlayerInParty(player.getUniqueId())) {
			player.sendMessage(ChatColor.RED + "You are already in a party!");
			return;
		}
		
		Party party = getPartyByLeader(leader);
		
		if(party.getState() != PartyState.LOBBY) {
			player.sendMessage(ChatColor.RED + "Can't join that party, they are busy!");
			return;
		}
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(profile.getState() != PlayerState.LOBBY) {
			player.sendMessage(ChatColor.RED + "Can't do that while in the " + profile.getState().toString() + " state!");
			return;
		}
		
		if(!party.getInvites().contains(player.getUniqueId()) && !party.isOpen()) {
			player.sendMessage(ChatColor.RED + "You have not been invited to that party!");
			return;
		}
		
		//Open party
		if(party.isOpen()) {
			
			if(!party.getMembers().contains(player.getUniqueId())) {
				sendPartyMessage(party, ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " has joined the party!");
			
				party.getMembers().add(player.getUniqueId());
				
				items.partyItemsMember(player);
				
			} else {
				player.sendMessage(ChatColor.RED + "You are already in that party!");
			}
			
		} else {
			
			if(party.getInvites().contains(player.getUniqueId())) {
				
				if(!party.getMembers().contains(player.getUniqueId())) {
					sendPartyMessage(party, ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " has joined the party!");
				
					party.getMembers().add(player.getUniqueId());
					party.getInvites().remove(player.getUniqueId());
					
					items.partyItemsMember(player);
					
				} else {
					player.sendMessage(ChatColor.RED + "You are already in that party!");
				}
				
			} else {
				player.sendMessage(ChatColor.RED + "You have not been invited to that party!");
			}
			
		}
		
	}
	
	public void leave(UUID player) {
		
		if(!isPlayerInParty(player)) {
			return;
		}
		
		Party party = getPartyByPlayer(player);
		
		/*if(party.getState() != PartyState.LOBBY) {
			Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "Can't leave your party while in the " + party.getState().toString() + " state");
			return;
		}*/
		
		if(party.getLeader() == player) {
			disband(Bukkit.getPlayer(player));
		} else {
			
			sendPartyMessage(party, ChatColor.YELLOW + Bukkit.getPlayer(player).getName() + ChatColor.RED + " has left the party");
			
			party.getMembers().remove(player);
			
			Practice.getInstance().teleportSpawn(Bukkit.getPlayer(player));
		}
		
		
	}
	
	public void leader(Player leader, Player target) {
		
		if(getPartyByLeader(leader.getUniqueId()) == null) {
			leader.sendMessage(ChatColor.RED + "You're not a party leader!");
			return;
		}
		
		Party party = getPartyByLeader(leader.getUniqueId());
		
		if(party.getMembers().contains(target.getUniqueId())) {
			
			party.setLeader(target.getUniqueId());
			
			items.partyItemsLeader(target);
			items.partyItemsMember(leader);
			
			sendPartyMessage(party, ChatColor.LIGHT_PURPLE + target.getName() + " is now the party leader");
		}
		
	}

	@SuppressWarnings("deprecation")
	public void sendPartyMessage(Party party, String message) {
		
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(party.getMembers().contains(p.getUniqueId())) {
				p.sendMessage(message);
			}
		}
		
	}
	
	public Party getPartyByLeader(UUID leader) {
		for(Party party : parties) {
			if(party.getLeader() == leader) {
				return party;
			}
		}
		return null;
	}

	public boolean isPlayerInParty(UUID player) {
		
		for(Party party : parties) {
			if(party.getMembers().contains(player)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isAPartyLeader(UUID player) {
		for(Party party : parties) {
			if(party.getLeader() == player) {
				return true;
			}
		}
		
		return false;
	}
	
	public Party getPartyByPlayer(UUID player) {
		
		for(Party party : parties) {
			if(party.getMembers().contains(player)) {
				return party;
			}
		}
		
		return null;
	}
	
	public Party getPartyById(int id) {
		for(Party party : parties) {
			if(Party.getId() == id) {
				return party;
			}
		}
		
		return null;
	}
	
	public boolean doesPartyExist(UUID leader) {
		
		for(Party party : parties) {
			if(party.getLeader() == leader) {
				return true;
			}
		}
		
		return false;
		
	}
	
}
