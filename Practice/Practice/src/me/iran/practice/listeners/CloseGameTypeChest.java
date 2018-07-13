package me.iran.practice.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.iran.practice.cmd.GameTypeCommands;
import me.iran.practice.gametype.GameType;
import net.md_5.bungee.api.ChatColor;

public class CloseGameTypeChest implements Listener {

	@EventHandler
	public void onClose(InventoryCloseEvent event) {

		if(event.getPlayer() instanceof Player) {
			
			Player player = (Player) event.getPlayer();
			
			if(GameTypeCommands.getEditor().containsKey(player.getName())) {
				
				GameType game = GameTypeCommands.getEditor().get(player.getName());
				
				game.setChest(event.getInventory().getContents());
				
				GameTypeCommands.getEditor().remove(player.getName());
				
				player.sendMessage(ChatColor.GREEN + "Updated Editor chest for " + game.getName());
				
			}
			
			
		}
		
	}
	
}
