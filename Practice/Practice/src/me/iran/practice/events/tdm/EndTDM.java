package me.iran.practice.events.tdm;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

public class EndTDM extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	@Getter
	@Setter
	private Player player;

	public EndTDM(Player player) {
		this.player = player;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
