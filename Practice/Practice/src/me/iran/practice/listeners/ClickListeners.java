package me.iran.practice.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import lombok.Getter;
import me.iran.practice.Practice;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.cmd.DuelCommand;
import me.iran.practice.duel.SendDuel;
import me.iran.practice.enums.PlayerState;
import me.iran.practice.events.lms.LMSManager;
import me.iran.practice.events.tdm.TDM;
import me.iran.practice.gametype.GameType;
import me.iran.practice.gametype.GameTypeManager;
import me.iran.practice.kit.KitManager;
import me.iran.practice.party.PartyManager;
import me.iran.practice.profile.Elo;
import me.iran.practice.profile.Profile;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.utils.PlayerInventories;
import me.iran.practice.utils.PlayerItems;
import me.iran.practice.utils.PlayerQueues;
import net.md_5.bungee.api.ChatColor;

public class ClickListeners implements Listener {

	@Getter
	private static HashMap<String, GameType> gameSelected = new HashMap<>();
	
	@Getter
	private static Set<String> naming = new HashSet<>();
	
	private PlayerInventories inv = new PlayerInventories();
	
	private PlayerQueues queue = new PlayerQueues();
	
	private PlayerItems items = new PlayerItems();
	
	private TDM tdm = new TDM();
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		
		Player player = (Player) event.getWhoClicked();
		
		Profile profile = ProfileManager.getManager().getProfileByPlayer(player);
		
		if(profile.getState() == PlayerState.LOBBY || profile.getState() == PlayerState.SPECTATOR || profile.getState() == PlayerState.IN_QUEUE) {
				event.setCancelled(true);
		}
		
		if(event.getClickedInventory() == null) {
			return;
		}
		
		if(event.getClickedInventory().getTitle() == null) {
			return;
		}
		
		if(event.getCurrentItem() == null) {
			return;
		}
		
		if(!event.getCurrentItem().hasItemMeta()) {
			return;
		}
		
		if(event.getCurrentItem().getItemMeta().getDisplayName() == null) {
			return;
		}
		
		/*
		 * Opening on the Inventory that shows the 3 different queues (Ranked/uranked/premium)
		 */
		
		if(profile.getState() == PlayerState.EDIT) {
			return;
		}
		
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.queue")))) {
			if(profile.getState() == PlayerState.LOBBY) {
				
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				if(name.equalsIgnoreCase("ranked queue")) {
					
					if(profile.getUnrankedWon() < Practice.getInstance().getConfig().getInt("unranked-needed")) {
						player.closeInventory();
						
						int need = (Practice.getInstance().getConfig().getInt("unranked-needed") - profile.getUnrankedWon());
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("unranked-needed-message").replace("%need%", "" + need)));
						
						return;
					}
					
					player.closeInventory();
					
