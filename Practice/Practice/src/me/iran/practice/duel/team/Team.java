package me.iran.practice.duel.team;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class Team {

	@Getter
	@Setter
	private ArrayList<UUID> players;
	
	@Getter
	@Setter
	private ArrayList<UUID> allPlayers;
	
	@Getter
	@Setter
	private ArrayList<UUID> alive;
	
	public Team() {
		players = new ArrayList<>();
		alive = new ArrayList<>();
		allPlayers = new ArrayList<>();
	}
	
}
