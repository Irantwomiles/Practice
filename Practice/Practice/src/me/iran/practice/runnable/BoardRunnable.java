package me.iran.practice.runnable;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.iran.practice.Practice;
import me.iran.practice.events.lms.LMS;
import me.iran.practice.events.lms.LMSManager;
import me.iran.practice.events.tdm.TDM;
import me.iran.practice.scoreboard.PlayerBoard;
import me.iran.practice.utils.Utils;

public class BoardRunnable extends BukkitRunnable {
	
	PlayerBoard board = new PlayerBoard();
	
	Utils utils = new Utils();
	
	TDM tdm = new TDM();
	
	@SuppressWarnings("deprecation")
	public void run() {
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			
			if(p.getScoreboard() != null) {
				
				//Lobby Board
				if(p.getScoreboard().getTeam("online") != null) {
					String onlineMsg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.lobby.online").replace("%online%", "" + Bukkit.getServer().getOnlinePlayers().length));
					
					board.setLine(p.getScoreboard().getTeam("online"), onlineMsg);
				}
				
				
				//TDM Board
				if(tdm.isPlayerInTDM(p)) {
					
					if(p.getScoreboard().getTeam("blue") != null) {
						String bmsg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.tdm.blue").replace("%players%", "" + TDM.getTeam1().size()).replace("%win%", "" + tdm.getWin()).replace("%score%", TDM.getScore1() + ""));
						
						board.setLine(p.getScoreboard().getTeam("blue"), bmsg);
					}
					
					if(p.getScoreboard().getTeam("green") != null) {
						String gmsg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.tdm.green").replace("%players%", "" + TDM.getTeam2().size()).replace("%win%", "" + tdm.getWin()).replace("%score%", TDM.getScore2() + ""));
							
						board.setLine(p.getScoreboard().getTeam("green"), gmsg);
					}
					
					if(p.getScoreboard().getTeam("timer") != null) {
						
						if(TDM.isStarted()) {
							String timerMsg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.tdm.game-timer").replace("%timer%", "" + TDM.getGameTimer()));
							board.setLine(p.getScoreboard().getTeam("timer"), timerMsg);
						} else {
							String timerMsg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.tdm.start-timer").replace("%timer%", "" + TDM.getTimer()));
							board.setLine(p.getScoreboard().getTeam("timer"), timerMsg);
						}
						
					}

					
					
					if(p.getScoreboard().getTeam("team1") != null) {
						
						for(UUID uuid : TDM.getTeam1()) {
							
							Player player = Bukkit.getPlayer(uuid);
							
							if(player == null) {
								continue;
							}
							
							if(!p.getScoreboard().getTeam("team1").hasPlayer(player)) {
								p.getScoreboard().getTeam("team1").addPlayer(player);
							} 
							
						}
						
					}
					
					if(p.getScoreboard().getTeam("team2") != null) {
	
						for(UUID uuid : TDM.getTeam2()) {
							
							Player player = Bukkit.getPlayer(uuid);
							
							if(player == null) {
								continue;
							}
							
							if(!p.getScoreboard().getTeam("team2").hasPlayer(player)) {
								p.getScoreboard().getTeam("team2").addPlayer(player);
							} 
							
						}
					}
				}
				
				//FFA Board
				if (LMSManager.getManager().isPlayerInLMS(p)) {

					LMS lms = LMSManager.getManager().getLMSByPlayer(p);

					if (lms.getPlayers().size() > 0) {

						if (p.getScoreboard().getTeam("info") != null) {

							String msg = "";

							if (!lms.isActive()) {
								msg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.ffa.time").replace("%time%", utils.formatTime(lms.getTimer())));
							} else {
								msg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.ffa.info"));
							}

							board.setLine(p.getScoreboard().getTeam("info"), msg);
						}
						
						if (p.getScoreboard().getTeam("alive") != null) {
							
							String msg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.ffa.alive").replace("%alive%",  lms.getPlayers().size() + ""));
							board.setLine(p.getScoreboard().getTeam("alive"), msg);
							
						}

					}
					
				}
				
			}
			
		}
		
	}
	
}
