package me.iran.practice.kit;

import me.iran.practice.Practice;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.gametype.GameType;
import me.iran.practice.listeners.ClickListeners;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.utils.PlayerInventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class KitEditor implements Listener {

	private PlayerInventories inv = new PlayerInventories();
	
	@EventHandler
	public void onOpen(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if (event.getAction() == null) {
			return;
		}

		if(event.getClickedBlock() == null) {
			return;
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST) {
			
			if(profile.getState() == PlayerState.EDITOR) {
				
				if(ClickListeners.getGameSelected().containsKey(player.getName())) {
					
					GameType game = ClickListeners.getGameSelected().get(player.getName());
					
					Inventory inv = Bukkit.createInventory(null, 54, game.getName());
					
					for(int i = 0; i < game.getChest().length; i++) {
						inv.setItem(i, game.getChest()[i]);
					}
					
					event.setCancelled(true);
					
					player.openInventory(inv);
				
				}
			}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ANVIL) {
			
			if(profile.getState() == PlayerState.EDITOR) {
				
				if(ClickListeners.getGameSelected().containsKey(player.getName())) {
					
						event.setCancelled(true);
						
						inv.kitOptionsInventory(player);
					
				}
			}
		}
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			Block b = event.getClickedBlock();
			
			if ((b.getType() == Material.SIGN_POST) || (b.getType() == Material.WALL_SIGN)) {
				
				Sign s = (Sign) b.getState();
				
				if(profile.getState() == PlayerState.EDITOR) {
					
					if (s.getLine(0).equalsIgnoreCase("[Right Click]")) {
						
						if(ClickListeners.getNaming().contains(player.getName())) {
							player.sendMessage(ChatColor.GRAY + "You are in the middle of naming your kit, please type a name in chat!");
							return;
						}
						
						profile.setState(PlayerState.LOBBY);
						
						Practice.getInstance().teleportSpawn(player);
						
						ClickListeners.getGameSelected().remove(player.getName());
							
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		
		Player player = event.getPlayer();
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(profile.getState() == PlayerState.EDITOR) {
			
			if(ClickListeners.getGameSelected().containsKey(player.getName()) && ClickListeners.getNaming().contains(player.getName())) {
				
				GameType game = ClickListeners.getGameSelected().get(player.getName());
				
				KitManager.getManager().createKit(event.getMessage().replace(" ", "_"), game, player);

				ClickListeners.getNaming().remove(player.getName());
				
				event.setCancelled(true);
			}
			
		}
	}

	public void onDisconnect(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		if(ClickListeners.getGameSelected().containsKey(player.getName())) {
			ClickListeners.getGameSelected().remove(player.getName());
		}
		
		if(ClickListeners.getNaming().contains(player.getName())) {
			ClickListeners.getNaming().remove(player.getName());
		}
	}
}
