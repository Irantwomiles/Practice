package me.iran.practice.duel.team.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import me.iran.practice.Practice;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.arena.listeners.ArenaAvailable;
import me.iran.practice.duel.team.TeamDuelManager;
import me.iran.practice.enums.PartyState;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.party.Party;
import me.iran.practice.party.PartyManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.spectator.Spectator;
import net.md_5.bungee.api.ChatColor;

public class TeamDuelEndEvent implements Listener {
	
	private Spectator spec = new Spectator();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void endDuel(final TeamDuelEnd event) {
		
		for(UUID uuid : event.getDuel().getPlayers()) {

			Player player = Bukkit.getPlayer(uuid);
			
			if(player != null) {

				String winner = ChatColor.GREEN.toString() + ChatColor.BOLD + "Winner \n";
				
				for(UUID id : event.getDuel().getWinner().getAllPlayers()) {

					OfflinePlayer offline = Bukkit.getOfflinePlayer(id);
					
					winner += ChatColor.YELLOW + offline.getName() + " ";
					
				}
				
				String loser = ChatColor.RED.toString() + ChatColor.BOLD + "Loser \n";
				
				for(UUID id : event.getDuel().getLoser().getAllPlayers()) {

					OfflinePlayer offline = Bukkit.getOfflinePlayer(id);
					
					loser += ChatColor.YELLOW + offline.getName() + " ";
					
				}
				
				player.sendMessage(winner);
				player.sendMessage("");
				player.sendMessage(loser);
				
				for(UUID spec : event.getDuel().getArena().getSpectators()) {
					Player p = Bukkit.getPlayer(spec);
					
					p.sendMessage(winner);
					p.sendMessage("");
					p.sendMessage(loser);
				}
				
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				
				player.setHealth(20.0);
				player.setFoodLevel(20);
				
				for(PotionEffect effect : player.getActivePotionEffects()) {
					player.removePotionEffect(effect.getType());
				}
				
				for(Item item : event.getDuel().getArena().getDrops()) {
					item.remove();
				}
			}
		}
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
			
			public void run() {
				
				for(UUID id : event.getDuel().getTeam1().getPlayers()) {

					Player player = Bukkit.getPlayer(id);
					
					Party party = PartyManager.getManager().getPartyByPlayer(id);
					
					if(party != null) {
						party.setState(PartyState.LOBBY);
					}
					
					if(player != null) {
						Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
						profile.setState(PlayerState.LOBBY);
						
						player.setFireTicks(0);
						
						Practice.getInstance().teleportSpawn(player);
					}
				}
				
				for(UUID id : event.getDuel().getTeam2().getPlayers()) {

					Player player = Bukkit.getPlayer(id);
					
					Party party = PartyManager.getManager().getPartyByPlayer(id);
					
					if(party != null) {
						party.setState(PartyState.LOBBY);
					}
					
					if(player != null) {
						Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
						profile.setState(PlayerState.LOBBY);
						
						player.setFireTicks(0);
						
						Practice.getInstance().teleportSpawn(player);
					}
				}
				
				for(Player p : Bukkit.getServer().getOnlinePlayers()) {
					if(event.getDuel().getArena().getSpectators().contains(p.getUniqueId())) {
						spec.leaveSpectator(p);
					}
				}
				
				ArenaManager.getAvailableArenas().add(event.getDuel().getArena());
				
				Bukkit.getServer().getPluginManager().callEvent(new ArenaAvailable(event.getDuel().getArena()));
				
				TeamDuelManager.getDuelSet().remove(event.getDuel());
			}
			
		}, 60);
		
	}
	
}