					inv.rankedInventory(player);
					
				} else if(name.equalsIgnoreCase("unranked queue")) {
					
					player.closeInventory();
					
					inv.unrankedInventory(player);
					
				} else if(name.equalsIgnoreCase("premium queue")) {
					
					player.closeInventory();
					
					if(profile.getPremium() > 0) {
						inv.premiumInventory(player);
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("no-premium-matches")));
						player.closeInventory();
					}
				}
			}
			event.setCancelled(true);
		}
		
		
		/*
		 * Joining ranked queue
		 */
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.ranked")))) {
			
			if(profile.getState() == PlayerState.LOBBY && !PartyManager.getManager().isPlayerInParty(player.getUniqueId())) {
				
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				GameType game = GameTypeManager.getManager().getGameType(name);
				
				queue.joinRanked(player, game);
				
				items.rankedQueueItems(player);
				
				if(!PlayerQueues.getIncrement().containsKey(player.getUniqueId())) {
					PlayerQueues.getIncrement().put(player.getUniqueId(), 1);
				}
				
				profile.setState(PlayerState.IN_QUEUE);
				
			}
			
			player.closeInventory();
			event.setCancelled(true);
		}
		
		/*
		 * Joining unranked queue
		 */
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.unranked")))) {
		
			if(profile.getState() == PlayerState.LOBBY && !PartyManager.getManager().isPlayerInParty(player.getUniqueId())) {
				
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				GameType game = GameTypeManager.getManager().getGameType(name);
				
				queue.joinUnranked(player, game);
				
				items.unrankedQueueItems(player);
				
				profile.setState(PlayerState.IN_QUEUE);
				
			}
			
			player.closeInventory();
			event.setCancelled(true);
		}
		
		/*
		 * Joining premium queue
		 */
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.premium")))) {
		
			if(profile.getState() == PlayerState.LOBBY && !PartyManager.getManager().isPlayerInParty(player.getUniqueId())) {
				
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				GameType game = GameTypeManager.getManager().getGameType(name);
				
				queue.joinPremium(player, game);
				
				items.premiumQueueItems(player);
				
				profile.setState(PlayerState.IN_QUEUE);
			}
			
			player.closeInventory();
			event.setCancelled(true);
		}
		
		/*
		 * Solo Duel inventorys all the way down
		 */
		
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.duel")))) {
			
			if(profile.getState() == PlayerState.LOBBY) {
				
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				GameType game = GameTypeManager.getManager().getGameType(name);
				
				Player target = Bukkit.getPlayer(DuelCommand.getDuelTarget().get(player.getUniqueId()));
				
				if(target == null) {
					return;
				}
				
				Profile targetProfile = ProfileManager.getManager().getProfileByPlayer(target);
				
				if(targetProfile == null) {
					return;
				}
				
				targetProfile.getDuelRequest().put(player.getUniqueId(), new SendDuel(player.getUniqueId(), target.getUniqueId(), game));
				
				player.closeInventory();
			}
			
			event.setCancelled(true);
		}
		
		/*
		 * Kit editor inventory
		 */
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.editor")))) {
			//send to kit editor
			
			if(profile.getState() == PlayerState.LOBBY) {
				
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				GameType game = GameTypeManager.getManager().getGameType(name);
				
				if(game != null) {
					
					gameSelected.put(player.getName(), game);
					
					profile.setState(PlayerState.EDITOR);
					
					player.getInventory().clear();
					player.getInventory().setArmorContents(null);
					
					player.getInventory().setContents(game.getInv());
					player.getInventory().setArmorContents(game.getArmor());
					
					player.sendMessage(ChatColor.GRAY + "Going into editor mode for " + ChatColor.YELLOW + game.getName());
					
					Practice.getInstance().teleportEditor(player);
					
				}
				
			}
			
			player.closeInventory();
			event.setCancelled(true);
		}
		
		/*
		 * Kit Options Inv
		 */
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.kit-options")))) {
			
			if(profile.getState() == PlayerState.EDITOR) {
				
				if(gameSelected.containsKey(player.getName())) {
					
					String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
					
					GameType game = gameSelected.get(player.getName());
					
					if(name.equalsIgnoreCase("save")) {
						inv.saveKitIventory(player, game);
					} else if(name.equalsIgnoreCase("edit")) {
						inv.editKitInventory(player, game);
					} else if(name.equalsIgnoreCase("delete")) {
						inv.deleteKitInventory(player, game);
					} else if(name.equalsIgnoreCase("cancel")) {
						player.closeInventory();
					}
					
				}
			}
			
			event.setCancelled(true);
		}
		
		/*
		 * Kit Editor options
		 */
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.edit-kit")))) {
			
			if(profile.getState() == PlayerState.EDITOR) {
				
				if(gameSelected.containsKey(player.getName())) {
					
					String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
					
					GameType game = gameSelected.get(player.getName());
					
					KitManager.getManager().loadPlayerKit(player, game, name);
					
				}
			}
			
			player.closeInventory();
			event.setCancelled(true);
		}
		
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.delete-kit")))) {
			
			if(profile.getState() == PlayerState.EDITOR) {
				
				if(gameSelected.containsKey(player.getName())) {
					
					String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
					
					GameType game = gameSelected.get(player.getName());
					
					KitManager.getManager().deletePlayerKit(player, game, name);
					
				}
			}
			
			player.closeInventory();
			event.setCancelled(true);
		}
		
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.kit-editor.save-kit")))) {
			
			if(profile.getState() == PlayerState.EDITOR) {
				
				if(gameSelected.containsKey(player.getName())) {
					
					if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "EMPTY")) {
						
						if(naming.contains(player.getName())) {
							player.sendMessage(ChatColor.GREEN + "You are already ready to name your kit, please type a name in chat");
						} else {
							naming.add(player.getName());
							player.sendMessage(ChatColor.GREEN + "Please type the name of your kit in chat");
						}
						
					} else {
						
						String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
						
						GameType game = gameSelected.get(player.getName());
						
						KitManager.getManager().updatePlayerKit(player, game, name);
						
					}
				}
			}
			
			player.closeInventory();
			event.setCancelled(true);
		}
		
		/*
		 * Creating LMS Inventory
		 */
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.lms")))) {
			
			if(profile.getState() == PlayerState.LOBBY) {
				
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				GameType game = GameTypeManager.getManager().getGameType(name);
				
				if(LMSManager.getLmsList().size() >= 9) {
					player.sendMessage(ChatColor.RED + "Please wait for a spot to open up!");
					return;
				}
				
				if(profile.getToken() <= 0) {
					player.sendMessage(ChatColor.RED + "You don't have enough Event Tokens to start a event!");
					return;
				}
				
				if(Practice.getCooldown() > System.currentTimeMillis()) {
					
					int cooldown = (int) ((Practice.getCooldown() - System.currentTimeMillis()) / 1000);
					
					player.sendMessage(ChatColor.RED + "Please wait " + cooldown + " seconds before starting an Event!");
					player.closeInventory();
					return;
				}
				
				profile.setToken(profile.getToken() - 1);
				
				LMSManager.getManager().start(player, ArenaManager.getManager().randomArena(), game);
				Practice.setCooldown(System.currentTimeMillis() + (60 * 3 * 1000));
			}
			
			player.closeInventory();
			event.setCancelled(true);
		}
		
		/*
		 * Opening up the Inventory that shows all of the current LMS events
		 */
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.events")))) {
			
			if(profile.getState() == PlayerState.LOBBY) {
				
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				if(name.equalsIgnoreCase("lms")) {
					
					player.closeInventory();
					inv.lms(player);
				}
				
				if(name.equalsIgnoreCase("tdm")) {
					
					player.closeInventory();
					tdm.joinTDM(player);
				}
			}
			event.setCancelled(true);
		}
		
		/*
		 * Hosting a event
		 */
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.host")))) {
			
			if(profile.getState() == PlayerState.LOBBY) {
				
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				if(name.equalsIgnoreCase("lms")) {
					
					player.closeInventory();
					inv.createLMS(player);
				}
				
				if(name.equalsIgnoreCase("tdm")) {
					
					player.sendMessage(ChatColor.RED + "This is still being tested and is not available for players to start yet!");
					
					player.closeInventory();
				}
			}
			event.setCancelled(true);
		}
		
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.list-lms")))) {
		
			if(profile.getState() == PlayerState.LOBBY) {
				
				String id = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
			
				try {
					LMSManager.getManager().join(player, Integer.parseInt(id));
					player.closeInventory();
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			
			event.setCancelled(true);
		}
		
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.options")))) {
			
			if(profile.getState() == PlayerState.LOBBY) {
				
				//Resetting Elo
				if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Reset Elo")) {
					
					if(Practice.getInstance().getConfig().getBoolean("allow-elo-reset")) {
						
						int tokens = Practice.getInstance().getConfig().getInt("elo-reset-token");
						
						if(profile.getToken() > tokens) {
						
							profile.setToken(profile.getToken() - tokens);
							
							for(Elo elo : profile.getElo()) {
								elo.setElo(1600);
							}
							
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("elo-reset-message").replace("%player%", player.getName())));
						} else {
							player.sendMessage(ChatColor.RED + "You don't have enough Event Tokens to reset your elo!");
						}
					} else {
						player.sendMessage(ChatColor.RED + "You can't reset your Elo rating on this server!");
					}
				}
				
				//Disable duel request
				if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN.toString() + "Duel Requests")) {
					
					if(profile.isAllowDuel()) {
						profile.setAllowDuel(false);
						
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 10f, 10f);
						player.closeInventory();
						inv.options(player);
						
					} else {
						profile.setAllowDuel(true);
						
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 10f, 10f);
						player.closeInventory();
						inv.options(player);
					}
				}
				
				//Disable Spectator message
				if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN.toString() + "Spectator Message")) {
					
					if(profile.isSpectatorMessage()) {
						profile.setSpectatorMessage(false);
						
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 10f, 10f);
						
						player.closeInventory();
						inv.options(player);
						
					} else {
						profile.setSpectatorMessage(true);
						
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 10f, 10f);
						player.closeInventory();
						inv.options(player);
					}
				}

			}
			
			event.setCancelled(true);
		}
		
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("inventory.tdm")))) {
			
			if(profile.getState() == PlayerState.LOBBY) {
				
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				GameType game = GameTypeManager.getManager().getGameType(name);
				
				if(!TDM.isActive()) {
					TDM.setActive(true);
					TDM.setGame(game);
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Practice.getInstance().getConfig().getString("tdm-start-message")));
				} else {
					player.sendMessage(ChatColor.RED + "Can't start a TDM event, because it is already active!");
				}
				
			}
			
			player.closeInventory();
			event.setCancelled(true);
		}

	}
	
}
