package me.iran.practice.arena.listeners;

import me.iran.practice.Practice;
import me.iran.practice.arena.Arena;
import me.iran.practice.cmd.ArenaCommands;
import me.iran.practice.enums.ArenaState;
import me.iran.practice.utils.PlayerItems;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ArenaEditListeners implements Listener {

	private PlayerItems items = new PlayerItems();
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		
		if(ArenaCommands.getEditing().containsKey(event.getPlayer().getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		
		if(event.getAction() == null)
			return;
		
		if(ArenaCommands.getEditing().containsKey(player.getName())) {
			
			Arena arena = ArenaCommands.getEditing().get(player.getName());
			
			//set location 1
			if(event.getAction() == Action.RIGHT_CLICK_AIR && arena.getState() == ArenaState.EDITOR) {
				
				if(player.getItemInHand() != null && player.getItemInHand().getType() == Material.DIAMOND_BLOCK) {
					
					arena.setLoc1(player.getLocation());
					player.sendMessage(ChatColor.GRAY + "You have set location 1 for " + ChatColor.YELLOW + arena.getId());
					
				}
				
			}
			
			//set location 2
			if(event.getAction() == Action.LEFT_CLICK_AIR && arena.getState() == ArenaState.EDITOR) {
				
				if(player.getItemInHand() != null && player.getItemInHand().getType() == Material.DIAMOND_BLOCK) {
					
					arena.setLoc2(player.getLocation());
					player.sendMessage(ChatColor.GRAY + "You have set location 2 for " + ChatColor.YELLOW + arena.getId());
					
				}
				
			}
			
			//Leaving editor mode
			if(event.getAction() == Action.RIGHT_CLICK_AIR && arena.getState() == ArenaState.EDITOR) {
				
				if(player.getItemInHand() != null && player.getItemInHand().getType() == Material.GOLD_BLOCK) {
					
					arena.setState(ArenaState.OPEN);
					ArenaCommands.getEditing().remove(player.getName());
					
					player.sendMessage(ChatColor.GRAY + "You left editor mode for arena " + ChatColor.RED + arena.getId());
					
					Practice.getInstance().teleportSpawn(player);
					items.spawnItems(player);
				}
				
			}
		}
		
	}
}
