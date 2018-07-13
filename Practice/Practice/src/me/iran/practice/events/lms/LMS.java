package me.iran.practice.events.lms;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.arena.Arena;
import me.iran.practice.gametype.GameType;

public class LMS {

	private static int ID;

	@Getter
	@Setter
	private int timer = 60, max = 100, min = 2, id;
	
	@Getter
	@Setter
	private boolean active = false;
	
	@Getter
	@Setter
	private ArrayList<UUID> players;
	
	@Getter
	@Setter
	private Arena arena;
	
	@Getter
	@Setter
	private GameType game;
	
	public LMS(Arena arena, GameType game) {
		ID++;
		players = new ArrayList<>();
		this.arena = arena;
		this.game = game;
		id = ID;
		this.timer = 60;
	}

}
