package me.iran.practice.tournament;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.iran.practice.gametype.GameTypeManager;

public class TournamentCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		
		if(cmd.getName().equalsIgnoreCase("tournament")) {
			
			if(args[0].equalsIgnoreCase("create")) {
				
				Tournament t = new Tournament(GameTypeManager.getManager().getGameType(args[1]));
				
				
				TournamentManager.getManager().getTournaments().add(t);
				
				Bukkit.broadcastMessage("Tournament started /tournament join " + t.getID());
			}
			
			if(args[0].equalsIgnoreCase("join")) {
				
				Tournament t = TournamentManager.getManager().getTournamentByID(Integer.parseInt(args[1]));
				
				TPlayer tplayer = new TPlayer(player);
				
				if(!t.getPlayers().contains(tplayer)) {
					t.getPlayers().add(tplayer);
					
					tplayer.setPosition(t.getPlayers().size());
					
					Bukkit.broadcastMessage("[" + tplayer.getPosition() + "]" + tplayer.getPlayer().getName() + " has joined the tournament ");
					
				}
				
				
				
			}
			
		}
		
		return true;
	}
	
}
