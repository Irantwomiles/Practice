package me.iran.practice.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.duel.team.SendTeamDuel;
import me.iran.practice.enums.PartyState;

public class Party {

	@Getter
	@Setter
	private UUID leader;
	
	@Getter
	@Setter
	private ArrayList<UUID> members, invites;
	
	@Getter
	@Setter
	private boolean open;
	
	@Getter
	@Setter
	private int maxMembers = 10;
	
	@Getter
	@Setter
	private int elo;
	
	@Getter
	private static int id;
	
	@Getter
	@Setter
	private PartyState state;
	
	@Getter
	@Setter
	private HashMap<Party, SendTeamDuel> requests;
	
	public Party (UUID leader) {
		
		id++;
		
		this.leader = leader;
		this.open = false;
		
		members = new ArrayList<>();
		invites = new ArrayList<>();
		
		members.add(leader);
		
		state = PartyState.LOBBY;
		
		requests = new HashMap<>();
	}
	
}
