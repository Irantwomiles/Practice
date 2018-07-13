package me.iran.practice.events.tdm;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.iran.practice.Practice;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.kit.KitManager;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.spectator.Spectator;
import net.md_5.bungee.api.ChatColor;

public class TDMManager extends BukkitRunnable implements Listener {

	private static HashMap<UUID, Integer> tdmCooldown = new HashMap<>();
	
	private TDM tdm = new TDM();
	
	private Spectator spec = new Spectator();
	
	@SuppressWarnings("deprecation")
	public void run() {
		
		if(TDM.isStarted()) {
			TDM.setGameTimer(TDM.getGameTimer() + 1);
		}
		
		if(TDM.isActive() && TDM.getTimer() > 0) {
			TDM.setTimer(TDM.getTimer() - 1);
		
			if(TDM.getTimer() == 0) {
				
				if(TDM.getTeam1().size() < TDM.getMin() || TDM.getTeam2().size() < TDM.getMin()) {
					
					tdm.forceEndGame(ChatColor.RED + "TDM did not reach its minimum number of players!");
					return;
				}
				
				TDM.setTimer(-1);
				TDM.setStarted(true);
				Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "[TDM]" + ChatColor.GRAY + " The event has begun!");
				
				for(UUID uuid : TDM.getTeam1()) {
					Player p = Bukkit.getPlayer(uuid);
					if(p!=null) {
						p.teleport(TDM.getLoc1());
						KitManager.getManager().getPlayerKits(p, TDM.getGame());
					}
				}
				
				for(UUID uuid : TDM.getTeam2()) {
					Player p = Bukkit.getPlayer(uuid);
					if(p!=null) {
						p.teleport(TDM.getLoc2());
						KitManager.getManager().getPlayerKits(p, TDM.getGame());
					}
				}
			}
		}
		
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			
			if(tdmCooldown.containsKey(p.getUniqueId())) {
				
				tdmCooldown.put(p.getUniqueId(), tdmCooldown.get(p.getUniqueId()) - 1);
				
				if(tdmCooldown.get(p.getUniqueId()) <= 0) {
					tdmCooldown.remove(p.getUniqueId());
					
					if(tdm.isPlayerInTDM(p)) {
					
						spec.leaveSpectator(p);
						
						KitManager.getManager().getPlayerKits(p, TDM.getGame());
						
					}
					
				}
			}
			
		}
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		if(tdm.isPlayerInTDM(player)) {
			tdm.leaveTDM(player);
		}
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		
		Player player = event.getEntity();
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(tdm.isPlayerInTDM(player)) {

			if(event.getEntity().getKiller() instanceof Player) {
				event.getEntity().getKiller().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 1));
			}
			
			profile.setState(PlayerState.SPECTATOR);
			
			event.getDrops().clear();
			
			player.spigot().respawn();
			
			spec.setSpectator(player, player);
			
			if(TDM.team1.contains(player.getUniqueId())) {
				TDM.score2++;
				
				tdmCooldown.put(player.getUniqueId(), 5);
				tdm.check();

				tdm.sendTeamMessage(TDM.getTeam1(), ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("tdm-team1-death").replace("%player%", player.getName())) );
				tdm.sendTeamMessage(TDM.getTeam2(), ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("tdm-team1-death").replace("%player%", player.getName())));
			
			} else {
				TDM.score1++;
				
				tdmCooldown.put(player.getUniqueId(), 5);
				tdm.check();
				
				tdm.sendTeamMessage(TDM.getTeam1(), ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("tdm-team2-death").replace("%player%", player.getName())) );
				tdm.sendTeamMessage(TDM.getTeam2(), ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("tdm-team2-death").replace("%player%", player.getName())));
			}
			
			player.sendMessage(ChatColor.GRAY + "You are now on a cooldown timer, you will automatically respawn in " + ChatColor.YELLOW + "5" + ChatColor.GRAY + " seconds!");
			
		}
		
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		
		if(event.getEntity() instanceof Player) {
			
			Player player = (Player) event.getEntity();
			
			if(tdm.isPlayerInTDM(player)) {
		
				if(!TDM.isStarted()) {
					event.setCancelled(true);
				}
				
			}
			
			if(event.getDamager() instanceof Player) {
				
				Player damager = (Player) event.getDamager();
				
				if(tdm.isPlayerInTDM(damager) && tdm.isPlayerInTDM(player)) {
					
					if(tdm.getPlayerTeam(damager).contains(player.getUniqueId())) {
						event.setCancelled(true);
					}
					
					if(!TDM.isStarted()) {
						event.setCancelled(true);
					}
					
				}
			}

		}
	}

	@EventHandler
	public void onFood(FoodLevelChangeEvent event) {
		
		if(event.getEntity() instanceof Player) {

			Player player = (Player) event.getEntity();
			
			if(tdm.isPlayerInTDM(player)) {
				
				if(!TDM.isStarted()) {
					event.setCancelled(true);
				}
				
			}
		}
	}
	
	@EventHandler
	public void onEnd(EndTDM event) {
		ProfileManager.getManager().getProfileByPlayer(event.getPlayer()).setState(PlayerState.LOBBY);
	}
	
	@EventHandler
	public void onLeave(LeaveTDM event) {
		Practice.getInstance().teleportSpawn(event.getPlayer());
	}
	
}
