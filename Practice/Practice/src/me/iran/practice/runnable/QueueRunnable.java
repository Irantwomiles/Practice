package me.iran.practice.runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.iran.practice.utils.PlayerQueues;

public class QueueRunnable extends BukkitRunnable {

	private PlayerQueues queue = new PlayerQueues();
	
	@SuppressWarnings("deprecation")
	public void run() {
		
		queue.findUnrankedMatch();
		
		queue.findPremiumMatch();
		
		queue.findRankedMatch();
		
		queue.findPartyUnrankedMatch();
		
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(PlayerQueues.getIncrement().containsKey(p.getUniqueId())) {
				PlayerQueues.getIncrement().put(p.getUniqueId(), PlayerQueues.getIncrement().get(p.getUniqueId()) + 5);
			}
		}
	}
	
}
