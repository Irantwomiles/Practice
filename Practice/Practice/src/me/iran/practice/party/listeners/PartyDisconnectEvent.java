package me.iran.practice.party.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.iran.practice.party.Party;
import me.iran.practice.party.PartyManager;

public class PartyDisconnectEvent implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		if(PartyManager.getManager().isPlayerInParty(player.getUniqueId())) {
			Party party = PartyManager.getManager().getPartyByPlayer(player.getUniqueId());
			
			if(party.getLeader() == player.getUniqueId()) {
				PartyManager.getManager().disband(player);
			} else {
				PartyManager.getManager().leave(player.getUniqueId());
			}
		}
		
	}

}
