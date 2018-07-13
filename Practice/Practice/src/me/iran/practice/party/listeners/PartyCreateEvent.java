package me.iran.practice.party.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.iran.practice.utils.PlayerItems;

public class PartyCreateEvent implements Listener {

	private PlayerItems items = new PlayerItems();
	
	@EventHandler
	public void onCreate(PartyCreate event) {
		
		Player leader = Bukkit.getPlayer(event.getParty().getLeader());
		
		items.partyItemsLeader(leader);
		
	}
	
}
