package me.iran.practice.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

public class StoreInventory {

	@Getter
	@Setter
	private ItemStack[] armor, inv;

	@Getter
	@Setter
	private List<String> effects;
	
	@Getter
	@Setter
	private double hunger, health;
	
	public StoreInventory(ItemStack[] inv, ItemStack[] armor, double hunger, double health) {
		this.inv = inv;
		this.armor = armor;
		
		this.hunger = hunger;
		this.health = health;
		
		effects = new ArrayList<>();
	}
	
}
