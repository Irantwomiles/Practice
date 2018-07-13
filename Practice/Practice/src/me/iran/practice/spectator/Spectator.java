package me.iran.practice.spectator;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import me.iran.practice.Practice;
import me.iran.practice.arena.Arena;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.duel.SoloDuel;
import me.iran.practice.duel.SoloDuelManager;
import me.iran.practice.duel.team.TeamDuel;
import me.iran.practice.duel.team.TeamDuelManager;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.events.lms.LMS;
import me.iran.practice.events.lms.LMSManager;
import me.iran.practice.events.tdm.TDM;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.utils.PlayerItems;
import net.md_5.bungee.api.ChatColor;

public class Spectator {

	private PlayerItems items = new PlayerItems();
	
	private TDM tdm = new TDM();
	
	public void setSpectator(final Player player, final Player target) {
		
		Profile playerProfile = ProfileManager.getManager().getProfileByPlayer(player);
		
		Profile targetProfile = ProfileManager.getManager().getProfileByPlayer(target);
		
		if(playerProfile.getState() == PlayerState.LOBBY || playerProfile.getState() == PlayerState.SPECTATOR) {
			
			if(tdm.isPlayerInTDM(player)) {
				
				playerProfile.setState(PlayerState.SPECTATOR);
				
				for(UUID uuid : TDM.getTeam1()) {
					
					Player p = Bukkit.getPlayer(uuid);
					
					p.hidePlayer(player);
				}
				
				for(UUID uuid : TDM.getTeam2()) {
					
					Player p = Bukkit.getPlayer(uuid);
					
					p.hidePlayer(player);
				}
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
					public void run() {

						if(TDM.getTeam1().contains(player.getUniqueId())) {
							player.teleport(TDM.getLoc1());
						} else if(TDM.getTeam2().contains(player.getUniqueId())) {
							player.teleport(TDM.getLoc2());
						}
						
					}
				}, 1);
				
				player.setAllowFlight(true);
				
				player.setGameMode(GameMode.CREATIVE);
				return;
			}
			
