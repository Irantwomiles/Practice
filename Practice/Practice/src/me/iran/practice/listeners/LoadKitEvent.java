package me.iran.practice.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.iran.practice.duel.SoloDuelManager;
import me.iran.practice.duel.team.TeamDuelManager;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.events.lms.LMS;
import me.iran.practice.events.lms.LMSManager;
import me.iran.practice.events.tdm.TDM;
import me.iran.practice.kit.KitManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;

public class LoadKitEvent implements Listener {

	private TDM tdm = new TDM();
	
	@EventHandler
	public void onLoad(PlayerInteractEvent event) {
		
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
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if(player.getItemInHand().getType() == Material.ENCHANTED_BOOK) {
				
				if(profile.getState() == PlayerState.IN_GAME) {
					
					if(SoloDuelManager.getManager().getDuelByPlayer(player) != null) {
						
						String name = ChatColor.stripColor(player.getItemInHand().getItemMeta().getDisplayName());
						
						KitManager.getManager().loadPlayerKit(player, SoloDuelManager.getManager().getDuelByPlayer(player).getGame(), name);
					
						player.updateInventory();
						
					} else if(TeamDuelManager.getManager().getDuelByAlivePlayer(player) != null) {
	
						String name = ChatColor.stripColor(player.getItemInHand().getItemMeta().getDisplayName());
						
						KitManager.getManager().loadPlayerKit(player, TeamDuelManager.getManager().getDuelByAlivePlayer(player).getGame(), name);

						player.updateInventory();
					}
					
				} else if(profile.getState() == PlayerState.IN_EVENT) {
					
					if(LMSManager.getManager().isPlayerInLMS(player)) {
						
						LMS lms = LMSManager.getManager().getLMSByPlayer(player);
						
						if(lms.getTimer() <= 5) {
							
							if(player.getItemInHand().getType() == Material.ENCHANTED_BOOK) {
								
								String name = ChatColor.stripColor(player.getItemInHand().getItemMeta().getDisplayName());
								
								KitManager.getManager().loadPlayerKit(player, lms.getGame(), name);
							
								player.updateInventory();
								
							}
							
						}
					}
					
					if(tdm.isPlayerInTDM(player)) {
						
						String name = ChatColor.stripColor(player.getItemInHand().getItemMeta().getDisplayName());
						
						KitManager.getManager().loadPlayerKit(player, TDM.getGame(), name);
						player.updateInventory();
					}
					
				}
				
			}
			
		}
		
	}
	
}
