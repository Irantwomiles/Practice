package me.iran.practice.events.tdm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.iran.practice.utils.PlayerInventories;
import net.md_5.bungee.api.ChatColor;

public class TDMCommands implements CommandExecutor {

	private TDM tdm = new TDM();
	
	private PlayerInventories inv = new PlayerInventories();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("tdm")) {
			
			if(args.length < 1) {
				player.sendMessage(ChatColor.RED + "/tdm start");
				player.sendMessage(ChatColor.RED + "/tdm stop");
				player.sendMessage(ChatColor.RED + "/tdm setloc1");
				player.sendMessage(ChatColor.RED + "/tdm setloc2");
				player.sendMessage(ChatColor.RED + "/tdm join");
				player.sendMessage(ChatColor.RED + "/tdm leave");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("start")) {
				
				if(!player.hasPermission("practice.tdm")) {
					player.sendMessage(ChatColor.RED + "No Permission.");
					return true;
				}
				
				inv.TDM(player);
				
			}
			
			if(args[0].equalsIgnoreCase("stop")) {
				if(!player.hasPermission("practice.tdm")) {
					player.sendMessage(ChatColor.RED + "No Permission.");
					return true;
				}
				
				tdm.forceEndGame(ChatColor.RED + "Game has come to a forced end!");
			}
	
			if(args[0].equalsIgnoreCase("setloc1")) {
				
				if(!TDM.isActive()) {
					TDM.setLoc1(player.getLocation());
					
					player.sendMessage(ChatColor.GOLD + "Set Location 1 for TDM");
				} else {
					player.sendMessage(ChatColor.RED + "Can't edit an TDM arena while it's active");
				}
				
			}
			
			if(args[0].equalsIgnoreCase("setloc2")) {
				
				if(!TDM.isActive()) {
					TDM.setLoc2(player.getLocation());
					
					player.sendMessage(ChatColor.GOLD + "Set Location 2 for TDM");
				} else {
					player.sendMessage(ChatColor.RED + "Can't edit an TDM arena while it's active");
				}
				
			}
			
			if(args[0].equalsIgnoreCase("join")) {
				tdm.joinTDM(player);
			}
	
			if(args[0].equalsIgnoreCase("leave")) {
				tdm.leaveTDM(player);
			}
			
		}
		
		return true;
	}
	
}
