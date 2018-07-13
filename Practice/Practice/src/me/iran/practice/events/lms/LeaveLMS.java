package me.iran.practice.events.lms;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

public class LeaveLMS extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	@Getter
	@Setter
	private LMS lms;
	
	public LeaveLMS(LMS lms) {
		this.lms = lms;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
