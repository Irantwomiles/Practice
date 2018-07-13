package me.iran.practice.scoreboard;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.iran.practice.Practice;
import me.iran.practice.arena.Arena;
import me.iran.practice.duel.team.TeamDuel;
import me.iran.practice.duel.team.TeamDuelManager;
import me.iran.practice.events.lms.LMS;
import me.iran.practice.events.lms.LMSManager;
import me.iran.practice.events.tdm.TDM;
import me.iran.practice.utils.Utils;

public class PlayerBoard {

	TDM tdm = new TDM();
	
	Utils utils = new Utils();
	
	@SuppressWarnings("deprecation")
	public void teamBoard(Player player) {

		TeamDuel duel = TeamDuelManager.getManager().getDuelByPlayer(player);
		
		if(duel == null) return;

		
		Arena arena = duel.getArena();
		
		if(arena == null) return;

		
		player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		if(board == null) return;

		Objective obj = board.registerNewObjective("team", "dummy");
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.team.title")));
		
		Team topWrapper = board.registerNewTeam("top");
		topWrapper.addEntry(ChatColor.WHITE.toString());
		
		String topLine = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.team.line"));
		
		setLine(topWrapper, topLine);
		obj.getScore(ChatColor.WHITE.toString()).setScore(6);
		
		Team bottomWrapper = board.registerNewTeam("bottom");
		bottomWrapper.addEntry(ChatColor.BLACK.toString());
		setLine(bottomWrapper, topLine);
		obj.getScore(ChatColor.BLACK.toString()).setScore(1);
		
		Team info = board.registerNewTeam("info");
		info.addEntry(ChatColor.AQUA.toString());
		String msg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.team.info"));
		
		setLine(info, msg);
		
		obj.getScore(ChatColor.AQUA.toString()).setScore(2);
		
		Team space = board.registerNewTeam("space");
		space.addEntry(ChatColor.GRAY.toString());
		space.setPrefix("");
		obj.getScore(ChatColor.GRAY.toString()).setScore(3);
		
		Team team1 = board.registerNewTeam("team1");
		team1.addEntry(ChatColor.RESET.toString());
		
		String msg1 = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.team.team1").replace("%total%", duel.getTeam1().getPlayers().size() + "").replace("%alive%", duel.getTeam1().getAlive().size() + ""));
		
		if(duel.getTeam1().getAlive().contains(player.getUniqueId())) {

			setLine(team1, msg1 + ChatColor.GRAY + " (You)");
		} else {
			setLine(team1, msg1);
		}
		
		obj.getScore(ChatColor.RESET.toString()).setScore(5);
		
		Team team2 = board.registerNewTeam("team2");
		team2.addEntry(ChatColor.RED.toString());
		
		String msg2 = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.team.team2").replace("%total%", duel.getTeam2().getPlayers().size() + "").replace("%alive%", duel.getTeam2().getAlive().size() + ""));
		
		if(duel.getTeam2().getAlive().contains(player.getUniqueId())) {
			setLine(team2, msg2 + ChatColor.GRAY + " (You)");
		} else {
			setLine(team2, msg2);
		}	
		
		obj.getScore(ChatColor.RED.toString()).setScore(4);
		
		
		Team friend;
		
		if(board.getTeam("friend") != null) {
			friend = board.getTeam("friend");
		} else {
			friend = board.registerNewTeam("friend");
		}
		
		friend.setPrefix(ChatColor.GREEN.toString());
		
		Team enemy;

		if(board.getTeam("enemy") != null) {
			enemy = board.getTeam("enemy");
		} else {
			enemy = board.registerNewTeam("enemy");
		}
		
		enemy.setPrefix(ChatColor.WHITE.toString());
		enemy.setCanSeeFriendlyInvisibles(false);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
				enemy.addPlayer(p);
		}
		
		if(duel.getTeam1().getAlive().contains(player.getUniqueId())) {
			
			for(UUID uuid : duel.getTeam1().getAlive()) {
				
				Player p = Bukkit.getPlayer(uuid);
				
				if(p == null) continue;
				
				friend.addPlayer(p);
			}
		}
		
		if(duel.getTeam2().getAlive().contains(player.getUniqueId())) {
			
			for(UUID uuid : duel.getTeam2().getAlive()) {
				
				Player p = Bukkit.getPlayer(uuid);
				
				if(p == null) continue;
				
				friend.addPlayer(p);
			}
		}
		
