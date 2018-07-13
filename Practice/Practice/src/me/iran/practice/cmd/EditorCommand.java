package me.iran.practice.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.iran.practice.Practice;
import net.md_5.bungee.api.ChatColor;

public class EditorCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String args, String[] label) {

		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("seteditor")) {
			
			if(!player.hasPermission("practice.admin")) return true;
			
			double x = player.getLocation().getX();
			double y = player.getLocation().getY();
			double z = player.getLocation().getZ();
			
			float pitch = player.getLocation().getPitch();
			float yaw = player.getLocation().getYaw();
			
			String world = player.getLocation().getWorld().getName();
			
			Practice.getInstance().getConfig().set("editor.x", x);
			Practice.getInstance().getConfig().set("editor.y", y);
			Practice.getInstance().getConfig().set("editor.z", z);
			Practice.getInstance().getConfig().set("editor.pitch", pitch);
			Practice.getInstance().getConfig().set("editor.yaw", yaw);
			Practice.getInstance().getConfig().set("editor.world", world);
		
			Practice.getInstance().saveConfig();
			
			player.sendMessage(ChatColor.YELLOW + "Editor Spawn has been set!");
		}
		
		return true;
	}
	
}
