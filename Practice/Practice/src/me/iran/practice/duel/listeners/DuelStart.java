package me.iran.practice.duel.listeners;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.duel.SoloDuel;

public class DuelStart extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	@Getter
	@Setter
	private SoloDuel duel;

	public DuelStart(SoloDuel duel) {
		this.duel = duel;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
