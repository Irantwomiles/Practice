package me.iran.practice.duel.team.listeners;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.duel.team.TeamDuel;

public class TeamDeath extends Event {
	
	private static final HandlerList handlers = new HandlerList();

	@Getter
	@Setter
	private TeamDuel duel;
	
	public TeamDeath(TeamDuel duel) {
		this.duel = duel;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