		player.setScoreboard(board);
		
	}

	@SuppressWarnings("deprecation")
	public void lobbyBoard(Player player) {

		player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		if(board == null) return;

		Objective obj = board.registerNewObjective("lobby", "dummy");
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.lobby.title")));
		
		Team topWrapper = board.registerNewTeam("top");
		topWrapper.addEntry(ChatColor.WHITE.toString());
		
		String topLine = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.lobby.line"));
		
		setLine(topWrapper, topLine);
		obj.getScore(ChatColor.WHITE.toString()).setScore(6);
		
		Team bottomWrapper = board.registerNewTeam("bottom");
		bottomWrapper.addEntry(ChatColor.BLACK.toString());
		setLine(bottomWrapper, topLine);
		obj.getScore(ChatColor.BLACK.toString()).setScore(1);
		
		Team info = board.registerNewTeam("info");
		info.addEntry(ChatColor.AQUA.toString());
		String msg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.lobby.info"));
		
		setLine(info, msg);
		
		obj.getScore(ChatColor.AQUA.toString()).setScore(2);
		
		Team space = board.registerNewTeam("space");
		space.addEntry(ChatColor.GRAY.toString());
		space.setPrefix("");
		obj.getScore(ChatColor.GRAY.toString()).setScore(3);
		
		Team online = board.registerNewTeam("online");
		online.addEntry(ChatColor.RESET.toString());
		
		String msg1 = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.lobby.online").replace("%online%", Bukkit.getOnlinePlayers().length + ""));
		setLine(online, msg1);
		obj.getScore(ChatColor.RESET.toString()).setScore(5);
		
		
		obj.getScore(ChatColor.RED.toString()).setScore(4);
		
		Team players;
		
		if(board.getTeam("players") != null) {
			players = board.getTeam("players");
		} else {
			players = board.registerNewTeam("players");
		}
		
		players.setPrefix(ChatColor.WHITE.toString());
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			players.addPlayer(p);
		}
		
		player.setScoreboard(board);
		
	}
	
	public void tdmBoard(Player player) {

		player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		if(board == null) return;

		Objective obj = board.registerNewObjective("tdm", "dummy");
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.tdm.title")));
		
		Team topWrapper = board.registerNewTeam("top");
		topWrapper.addEntry(ChatColor.WHITE.toString());
		
		String topLine = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.tdm.line"));
		
		setLine(topWrapper, topLine);
		obj.getScore(ChatColor.WHITE.toString()).setScore(8);
		
		Team bottomWrapper = board.registerNewTeam("bottom");
		bottomWrapper.addEntry(ChatColor.BLACK.toString());
		setLine(bottomWrapper, topLine);
		obj.getScore(ChatColor.BLACK.toString()).setScore(1);
		
		Team info = board.registerNewTeam("info");
		info.addEntry(ChatColor.AQUA.toString());
		String msg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.tdm.info"));
		
		setLine(info, msg);
		obj.getScore(ChatColor.AQUA.toString()).setScore(2);
		
		Team blue = board.registerNewTeam("blue");
		blue.addEntry(ChatColor.DARK_BLUE.toString());
		String bmsg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.tdm.blue").replace("%players%", "" + TDM.getTeam1().size()).replace("%win%", "" + tdm.getWin()).replace("%score%", TDM.getScore1() + ""));
		
		setLine(blue, bmsg);
		obj.getScore(ChatColor.DARK_BLUE.toString()).setScore(7);
		
		Team green = board.registerNewTeam("green");
		green.addEntry(ChatColor.GREEN.toString());
		String gmsg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.tdm.green").replace("%players%", "" + TDM.getTeam2().size()).replace("%win%", "" + tdm.getWin()).replace("%score%", TDM.getScore2() + ""));
		
		setLine(green, gmsg);
		
		obj.getScore(ChatColor.GREEN.toString()).setScore(6);
		
		Team timer = board.registerNewTeam("timer");
		timer.addEntry(ChatColor.LIGHT_PURPLE.toString());
		
		if(TDM.isStarted()) {
			String timerMsg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.tdm.game-timer").replace("%timer%", "" + TDM.getGameTimer()));
			setLine(timer, timerMsg);
		} else {
			String timerMsg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.tdm.start-timer").replace("%timer%", "" + TDM.getTimer()));
			setLine(timer, timerMsg);
		}
		
		obj.getScore(ChatColor.LIGHT_PURPLE.toString()).setScore(4);
		
		Team space = board.registerNewTeam("space");
		space.addEntry(ChatColor.GRAY.toString());
		space.setPrefix("");
		obj.getScore(ChatColor.GRAY.toString()).setScore(5);
		
		Team space2 = board.registerNewTeam("space2");
		space2.addEntry(ChatColor.DARK_PURPLE.toString());
		space2.setPrefix("");
		obj.getScore(ChatColor.DARK_PURPLE.toString()).setScore(3);
		
		
		Team team1;
		
		if(board.getTeam("team1") != null) {
			team1 = board.getTeam("team1");
		} else {
			team1 = board.registerNewTeam("team1");
		}
		
		team1.setPrefix(ChatColor.BLUE.toString());
		
		Team team2;
		
		if(board.getTeam("team2") != null) {
			team2 = board.getTeam("team2");
		} else {
			team2 = board.registerNewTeam("team2");
		}
		
		team2.setPrefix(ChatColor.RED.toString());
		
		
		for(UUID uuid : TDM.getTeam1()) {
			
			Player p = Bukkit.getPlayer(uuid);
			
			if(p==null) continue;
			
			if(!team1.getPlayers().contains(p)) {
				team1.addPlayer(p);
			}
			
		}
		
		for(UUID uuid : TDM.getTeam2()) {
			
			Player p = Bukkit.getPlayer(uuid);
			
			if(p==null) continue;
			
			if(!team2.getPlayers().contains(p)) {
				team2.addPlayer(p);
			}
			
		}
		
		player.setScoreboard(board);
		
	}
	
	public void ffaBoard(Player player) {
		
		player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		if(board == null) return;

		if(!LMSManager.getManager().isPlayerInLMS(player)) {
			return;
		}
		
		LMS lms = LMSManager.getManager().getLMSByPlayer(player);
		
		Objective obj = board.registerNewObjective("ffa", "dummy");
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.ffa.title")));
		
		Team topWrapper = board.registerNewTeam("top");
		topWrapper.addEntry(ChatColor.WHITE.toString());
		
		String topLine = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.ffa.line"));
		
		setLine(topWrapper, topLine);
		obj.getScore(ChatColor.WHITE.toString()).setScore(6);
		
		Team bottomWrapper = board.registerNewTeam("bottom");
		bottomWrapper.addEntry(ChatColor.BLACK.toString());
		setLine(bottomWrapper, topLine);
		obj.getScore(ChatColor.BLACK.toString()).setScore(1);
		
		Team info = board.registerNewTeam("info");
		info.addEntry(ChatColor.AQUA.toString());
		
		String msg = "";
		
		if(!lms.isActive()) {
			msg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.ffa.time").replace("%time%", utils.formatTime(lms.getTimer())));
		} else {
			msg = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.ffa.info"));
		}
		
		setLine(info, msg);
		
		obj.getScore(ChatColor.AQUA.toString()).setScore(2);
		
		Team space = board.registerNewTeam("space");
		space.addEntry(ChatColor.GRAY.toString());
		space.setPrefix("");
		obj.getScore(ChatColor.GRAY.toString()).setScore(3);
		
		Team alive = board.registerNewTeam("alive");
		alive.addEntry(ChatColor.RESET.toString());
		
		String msg1 = ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("scoreboard.ffa.alive").replace("%alive%",  lms.getPlayers().size() + ""));
		setLine(alive, msg1);
		obj.getScore(ChatColor.RESET.toString()).setScore(5);
		
		
		obj.getScore(ChatColor.RED.toString()).setScore(4);
		
		player.setScoreboard(board);
		
	}
	
	public void setLine(Team team, String message) {
		
		String prefix = "";
		String suffix = "";
		
		if(message.length() > 16) {
			
			prefix = message.substring(0, 16);
			suffix = ChatColor.getLastColors(prefix) + message.substring(16, message.length());
			
			if(suffix.length() > 16) {
				suffix = suffix.substring(0, 16);
			} 
			
		} else {
			prefix = message.substring(0, message.length());
		}
		
		team.setPrefix(prefix);
		team.setSuffix(suffix);
		
	}
}
