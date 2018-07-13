package me.iran.practice.party.listeners;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.party.Party;

public class PartyCreate extends Event {

	private static final HandlerList handlers = new HandlerList();

	@Getter
	@Setter
	private Party party;
	
	public PartyCreate(Party party) {
		this.party = party;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
