package me.iran.practice.events.tdm;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

public class LeaveTDM extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	@Getter
	@Setter
	private Player player;

	public LeaveTDM(Player player) {
		this.player = player;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
