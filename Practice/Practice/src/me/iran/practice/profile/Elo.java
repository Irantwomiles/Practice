package me.iran.practice.profile;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.gametype.GameType;

public class Elo {

	@Getter
	@Setter
	private int elo;
	
	@Getter
	@Setter
	private GameType game;
	
	public Elo(int elo, GameType game) {
		this.elo = elo;
		this.game = game;
	}
	
}
