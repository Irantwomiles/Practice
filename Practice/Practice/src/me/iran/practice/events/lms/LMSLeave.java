package me.iran.practice.events.lms;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

public class LMSLeave extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	@Getter
	@Setter
	private LMS lms;

	@Getter
	@Setter
	private Player player;
	
	public LMSLeave(LMS lms, Player player) {
		this.lms = lms;
		this.player = player;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
