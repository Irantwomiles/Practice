package me.iran.practice.duel.team;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.Practice;
import me.iran.practice.gametype.GameType;
import me.iran.practice.party.Party;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class SendTeamDuel {

	@Getter
	@Setter
	private Party sender, reciever;
	
	@Getter
	@Setter
	private GameType game;
	
	public SendTeamDuel(Party sender, Party reciever, GameType game) {
		
		this.sender = sender;
		this.reciever = reciever;
		this.game = game;
		
		Player s = Bukkit.getPlayer(sender.getLeader());
		Player r = Bukkit.getPlayer(reciever.getLeader());
		
		TextComponent senderName = new TextComponent(s.getName());
		senderName.setColor(ChatColor.YELLOW);
		
		TextComponent message = new TextComponent(" has sent a duel request for Game Mode ");
		message.setColor(ChatColor.GRAY);
		
		TextComponent gameMode = new TextComponent(game.getName());
		gameMode.setColor(ChatColor.GOLD);
		
		TextComponent accept = new TextComponent(" [Accept] ");
		accept.setColor(ChatColor.GREEN);
		accept.setBold(true);
		accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + s.getName()));
		
		TextComponent deny = new TextComponent("[Deny]");
		deny.setColor(ChatColor.RED);
		deny.setBold(true);
		deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny " + s.getName()));
		
		senderName.addExtra(message);
		senderName.addExtra(gameMode);
		senderName.addExtra(accept);
		senderName.addExtra(deny);
		
		r.spigot().sendMessage(senderName);
		
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("duel-send").replace("%gametype%", game.getName()).replace("%reciever%", r.getName())));
		
	}
	
}
