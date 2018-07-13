package me.iran.practice.party.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.iran.practice.Practice;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.cmd.PartyCommands;
import me.iran.practice.duel.team.SendTeamDuel;
import me.iran.practice.enums.PartyState;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.gametype.GameType;
import me.iran.practice.gametype.GameTypeManager;
import me.iran.practice.party.Party;
import me.iran.practice.party.PartyManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.utils.PlayerInventories;
import me.iran.practice.utils.PlayerItems;
import me.iran.practice.utils.PlayerQueues;
import net.md_5.bungee.api.ChatColor;

public class PartyClickListeners implements Listener {

	PlayerQueues queue = new PlayerQueues();
	
	PlayerInventories inv = new PlayerInventories();
	
	PlayerItems items = new PlayerItems();
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		
		Player player = (Player) event.getWhoClicked();
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(profile.getState() == PlayerState.LOBBY || profile.getState() == PlayerState.SPECTATOR || profile.getState() == PlayerState.IN_QUEUE) {
			event.setCancelled(true);
		}
		
		if(event.getClickedInventory() == null) {
			return;
		}
		
		if(event.getClickedInventory().getTitle() == null) {
			return;
		}
		
		if(event.getCurrentItem() == null) {
			return;
		}
		
		if(!event.getCurrentItem().hasItemMeta()) {
			return;
		}
		
		if(event.getCurrentItem().getItemMeta().getDisplayName() == null) {
			return;
		}
		
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.party.duel")))) {
			
			if(PartyManager.getManager().getPartyByLeader(player.getUniqueId()) == null) {
				return;
			}
			
			if(!PartyCommands.getDuelRequest().containsKey(PartyManager.getManager().getPartyByLeader(player.getUniqueId()))) {
				return;
			}
			
			Party party = PartyManager.getManager().getPartyByLeader(player.getUniqueId());
			
			if(party.getState() == PartyState.LOBBY) {
				
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				GameType game = GameTypeManager.getManager().getGameType(name);

				Party target = PartyCommands.getDuelRequest().get(party);
				
				if(target == null) {
					player.sendMessage(ChatColor.RED + "That party is no available");
					return;
				}
				
				if(target.getRequests().containsKey(party)) {
					player.sendMessage(ChatColor.RED + "You have already dueled this party");
					player.closeInventory();
					return;
				}
				
				if(ArenaManager.getAvailableArenas().size() < 1) {
					player.sendMessage(ChatColor.RED + "Sorry, all of the arenas are busy right now!");
					return;
				}

				target.getRequests().put(party, new SendTeamDuel(party, target, game));
				
				player.closeInventory();
			}
			
			event.setCancelled(true);
		}

		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.party.queue")))) {
			
			if(PartyManager.getManager().isPlayerInParty(player.getUniqueId()) && PartyManager.getManager().isAPartyLeader(player.getUniqueId())) {
				
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				if(name.equalsIgnoreCase("Ranked Duo Queue")) {
					
				} else if(name.equalsIgnoreCase("UnRanked Duo Queue")) {
					
					inv.unrankedInventory(player);
					
				} else if(name.equalsIgnoreCase("Premium Duo Queue")) {
					
				}
				
			} else {
				player.closeInventory();
			}
			
		}
	
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.unranked")))) {
			
			if(PartyManager.getManager().isPlayerInParty(player.getUniqueId()) && PartyManager.getManager().isAPartyLeader(player.getUniqueId())) {
				
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				GameType game = GameTypeManager.getManager().getGameType(name);
				
				if(game == null) {
					player.closeInventory();
					return;
				}
				
				queue.joinPartyUnranked(PartyManager.getManager().getPartyByLeader(player.getUniqueId()), game);
				
				items.unrankedPartyQueueItems(player);
				
			} else {
				player.closeInventory();
			}
			
		}
		
	}
	
}
