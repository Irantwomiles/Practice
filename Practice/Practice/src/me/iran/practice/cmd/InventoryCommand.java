package me.iran.practice.cmd;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.iran.practice.enums.PlayerState;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.utils.StoreInventory;
import net.md_5.bungee.api.ChatColor;

public class InventoryCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(cmd.getName().equalsIgnoreCase("_")) {
			
			if(args.length < 1) {
				player.sendMessage(ChatColor.RED + "/_ <player>");
				return true;
			}

			if(profile.getState() != PlayerState.LOBBY) {
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);

			if(target == null) return true;
			
			StoreInventory store = ProfileManager.getManager().getProfileByPlayer(target).getStoreInv().get(target.getUniqueId());
			
			if(store == null) {
				player.sendMessage(ChatColor.RED + "Not a valid inventory");
				return true;
			}
			
			Inventory inv = Bukkit.createInventory(player, 54, target.getDisplayName());
			
			for(int i = 0; i < store.getArmor().length; i++) {
				inv.setItem(i + 36, store.getArmor()[i]);
			}
			
			for(int i = 0; i < 9; i++) {
				inv.setItem(i + 27, store.getInv()[i]);
			}
			
			for(int i = 9; i < store.getInv().length; i++) {
				inv.setItem(i - 9, store.getInv()[i]);
			}
			
			ItemStack steak = new ItemStack(Material.COOKED_BEEF);
			ItemMeta smeta = steak.getItemMeta();
			
			NumberFormat formatter = new DecimalFormat("#0.0");  
			
			smeta.setDisplayName(ChatColor.YELLOW.toString() + formatter.format(store.getHunger()) + " / 20");
			steak.setItemMeta(smeta);
			
			ItemStack apple = new ItemStack(Material.APPLE);
			ItemMeta ameta = apple.getItemMeta();
			
			ameta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() +formatter.format(store.getHealth()) + " / 20");
			apple.setItemMeta(ameta);
			
			ItemStack brew = new ItemStack(Material.BREWING_STAND_ITEM);
			ItemMeta bmeta = apple.getItemMeta();
			
			bmeta.setDisplayName(ChatColor.RED.toString() + "Active Effects");
			bmeta.setLore(store.getEffects());
			brew.setItemMeta(bmeta);
			
			inv.setItem(53, apple);
			inv.setItem(52, steak);
			inv.setItem(51, brew);
			
			player.openInventory(inv);
			
		}
		
		return true;
	}

}
