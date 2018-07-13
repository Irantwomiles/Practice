package me.iran.practice.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import lombok.Getter;
import me.iran.practice.Practice;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.party.Party;
import me.iran.practice.party.PartyManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.spectator.Spectator;
import me.iran.practice.utils.PlayerInventories;
import me.iran.practice.utils.PlayerItems;
import me.iran.practice.utils.PlayerQueues;

public class InteractListeners implements Listener {

	@Getter
	private static HashMap<String, Long> wait = new HashMap<>();
	
	private PlayerInventories inv = new PlayerInventories();
	
	private PlayerItems items = new PlayerItems();
	
	private PlayerQueues queue = new PlayerQueues();
	
	private Spectator spec = new Spectator();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();

		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if (event.getAction() == null) {
			return;
		}

		if (player.getItemInHand() == null) {
			return;
		}

		if (!player.getItemInHand().hasItemMeta()) {
			return;
		}

		if (player.getItemInHand().getItemMeta().getDisplayName() == null) {
			return;
		}
		
		if(profile.getState() == PlayerState.EDIT) {
			return;
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if(profile.getState() == PlayerState.LOBBY || profile.getState() == PlayerState.EDITOR) {
				
				if(wait.containsKey(player.getName()) && wait.get(player.getName()) > System.currentTimeMillis()) {
					return;
				} else {
					wait.put(player.getName(), System.currentTimeMillis() + (1000 + 2));
				}
				
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.queue.name")))) {
				
				if(queue.isPlayerInQueue(player)) {
					return;
				}
				
				inv.queue(player);
				
				return;
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.events.name")))) {
				
				if(queue.isPlayerInQueue(player)) {
					return;
				}
				
				inv.events(player);
				
				return;
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.kit-editor.name")))) {
				
				if(queue.isPlayerInQueue(player)) {
					return;
				}
				
				inv.kitEditorInventory(player);
				return;
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.party.name")))) {
				
				if(queue.isPlayerInQueue(player)) {
					return;
				}
				
				if(Practice.getInstance().getConfig().getBoolean("party-enabled")) {
					PartyManager.getManager().createParty(player);
				} else {
					player.sendMessage(ChatColor.YELLOW + "Currently disabled during beta testing!");
				}
				
				return;
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.unranked-queue.name")))) {
				if(queue.isPlayerInQueue(player)) {
					queue.leaveUnrankedQueue(player);
					profile.setState(PlayerState.LOBBY);
					items.spawnItems(player);
				}
				return;
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.ranked-queue.name")))) {
				if(queue.isPlayerInQueue(player)){
					queue.leaveRankedQueue(player);
					profile.setState(PlayerState.LOBBY);
					
					if(PlayerQueues.getIncrement().containsKey(player.getUniqueId())) {
						PlayerQueues.getIncrement().remove(player.getUniqueId());
					}
					
					items.spawnItems(player);
				}
				return;
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.premium-queue.name")))) {

				if(queue.isPlayerInQueue(player)){
					queue.leavePremiumQueue(player);
					profile.setState(PlayerState.LOBBY);
					items.spawnItems(player);
				}
				return;
			}
			
			//Party Stuff
		
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("party-items.queue.name")))) {
			
				if(PartyManager.getManager().isAPartyLeader(player.getUniqueId())) {

					inv.partyQueue(player);
					
				} else {
					player.sendMessage(ChatColor.RED + "You must be the party leader to do this action!");
				}
				
				return;
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("party-items.party-events.name")))) {
				
				player.sendMessage("SoonTM");
				
				return;
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("party-items.disband.name")))) {
				
				PartyManager.getManager().disband(player);
				
				return;
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("party-items.leave.name")))) {
				
				PartyManager.getManager().leave(player.getUniqueId());
				
				return;
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("party-items.info.name")))) {
				
				Party party = PartyManager.getManager().getPartyByPlayer(player.getUniqueId());
				
				if(party == null) {
					return;
				}
				
				player.sendMessage("\n" + ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");
				
				for(int i = 0; i < party.getMembers().size(); i++) {
					Player p = Bukkit.getPlayer(party.getMembers().get(i));
					
					if(party.getLeader() == p.getUniqueId()) {
						player.sendMessage(ChatColor.GOLD + "✫" + ChatColor.GRAY + " • " + (i+1) + " " + ChatColor.RED + p.getName());
					} else {
						player.sendMessage(ChatColor.GRAY + "   • " + (i+1) + " " + ChatColor.GOLD + p.getName());
					}
					
				}
				
				player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");
				
				return;
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.party-unranked-queue.name")))) {

				Party party = PartyManager.getManager().getPartyByLeader(player.getUniqueId());
				
				if(party == null) {
					return;
				}
				
				if(queue.isPartyInQueue(party)) {
					
					queue.leavePartyUnrankedQueue(party);
					
					for(UUID uuid : party.getMembers()) {
						Practice.getInstance().teleportSpawn(Bukkit.getPlayer(uuid));
					}
					
				}
				return;
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.spectator-leave.name")))) {
				
				spec.leaveSpectator(player);
				
				return;
			}
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("items.options.name")))) {
				
				inv.options(player);
				
				return;
			}
			
		}

	}
	
}
