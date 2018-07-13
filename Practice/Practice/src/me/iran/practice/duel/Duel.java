package me.iran.practice.duel;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.arena.Arena;
import me.iran.practice.enums.DuelType;
import me.iran.practice.gametype.GameType;

public class Duel {

	@Getter
    @Setter
    private Arena arena;

    @Getter
    @Setter
    private GameType game;
    
    @Getter
    @Setter
    private int duration;
    
    @Getter
    @Setter
    private boolean combo;
    
    @Getter
    @Setter
    private DuelType type;
    
    public Duel(GameType game, Arena arena) {
    		this.game = game;
    		this.arena = arena;
    		
    		duration = 0;
    }

}


