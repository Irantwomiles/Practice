package me.iran.practice.utils;

import java.util.Random;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import me.iran.practice.Practice;
import me.iran.practice.profile.Profile;
import net.md_5.bungee.api.ChatColor;

public class Utils {

	public String formatTime(int timer) {
		
		String msg = "";

		int minute = 0;
		int second = 0;
		
		minute = (timer/60);
		second = (timer%60);
		
		msg = minute + ":";
		
		if(second < 10) {
			msg = msg + "0" + second;
		} else {
			msg = msg + second;
		}
		
		return msg;
	}
	
	public String formatEffect(PotionEffect effect) {
		
		String msg = "";
		
		String name = effect.getType().getName().toLowerCase();
		
		if(name.contains("_")) {
			
			String[] split = name.split("_");
			
			for(int i = 0; i < split.length; i++) {
				
				msg = msg + " " + Character.toUpperCase(split[i].charAt(0)) + split[i].substring(1);
				
			}
		} else {
			msg = " " + Character.toUpperCase(name.charAt(0)) + name.substring(1);
		}
		
		int amp = effect.getAmplifier() + 1;
		
		String amplifier = "";
		
		for(int i = 0; i < amp; i++) {
			amplifier = amplifier + "I";
		}
		
		msg = msg + " " + amplifier;
		
		return msg;
	}

	public void randomToken(Player player, Profile profile) {
		
		if(profile == null) {
			return;
		}
		
		Random random = new Random();
		
		int rand = random.nextInt(100) + 1;
		
		if(rand >= 1 && rand <= Practice.getInstance().getConfig().getInt("token-percent")) {
			
			profile.setToken(profile.getToken() + 1);
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("random-token")));
			
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 10L, 10L);
			
		}
		
	}
	
	public void randomPremium(Player player, Profile profile) {
		
		if(profile == null) {
			return;
		}
		
		Random random = new Random();
		
		int rand = random.nextInt(100) + 1;
		
		if(rand >= 1 && rand <= Practice.getInstance().getConfig().getInt("premium-percent")) {
			
			profile.setPremium(profile.getPremium() + 5);
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("random-premium")));
			
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 10L, 10L);
			
		}
		
	}
}
