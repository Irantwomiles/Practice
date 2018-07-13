package me.iran.practice.gametype;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.party.Party;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GameType {

	@Getter 
	@Setter
	private String name;
	
	@Getter
	@Setter
	private ItemStack display = new ItemStack(Material.DIAMOND_SWORD);
	
	@Getter
	@Setter
	private ItemStack[] inv, armor, chest;
	
	@Getter
	@Setter
	private ArrayList<String> unranked, ranked, premium;
	
	@Getter
	@Setter
	private ArrayList<Party> partyUnranked, partyRanked, partyPremium;
	
	@Getter
	@Setter
	private int inGame = 0;
	
	@Getter
	@Setter
	private boolean editable = true, rank;
	
	public GameType(String name, ItemStack[] inv, ItemStack[] armor) {
		
		this.name = name;
		this.rank = true;
		this.inv = inv;
		this.armor = armor;
		
		chest = new ItemStack[54];
		
		unranked = new ArrayList<>();
		ranked = new ArrayList<>();
		premium = new ArrayList<>();
	
		partyUnranked = new ArrayList<>();
		partyRanked = new ArrayList<>();
		partyPremium = new ArrayList<>();
		
	}
}