			if(targetProfile.getState() == PlayerState.IN_GAME) {
				
				if(SoloDuelManager.getManager().isPlayerInDuel(target)) {
					
					SoloDuel duel = SoloDuelManager.getManager().getDuelByPlayer(target);
					
					duel.getArena().getSpectators().add(player.getUniqueId());
					
					playerProfile.setState(PlayerState.SPECTATOR);
					
					items.spectatorItems(player);
					
					duel.getPlayer1().hidePlayer(player);
					duel.getPlayer2().hidePlayer(player);
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
						public void run() {
							player.teleport(target.getLocation());
						}
					}, 1);
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("spectate-message-player").replace("%player%", target.getName())));
					
					if(targetProfile.isSpectatorMessage()) {
						target.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("spectate-message-target").replace("%player%", player.getName())));
					}
					
					player.setAllowFlight(true);
				
					player.setGameMode(GameMode.CREATIVE);
					
				} else if(TeamDuelManager.getManager().isPlayerInDuel(target)) {
					
					TeamDuel duel = TeamDuelManager.getManager().getDuelByPlayer(target);
					
					duel.getArena().getSpectators().add(player.getUniqueId());
					
					playerProfile.setState(PlayerState.SPECTATOR);
					
					items.spectatorItems(player);
					
					for(UUID uuid : duel.getAlivePlayers()) {
						
						Player p = Bukkit.getPlayer(uuid);
						
						p.hidePlayer(player);
						
					}
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
						public void run() {
							player.teleport(target.getLocation());
						}
					}, 1);
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("spectate-message-player").replace("%player%", target.getName())));
					
					if(targetProfile.isSpectatorMessage()) {
						target.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("spectate-message-target").replace("%player%", player.getName())));
					}
					
					player.setAllowFlight(true);
					
					player.setGameMode(GameMode.CREATIVE);
				
				}
				
				
			} else {
				player.sendMessage(ChatColor.RED + "That player is currently not fighting anyone!");
			}
			
			if(targetProfile.getState() == PlayerState.IN_EVENT) {
				if(LMSManager.getManager().isPlayerInLMS(target)) {
					
					LMS lms = LMSManager.getManager().getLMSByPlayer(target);
					
					lms.getArena().getSpectators().add(player.getUniqueId());

					playerProfile.setState(PlayerState.SPECTATOR);
					
					items.spectatorItems(player);
					
					for(UUID uuid : lms.getPlayers()) {
						
						Player p = Bukkit.getPlayer(uuid);
						
						p.hidePlayer(player);
					}
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
						public void run() {
							player.teleport(target.getLocation());
						}
					}, 1);
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("spectate-message-player").replace("%player%", target.getName())));
					
					if(targetProfile.isSpectatorMessage()) {
						target.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("spectate-message-target").replace("%player%", player.getName())));
					}
					
					player.setAllowFlight(true);
					
					player.setGameMode(GameMode.CREATIVE);
					return;
				}
			}
			
		} else {
			player.sendMessage(ChatColor.RED + "Can't go into spectator mode while in " + playerProfile.getState().toString() + " state!");
		}
		
	}
	
	public void leaveSpectator(final Player player) {
		
		Profile playerProfile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(playerProfile.getState() == PlayerState.SPECTATOR) {
			
			if(tdm.isPlayerInTDM(player)) {
				
				if(TDM.getTeam1().contains(player.getUniqueId())) {
					player.teleport(TDM.getLoc1());
				} else if(TDM.getTeam2().contains(player.getUniqueId())) {
					player.teleport(TDM.getLoc2());
				}
				
				for(UUID uuid : TDM.getTeam1()) {
					
					Player p = Bukkit.getPlayer(uuid);
					
					p.showPlayer(player);
					
					player.showPlayer(p);
				}
				
				for(UUID uuid : TDM.getTeam2()) {
					
					Player p = Bukkit.getPlayer(uuid);
					
					p.showPlayer(player);
					
					player.showPlayer(p);
				}
				
				playerProfile.setState(PlayerState.IN_EVENT);
				
				player.setGameMode(GameMode.SURVIVAL);
				
				player.setAllowFlight(false);
				
				player.setHealth(20.0);
				player.setFoodLevel(20);
				
				return;
			}
			
			if(ArenaManager.getManager().getArenaBySpectator(player.getUniqueId()) == null) {
				return;
			}

			Arena arena = ArenaManager.getManager().getArenaBySpectator(player.getUniqueId());
			
			if(TeamDuelManager.getManager().isPlayerInDuel(player)) {
				
				TeamDuel duel = TeamDuelManager.getManager().getDuelByPlayer(player);
				
				/*if(duel.getTeam1().getPlayers().contains(player.getUniqueId())) {
					duel.getTeam1().getPlayers().remove(player.getUniqueId());
				}
				
				if(duel.getTeam2().getPlayers().contains(player.getUniqueId())) {
					duel.getTeam2().getPlayers().remove(player.getUniqueId());
				}*/
				
				for(UUID uuid : duel.getPlayers()) {
					Player p = Bukkit.getPlayer(uuid);
					
					if(p != null) {
						p.showPlayer(player);
					}
				}
				
			}
			
			if(SoloDuelManager.getManager().getDuelByArena(arena.getId()) != null) {
				
				SoloDuel duel = SoloDuelManager.getManager().getDuelByArena(arena.getId());
				
				duel.getPlayer1().showPlayer(player);
				duel.getPlayer2().showPlayer(player);
			}
			
			if(LMSManager.getManager().getLMSByArena(arena.getId()) != null) {
				
				LMS lms = LMSManager.getManager().getLMSByArena(arena.getId());
				
				for(UUID uuid : lms.getPlayers()) {
					
					Player p = Bukkit.getPlayer(uuid);
					
					p.showPlayer(player);
					player.showPlayer(p);
					
				}
				
			}
			
			playerProfile.setState(PlayerState.LOBBY);
			
			player.setGameMode(GameMode.SURVIVAL);
			
			player.setAllowFlight(false);
			
			player.setHealth(20.0);
			player.setFoodLevel(20);
			
			ArenaManager.getManager().getArenaBySpectator(player.getUniqueId()).getSpectators().remove(player.getUniqueId());
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
				public void run() {
					Practice.getInstance().teleportSpawn(player);
				}
			}, 1);
		}
	}
	
}
