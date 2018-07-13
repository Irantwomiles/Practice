package me.iran.practice.events.lms;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.iran.practice.Practice;
import me.iran.practice.arena.Arena;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.gametype.GameType;
import me.iran.practice.party.PartyManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.spectator.Spectator;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LMSManager {

	@Getter
	private static ArrayList<LMS> lmsList = new ArrayList<>();
	
    private static LMSManager manager;
    
    private Spectator spec = new Spectator();
    
	private LMSManager() {}

	public static LMSManager getManager() {
		if (manager == null)
			manager = new LMSManager();

		return manager;
	}
	
	@SuppressWarnings("deprecation")
	public void start(Player player, Arena arena, GameType game) {
		
		if(ArenaManager.getAvailableArenas().size() > 0) {
			LMS lms = new LMS(arena, game);
			
			lmsList.add(lms);
			
			TextComponent playerName = new TextComponent(player.getDisplayName());

			TextComponent message = new TextComponent(" has started an LMS Event");
			message.setColor(ChatColor.GRAY);
			message.setBold(true);
			
			TextComponent join = new TextComponent(" (JOIN) ");
			join.setColor(ChatColor.GREEN);
			join.setBold(true);
			join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lms join " + arena.getId()));
			join.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Click to Join LMS" ).create() ));
			
			playerName.addExtra(message);
			playerName.addExtra(join);
			
			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				p.spigot().sendMessage(playerName);
			}
			
			join(player, lms.getId());
			
		} else {
			player.sendMessage(ChatColor.RED + "No arenas available");
		}
		
	}
	
	public void join(final Player player, int id) {
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(profile.getState() != PlayerState.LOBBY) {
			player.sendMessage(ChatColor.RED + "You must be in the Lobby to join an LMS event!");
			return;
		}
		
		if(PartyManager.getManager().isPlayerInParty(player.getUniqueId())) {
			player.sendMessage(ChatColor.RED + "Can't do that while in a party!");
			return;
		}
		
		final LMS lms = getLMSById(id);
		
		if(lms == null) {
			return;
		}
		
		if(!lms.getPlayers().contains(player.getUniqueId()) && !lms.isActive()) {
			
			if(lms.getPlayers().size() >= lms.getMax()) {
				player.sendMessage(ChatColor.RED + "This event is currently full, try joining another one!");
				return;
			}
			
			if(lms.getTimer() <= 5) {
				player.sendMessage(ChatColor.RED + "Can't join this event now, try another!");
				return;
			}
			
			lms.getPlayers().add(player.getUniqueId());
			
			sendMessage(lms, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("join-lms").replace("%count%", lms.getPlayers().size() + "").replace("%player%", player.getName())));
			
			player.teleport(lms.getArena().getLoc1());
			
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			
			player.setHealth(20.0);
			player.setFoodLevel(20);
			
			profile.setState(PlayerState.IN_EVENT);
			
			Bukkit.getServer().getPluginManager().callEvent(new LMSJoin(lms, player));
		}
		
	}
	
	public void leave(Player player) {
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
	
		if(profile.getState() != PlayerState.IN_EVENT) {
			player.sendMessage(ChatColor.RED + "You are not in a event");
			return;
		}
		
		LMS lms = getLMSByPlayer(player);
		
		if(lms == null) {
			return;
		}
		
		if(lms.getPlayers().contains(player.getUniqueId())) {
			
			lms.getPlayers().remove(player.getUniqueId());
			
			Bukkit.getServer().getPluginManager().callEvent(new LeaveLMS(lms));
			
			sendMessage(lms, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("leave-lms").replace("%count%", lms.getPlayers().size() + "").replace("%player%", player.getName())));
			
			profile.setState(PlayerState.LOBBY);
			
			Bukkit.getServer().getPluginManager().callEvent(new LMSLeave(lms, player));
			
			Practice.getInstance().teleportSpawn(player);
		}
	}
	
	public void remove(Player player) {
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
	
		if(profile.getState() != PlayerState.IN_EVENT) {
			player.sendMessage(ChatColor.RED + "You are not in a event");
			return;
		}
		
		LMS lms = getLMSByPlayer(player);
		
		if(lms == null) {
			return;
		}
		
		if(lms.getPlayers().contains(player.getUniqueId())) {
			
			lms.getPlayers().remove(player.getUniqueId());
			
			Bukkit.getServer().getPluginManager().callEvent(new LeaveLMS(lms));
			
			sendMessage(lms, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("death-lms").replace("%count%", lms.getPlayers().size() + "").replace("%player%", player.getName())));
			
			profile.setState(PlayerState.LOBBY);
			
			Bukkit.getServer().getPluginManager().callEvent(new LMSLeave(lms, player));
			
			Practice.getInstance().teleportSpawn(player);
		}
	}
	
	public void disconnect(Player player) {
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(profile == null) {
			return;
		}
		
		LMS lms = getLMSByPlayer(player);
		
		if(lms == null) {
			return;
		}
		
		if(lms.getPlayers().contains(player.getUniqueId())) {
			
			lms.getPlayers().remove(player.getUniqueId());
			
			Bukkit.getServer().getPluginManager().callEvent(new LMSLeave(lms, player));
			
			sendMessage(lms, ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("leave-lms").replace("%count%", lms.getPlayers().size() + "").replace("%player%", player.getName())));
		}
	}
	
	public void forceEnd(int id) {
		
		LMS lms = getLMSById(id);

		if (lms == null) {
			return;
		}
		
		for(UUID uuid : lms.getPlayers()) {
			
			Player player = Bukkit.getPlayer(uuid);
			
			if(player == null) continue;
			
			Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
			
			profile.setState(PlayerState.LOBBY);
			
			Bukkit.getServer().getPluginManager().callEvent(new LMSLeave(lms, player));
			
			Practice.getInstance().teleportSpawn(player);
			
		}
		
		for(int i = 0; i < lms.getArena().getSpectators().size(); i++) {
			
			Player player = Bukkit.getPlayer(lms.getArena().getSpectators().get(i));
			
			if(player == null) continue;
			
			spec.leaveSpectator(player);
			
		}
		
		sendMessage(lms, ChatColor.RED.toString() + ChatColor.BOLD + "[LMS] Event has come to a forced end!");
		
		lms.getPlayers().clear();
		
		ArenaManager.getAvailableArenas().add(lms.getArena());
		
		getLmsList().remove(lms);
		
	}
	
	public void end(LMS lms) {
		
		for(UUID uuid : lms.getPlayers()) {
			
			Player player = Bukkit.getPlayer(uuid);
			
			if(player == null) continue;
			
			Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
			
			profile.setState(PlayerState.LOBBY);
			
			Bukkit.getServer().getPluginManager().callEvent(new LMSLeave(lms, player));
			
			Practice.getInstance().teleportSpawn(player);
			
		}
		
		for(int i = 0; i < lms.getArena().getSpectators().size(); i++) {
			
			Player player = Bukkit.getPlayer(lms.getArena().getSpectators().get(i));
			
			if(player == null) continue;
			
			spec.leaveSpectator(player);
			
		}
		
		lms.getPlayers().clear();
		ArenaManager.getAvailableArenas().add(lms.getArena());
		getLmsList().remove(lms);
		
	}
	
	public void sendMessage(LMS lms, String msg) {
		for(UUID uuid : lms.getPlayers()) {
			
			Player player = Bukkit.getPlayer(uuid);
			
			if(player != null) {
				player.sendMessage(msg);
			}
			
		}
	}
	
	public LMS getLMSByPlayer(Player player) {
		for(LMS lms : lmsList) {
			if(lms.getPlayers().contains(player.getUniqueId())) {
				return lms;
			}
		}
		return null;
	}
	
	public LMS getLMSByArena(String arena) {
		for(LMS lms : lmsList) {
			if(lms.getArena().getId().equals(arena)) {
				return lms;
			}
		}
		return null;
	}
	
	public LMS getLMSById(int id) {
		for(LMS lms : lmsList) {
			if(lms.getId() == id) {
				return lms;
			}
		}
		return null;
	}
	
	public boolean isPlayerInLMS(Player player) {
		for(LMS lms : lmsList) {
			if(lms.getPlayers().contains(player.getUniqueId())) {
				return true;
			}
		}
		return false;
	}
	
}
