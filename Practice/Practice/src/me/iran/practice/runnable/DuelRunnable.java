package me.iran.practice.runnable;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.iran.practice.duel.SoloDuel;
import me.iran.practice.duel.SoloDuelManager;
import me.iran.practice.duel.team.TeamDuel;
import me.iran.practice.duel.team.TeamDuelManager;
import net.md_5.bungee.api.ChatColor;

public class DuelRunnable extends BukkitRunnable {
	
	public void run() {
	
		for(SoloDuel duel : SoloDuelManager.getDuelSet()) {
			
			if(duel.getDuration() < 6) {
				
				if((5 - duel.getDuration()) > 0) {
					duel.getPlayer1().sendMessage(ChatColor.YELLOW + "Match starting in " + ChatColor.GRAY + (5 - duel.getDuration()) + ChatColor.YELLOW + "...");
					duel.getPlayer2().sendMessage(ChatColor.YELLOW + "Match starting in " + ChatColor.GRAY + (5 - duel.getDuration()) + ChatColor.YELLOW + "...");
					duel.getPlayer1().playSound(duel.getPlayer1().getLocation(), Sound.NOTE_BASS_GUITAR, 10f, 10f);
					duel.getPlayer2().playSound(duel.getPlayer1().getLocation(), Sound.NOTE_BASS_GUITAR, 10f, 10f);
				} else if((5 - duel.getDuration()) == 0) {
					duel.getPlayer1().sendMessage(ChatColor.YELLOW + "Match has started!");
					duel.getPlayer2().sendMessage(ChatColor.YELLOW + "Match has started!");
					duel.getPlayer1().playSound(duel.getPlayer1().getLocation(), Sound.LEVEL_UP, 10f, 10f);
					duel.getPlayer2().playSound(duel.getPlayer1().getLocation(), Sound.LEVEL_UP, 10f, 10f);
				}
				
				
			}
			
			duel.setDuration(duel.getDuration() + 1);
			
		}
		
		for(TeamDuel duel : TeamDuelManager.getDuelSet()) {
			
			if(duel.getDuration() < 6) {

				for(UUID uuid : duel.getPlayers()) {
					
					Player player = Bukkit.getPlayer(uuid);
					
					if(player != null) {
						if((5 - duel.getDuration()) > 0) {
							player.sendMessage(ChatColor.YELLOW + "Match starting in " + ChatColor.GRAY + (5 - duel.getDuration()) + ChatColor.YELLOW + "...");
							player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 10f, 10f);
						} else if((5 - duel.getDuration()) == 0) {
							player.sendMessage(ChatColor.YELLOW + "Match has started!");
							player.playSound(player.getLocation(), Sound.LEVEL_UP, 10f, 10f);
						}
					}

				}
				
			}
			
			duel.setDuration(duel.getDuration() + 1);
			
		}
		
	}
	
}
