package me.iran.practice.duel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import lombok.Getter;
import me.iran.practice.arena.Arena;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.duel.listeners.DuelEnd;
import me.iran.practice.duel.listeners.DuelStart;
import me.iran.practice.enums.DuelType;
import me.iran.practice.gametype.GameType;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.utils.PlayerQueues;
import me.iran.practice.utils.StoreInventory;
import me.iran.practice.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class SoloDuelManager {

	private Utils util = new Utils();
	
	@Getter
    private static Set<SoloDuel> duelSet = new HashSet<>();

    private static SoloDuelManager dm;

	private SoloDuelManager() {}

	public static SoloDuelManager getManager() {
		if (dm == null)
			dm = new SoloDuelManager();

		return dm;
	}
	
	public void createSoloDuel(Player player1, Player player2, GameType game, Arena arena) {
		
		if(!(canPlayerDuel(player1) && canPlayerDuel(player2))) {
			return;
		}
		
		if(ArenaManager.getAvailableArenas().size() < 1) {
			player1.sendMessage(ChatColor.RED + "Sorry, we couldn't start your duel with " + player2.getName() + " because all of the arenas are busy right now!");
			player2.sendMessage(ChatColor.RED + "Sorry, we couldn't start your duel with " + player1.getName() + " because all of the arenas are busy right now!");
			
			return;
		}
		
		SoloDuel duel = new SoloDuel(player1, player2, game, arena);

		duel.setType(DuelType.SOLO_UNRANKED);

		duelSet.add(duel);

		ArenaManager.getAvailableArenas().remove(duel.getArena());

		Bukkit.getServer().getPluginManager().callEvent(new DuelStart(duel));

	}
	
	public void createRankedSoloDuel(Player player1, Player player2, GameType game, Arena arena) {
		
		if(!(canPlayerDuel(player1) && canPlayerDuel(player2))) {
			return;
		}
		
		if(ArenaManager.getAvailableArenas().size() < 1) {
			player1.sendMessage(ChatColor.RED + "Sorry, we couldn't start your duel with " + player2.getName() + " because all of the arenas are busy right now!");
			player2.sendMessage(ChatColor.RED + "Sorry, we couldn't start your duel with " + player1.getName() + " because all of the arenas are busy right now!");
			return;
		}

		SoloDuel duel = new SoloDuel(player1, player2, game, ArenaManager.getManager().randomArena());

		duel.setType(DuelType.SOLO_RANKED);

		game.getRanked().remove(player1.getName());
		game.getRanked().remove(player2.getName());

		if(PlayerQueues.getIncrement().containsKey(player1.getUniqueId())) {
			PlayerQueues.getIncrement().remove(player1.getUniqueId());
		}
		
		if(PlayerQueues.getIncrement().containsKey(player2.getUniqueId())) {
			PlayerQueues.getIncrement().remove(player2.getUniqueId());
		}
		
		duelSet.add(duel);

		ArenaManager.getAvailableArenas().remove(duel.getArena());

		Bukkit.getServer().getPluginManager().callEvent(new DuelStart(duel));

	}
	
	public void endSoloDuel(Player winner, SoloDuel duel) {
		
		if(duel == null) {
			return;
		}
		
		duel.setWinner(winner);

		if(duel.getWinner().getUniqueId() == duel.getPlayer1().getUniqueId()) {
			duel.setLoser(duel.getPlayer2());
		}

		if(duel.getWinner().getUniqueId() == duel.getPlayer2().getUniqueId()) {
			duel.setLoser(duel.getPlayer1());
		}
		
		Profile profile1 = ProfileManager.getManager().getProfileByPlayer(duel.getWinner());
		Profile profile2 = ProfileManager.getManager().getProfileByPlayer(duel.getLoser());
				
		profile1.setKill(profile1.getKill() + 1);
		profile2.setDeath(profile1.getDeath() + 1);
		
		profile1.getStoreInv().put(duel.getWinner().getUniqueId(),
				new StoreInventory(
						duel.getWinner().getInventory().getContents(),
						duel.getWinner().getInventory().getArmorContents(),
						duel.getWinner().getFoodLevel(),
						((CraftPlayer) duel.getWinner()).getHealth()));
		
		profile2.getStoreInv().put(duel.getLoser().getUniqueId(),
				new StoreInventory(
						duel.getLoser().getInventory().getContents(),
						duel.getLoser().getInventory().getArmorContents(),
						duel.getLoser().getFoodLevel(),
						((CraftPlayer) duel.getLoser()).getHealth()));
		
		
		for(Iterator<PotionEffect> it = duel.getWinner().getActivePotionEffects().iterator(); it.hasNext();) {
			
			PotionEffect potion = it.next();

			int timer = (potion.getDuration()/20);
			
			String info = ChatColor.YELLOW + util.formatEffect(potion) + " " + util.formatTime(timer);
			
			profile1.getStoreInv().get(duel.getWinner().getUniqueId()).getEffects().add(info);
		}
		
		for(Iterator<PotionEffect> it = duel.getLoser().getActivePotionEffects().iterator(); it.hasNext();) {
			
			PotionEffect potion = it.next();
			
			int timer = (potion.getDuration()/20);
			
			String info = ChatColor.YELLOW + util.formatEffect(potion) + " " + util.formatTime(timer);
			
			profile2.getStoreInv().get(duel.getLoser().getUniqueId()).getEffects().add(info);
		}
		
		Bukkit.getServer().getPluginManager().callEvent(new DuelEnd(duel));
		
	}
	
	public boolean canPlayerDuel(Player player) {
		for(SoloDuel duel : duelSet) {
			if(duel.getPlayer1().getName().equalsIgnoreCase(player.getName()) 
					|| duel.getPlayer2().getName().equalsIgnoreCase(player.getName())) {
				return false;
			}
		}
		return true;
	}
	
	public SoloDuel getDuelByPlayer(Player player) {
		for(SoloDuel duel : duelSet) {
			if(duel.getPlayer1().getName().equalsIgnoreCase(player.getName()) || duel.getPlayer2().getName().equalsIgnoreCase(player.getName())) {
				return duel;
			}
		}
		return null;
	}
	
	public SoloDuel getDuelByArena(String id) {
		for(SoloDuel duel : duelSet) {
			if(duel.getArena().getId().equalsIgnoreCase(id)) {
				return duel;
			}
		}
		return null;
	}
	
	public boolean isPlayerInDuel(Player player) {
		for(SoloDuel duel : duelSet) {
			if(duel.getPlayer1().getName().equalsIgnoreCase(player.getName()) 
					|| duel.getPlayer2().getName().equalsIgnoreCase(player.getName())) {
				return true;
			}
		}
		return false;
	}
		
}
