package me.iran.practice.arena.listeners;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.arena.Arena;

public class ArenaAvailable extends Event {
	
	private static final HandlerList handlers = new HandlerList();

	@Getter
	@Setter
	private Arena arena;
	
	public ArenaAvailable(Arena arena) {
		this.arena = arena;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
