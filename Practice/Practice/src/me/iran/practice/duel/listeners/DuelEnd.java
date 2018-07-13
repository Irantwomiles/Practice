package me.iran.practice.duel.listeners;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.duel.SoloDuel;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DuelEnd extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	@Getter
	@Setter
	private SoloDuel duel;

	public DuelEnd(SoloDuel duel) {
		this.duel = duel;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
