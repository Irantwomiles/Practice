package me.iran.practice.scoreboard;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;

import me.iran.practice.Practice;
import me.iran.practice.duel.listeners.DuelEnd;
import me.iran.practice.duel.listeners.DuelStart;
import me.iran.practice.duel.team.TeamDuel;
import me.iran.practice.duel.team.TeamDuelManager;
import me.iran.practice.duel.team.listeners.TeamDeath;
import me.iran.practice.duel.team.listeners.TeamDuelEnd;
import me.iran.practice.duel.team.listeners.TeamDuelStart;
import me.iran.practice.events.lms.LMS;
import me.iran.practice.events.lms.LMSJoin;
import me.iran.practice.events.lms.LMSLeave;
import me.iran.practice.events.tdm.EndTDM;
import me.iran.practice.events.tdm.JoinTDM;
import me.iran.practice.events.tdm.LeaveTDM;

public class ScoreboardEvents implements Listener {

	PlayerBoard board = new PlayerBoard();
	
	
	@EventHandler
	public void onStart(TeamDuelStart event) {
		for(UUID uuid : event.getDuel().getAlivePlayers()) {
			board.teamBoard(Bukkit.getPlayer(uuid));
		}
		
	}
	
	@EventHandler
	public void onEnd(TeamDuelEnd event) {
		for(UUID uuid : event.getDuel().getPlayers()) {
			Bukkit.getPlayer(uuid).getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
			board.lobbyBoard(Bukkit.getPlayer(uuid));
		}
	}
	
	@EventHandler
	public void onDeath(TeamDeath event) {
		
		TeamDuel duel = event.getDuel();
		
		for(UUID uuid : duel.getAlivePlayers()) {
			
			Player player = Bukkit.getPlayer(uuid);
			
			if(player == null) continue;
			
			if(player.getScoreboard() != null && player.getScoreboard().getTeam("team1") != null && player.getScoreboard().getTeam("team2") != null) {

				if(duel.getTeam1().getAlive().size() > 1) {
					
					board.setLine(player.getScoreboard().getTeam("team1"),
							ChatColor.translateAlternateColorCodes('&',
									Practice.getInstance().getConfig().getString("scoreboard.team1")
											.replace("%total%", duel.getTeam1().getPlayers().size() + "")
											.replace("%alive%", duel.getTeam1().getAlive().size() + "")));

					board.setLine(player.getScoreboard().getTeam("team2"),
							ChatColor.translateAlternateColorCodes('&',
									Practice.getInstance().getConfig().getString("scoreboard.team2")
											.replace("%total%", duel.getTeam2().getPlayers().size() + "")
											.replace("%alive%", duel.getTeam2().getAlive().size() + "")));
				}

			}
			
		}
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(TeamDuelManager.getManager().isPlayerAliveInDuel(event.getEntity())) {
			event.getEntity().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {

		board.lobbyBoard(event.getPlayer());

	}
	
	@EventHandler
	public void onJoin(JoinTDM event) {
		board.tdmBoard(event.getPlayer());
	}
	
	@EventHandler
	public void onLeave(LeaveTDM event) {

		board.lobbyBoard(event.getPlayer());

	}
	
	@EventHandler
	public void onEnd(EndTDM event) {
		board.lobbyBoard(event.getPlayer());

	}
	
	@EventHandler
	public void onStart(DuelStart event) {
		
		event.getDuel().getPlayer1().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		event.getDuel().getPlayer2().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
	}
	
	@EventHandler
	public void onEnd(DuelEnd event) {
		
		board.lobbyBoard(event.getDuel().getPlayer1());
		board.lobbyBoard(event.getDuel().getPlayer2());
	}
	
	@EventHandler
	public void onJoinLMS(LMSJoin event) {
		board.ffaBoard(event.getPlayer());
	}
	
	@EventHandler
	public void onLeaveLMS(LMSLeave event) {
		
		LMS lms = event.getLms();
		
		if(lms.getPlayers().size() > 1) {
			
			for(UUID uuid : lms.getPlayers()) {
				
				Player player = Bukkit.getPlayer(uuid);
				
				if(player.getScoreboard().getTeam("alive") != null) {
					
					String msg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.ffa.alive").replace("%alive%",  lms.getPlayers().size() + ""));
					
					board.setLine(player.getScoreboard().getTeam("alive"), msg);
				}
				
			}
			
		}
		
		board.lobbyBoard(event.getPlayer());
	}
}
