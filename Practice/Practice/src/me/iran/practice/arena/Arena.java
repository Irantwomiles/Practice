package me.iran.practice.arena;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.enums.ArenaState;
import me.iran.practice.enums.ArenaType;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Item;

public class Arena {

    //list of locations that can be used based on the arena type
    @Getter
    @Setter
    private Location loc1, loc2;

    //name displayed to player
    //arena world
    @Getter
    @Setter
    private String name, id;

    //state of the arena
    @Getter
    @Setter
    private ArenaState state;

    //type of arena
    @Getter
    @Setter
    private ArenaType type;
    
    @Getter
    @Setter
    private ArrayList<UUID> spectators;
    
    @Getter
    @Setter
    private ArrayList<Item> drops;
    
    public Arena(String id) {

        this.id = id;
        this.name = id;

        this.state = ArenaState.EDITOR;
        this.type = ArenaType.SOLO;
        spectators = new ArrayList<>();
        drops = new ArrayList<>();
    }


}
