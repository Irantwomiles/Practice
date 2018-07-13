package me.iran.practice.runnable;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.iran.practice.events.lms.LMS;
import me.iran.practice.events.lms.LMSListeners;
import me.iran.practice.events.lms.LMSManager;
import me.iran.practice.kit.KitManager;
import net.md_5.bungee.api.ChatColor;

public class EventsRunnable extends BukkitRunnable {

	@SuppressWarnings("deprecation")
	public void run() {
		
		for(int i = 0; i < LMSManager.getLmsList().size(); i++) {
			
			LMS lms = LMSManager.getLmsList().get(i);
			
			if(lms.getTimer() > 0) {
				
				lms.setTimer(lms.getTimer() - 1);
				
				if(lms.getTimer() <= 0) {
					
					if(lms.getPlayers().size() >= lms.getMin()) {
						
						LMSManager.getManager().sendMessage(lms, ChatColor.YELLOW + "[Event] " + ChatColor.GRAY + "LMS event has started!");
						
						lms.setActive(true);
						lms.setTimer(-1);
					} else {
						LMSManager.getManager().forceEnd(lms.getId());
					}
					
				} else if(lms.getTimer() == 30) {
					LMSManager.getManager().sendMessage(lms, ChatColor.YELLOW + "[Event] " + ChatColor.GRAY + "LMS event will start in 30 seconds!");
				} else if(lms.getTimer() == 15) {
					LMSManager.getManager().sendMessage(lms, ChatColor.YELLOW + "[Event] " + ChatColor.GRAY + "LMS event will start in 15 seconds!");
				} else if(lms.getTimer() == 10) {
					LMSManager.getManager().sendMessage(lms, ChatColor.YELLOW + "[Event] " + ChatColor.GRAY + "LMS event will start in 10 seconds!");
				} else if(lms.getTimer() == 5) {
					
					for(UUID uuid: lms.getPlayers()) {
						KitManager.getManager().getPlayerKits(Bukkit.getPlayer(uuid), lms.getGame());
					}
					
					LMSManager.getManager().sendMessage(lms, ChatColor.YELLOW + "[Event] " + ChatColor.GRAY + "LMS event will start in 5 seconds!");
				} else if(lms.getTimer() == 4) {
					LMSManager.getManager().sendMessage(lms, ChatColor.YELLOW + "[Event] " + ChatColor.GRAY + "LMS event will start in 4 seconds!");
				} else if(lms.getTimer() == 3) {
					LMSManager.getManager().sendMessage(lms, ChatColor.YELLOW + "[Event] " + ChatColor.GRAY + "LMS event will start in 3 seconds!");
				} else if(lms.getTimer() == 2) {
					LMSManager.getManager().sendMessage(lms, ChatColor.YELLOW + "[Event] " + ChatColor.GRAY + "LMS event will start in 2 seconds!");
				} else if(lms.getTimer() == 1) {
					LMSManager.getManager().sendMessage(lms, ChatColor.YELLOW + "[Event] " + ChatColor.GRAY + "LMS event will start in 1 second!");
				}
			}
			
		}
		
		//Fireworks
		
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(LMSListeners.getWinners().containsKey(p.getUniqueId())) {
				
				if(LMSListeners.getWinners().get(p.getUniqueId()) > 0) {
					LMSListeners.getWinners().put(p.getUniqueId(), LMSListeners.getWinners().get(p.getUniqueId()) - 1);
					
					fireWorks(p);
				
				} else if(LMSListeners.getWinners().get(p.getUniqueId()) <= 0) {
					LMSListeners.getWinners().remove(p.getUniqueId());
				}
				
			}
		}
		
	}
	
	void fireWorks(Player player) {
		Firework f = (Firework) player.getWorld().spawn(player.getLocation(), Firework.class);
        
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                        .flicker(false)
                        .trail(true)
                        .with(Type.CREEPER)
                        .withColor(Color.GREEN)
                        .withFade(Color.BLUE)
                        .build());
        fm.setPower(3);
        f.setFireworkMeta(fm);
	}
	
}
