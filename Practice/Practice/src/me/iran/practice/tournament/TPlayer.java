package me.iran.practice.tournament;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;

public class TPlayer {

	@Getter
	@Setter
	private Player player;
	
	@Getter
	@Setter
	private int position;
	
	public TPlayer(Player player) {
		this.player = player;
	}
	
}
