package me.iran.practice;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.arena.listeners.ArenaEditListeners;
import me.iran.practice.cmd.AcceptCommand;
import me.iran.practice.cmd.ArenaCommands;
import me.iran.practice.cmd.DenyCommand;
import me.iran.practice.cmd.DuelCommand;
import me.iran.practice.cmd.EditCommand;
import me.iran.practice.cmd.EditorCommand;
import me.iran.practice.cmd.EloCommands;
import me.iran.practice.cmd.EventCommand;
import me.iran.practice.cmd.GameTypeCommands;
import me.iran.practice.cmd.InventoryCommand;
import me.iran.practice.cmd.PartyCommands;
import me.iran.practice.cmd.PremiumCommands;
import me.iran.practice.cmd.SpawnCommand;
import me.iran.practice.cmd.SpectateCommand;
import me.iran.practice.cmd.TokenCommands;
import me.iran.practice.duel.listeners.DisconnectInDuel;
import me.iran.practice.duel.listeners.DropItemInDuel;
import me.iran.practice.duel.listeners.DuelDamageEvent;
import me.iran.practice.duel.listeners.DuelDeathEvent;
import me.iran.practice.duel.listeners.DuelEndEvent;
import me.iran.practice.duel.listeners.DuelStartEvent;
import me.iran.practice.duel.team.listeners.DropItemInTeamDuel;
import me.iran.practice.duel.team.listeners.TeamDeathEvent;
import me.iran.practice.duel.team.listeners.TeamDisconnectEvent;
import me.iran.practice.duel.team.listeners.TeamDuelEndEvent;
import me.iran.practice.duel.team.listeners.TeamDuelStartEvent;
import me.iran.practice.events.lms.LMSCommands;
import me.iran.practice.events.lms.LMSListeners;
import me.iran.practice.events.tdm.TDM;
import me.iran.practice.events.tdm.TDMCommands;
import me.iran.practice.events.tdm.TDMManager;
import me.iran.practice.gametype.GameTypeManager;
import me.iran.practice.kit.KitEditor;
import me.iran.practice.kit.KitSetup;
import me.iran.practice.listeners.CancelBlockEvents;
import me.iran.practice.listeners.CancelPickUpEvent;
import me.iran.practice.listeners.ClickListeners;
import me.iran.practice.listeners.CloseGameTypeChest;
import me.iran.practice.listeners.DamageListener;
import me.iran.practice.listeners.EnderpearlCooldown;
import me.iran.practice.listeners.InteractListeners;
import me.iran.practice.listeners.LoadKitEvent;
import me.iran.practice.listeners.PlayerQuit;
import me.iran.practice.listeners.RespawnEvent;
import me.iran.practice.listeners.StopHungerEvent;
import me.iran.practice.listeners.TeleportSpawnOnJoin;
import me.iran.practice.party.PartyManager;
import me.iran.practice.party.listeners.PartyClickListeners;
import me.iran.practice.party.listeners.PartyCreateEvent;
import me.iran.practice.party.listeners.PartyDisconnectEvent;
import me.iran.practice.profile.ProfileManager;
import me.iran.practice.profile.ProfileSetup;
import me.iran.practice.runnable.BoardRunnable;
import me.iran.practice.runnable.DuelRunnable;
import me.iran.practice.runnable.EventsRunnable;
import me.iran.practice.runnable.InventoryRunnables;
import me.iran.practice.runnable.QueueRunnable;
import me.iran.practice.scoreboard.ScoreboardEvents;
import me.iran.practice.tournament.TournamentCommands;
import me.iran.practice.tournament.TournamentRunnable;
import me.iran.practice.tournament.listeners.TDuelEndEvent;
import me.iran.practice.tournament.listeners.TournamentDisconnect;
import me.iran.practice.utils.PlayerItems;

public class Practice extends JavaPlugin {
	
	private PlayerItems items = new PlayerItems();
	
    private static Practice instance;

    @Getter
    @Setter
    public static Long cooldown;
    
    private QueueRunnable soloQueue = new QueueRunnable();
    private DuelRunnable duelRunnable = new DuelRunnable();
    private InventoryRunnables invRunnable = new InventoryRunnables();
    private EventsRunnable eventRunnable = new EventsRunnable();
    private BoardRunnable boardRunnable = new BoardRunnable();
    private TournamentRunnable tournamentRunnable = new TournamentRunnable();
    
    private TDMManager tdmRunnable = new TDMManager();
    
    private TDM tdm = new TDM();
    
    public void onEnable() {

    	getConfig().options().copyDefaults(true);
    	saveConfig();
        instance = this;

        loadListeners();
        loadCommands();
        loadProfiles();
        
        ArenaManager.getManager().loadArenas();
        GameTypeManager.getManager().loadGameTypes();
        
        soloQueue.runTaskTimer(this, 0L, 20L);
        duelRunnable.runTaskTimer(this, 0L, 20L);
        invRunnable.runTaskTimer(this, 0L, 20L);
        eventRunnable.runTaskTimer(this, 0L, 20L);
        tdmRunnable.runTaskTimer(this, 0L, 20L);
        boardRunnable.runTaskTimer(this, 0L, 20L);
        tournamentRunnable.runTaskTimer(this, 0L, 20L);
        
        cooldown = 0L;
        
        tdm.loadTDM();
    }
    
    public void onDisable() {
    	saveProfiles();
    	ArenaManager.getManager().saveArenas();
    	GameTypeManager.getManager().saveGameTypes();
    	
    	tdm.saveFile();
    }

    public static Practice getInstance() {
        return instance;
    }
    
    /*
     * Load profiles on reload 
     */
    @SuppressWarnings("deprecation")
	private void loadProfiles() {
    	for(final Player player : Bukkit.getServer().getOnlinePlayers()) {
    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    			public void run() {
    				ProfileManager.getManager().loadProfile(player);
    			}
    		}, 5);
    	}
    }
    
    /*
     * Save profiles on reload
     */
    @SuppressWarnings("deprecation")
	private void saveProfiles() {
    	for(Player player : Bukkit.getServer().getOnlinePlayers()) {
    		ProfileManager.getManager().saveProfile(player);
    	}
    }

    /*
     * Load listeners
     */
    private void loadListeners() {
    	Bukkit.getServer().getPluginManager().registerEvents(new ProfileSetup(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new ArenaEditListeners(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new TeleportSpawnOnJoin(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new KitSetup(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new KitEditor(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new InteractListeners(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new ClickListeners(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new DuelStartEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new DuelEndEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new DuelDeathEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new DisconnectInDuel(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new DropItemInDuel(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new LoadKitEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new DamageListener(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new CloseGameTypeChest(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new CancelBlockEvents(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new PartyCreateEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new TeamDuelStartEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new PartyClickListeners(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new TeamDeathEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new TeamDuelEndEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new TeamDisconnectEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new StopHungerEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new PartyDisconnectEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new DropItemInTeamDuel(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new CancelPickUpEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new EnderpearlCooldown(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new LMSListeners(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new RespawnEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new DuelDamageEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new TDMManager(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new ScoreboardEvents(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new TDuelEndEvent(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new TournamentDisconnect(), this);
    }
    
    private void loadCommands() {
    	getCommand("arena").setExecutor(new ArenaCommands());
    	getCommand("gametype").setExecutor(new GameTypeCommands());
    	getCommand("duel").setExecutor(new DuelCommand());
    	getCommand("accept").setExecutor(new AcceptCommand());
     	getCommand("deny").setExecutor(new DenyCommand());
    	getCommand("premium").setExecutor(new PremiumCommands());
    	getCommand("token").setExecutor(new TokenCommands());
    	getCommand("setspawn").setExecutor(new SpawnCommand());
    	getCommand("seteditor").setExecutor(new EditorCommand());
    	getCommand("party").setExecutor(new PartyCommands());
    	getCommand("spectate").setExecutor(new SpectateCommand());
    	getCommand("lms").setExecutor(new LMSCommands());
    	getCommand("_").setExecutor(new InventoryCommand());
    	getCommand("elo").setExecutor(new EloCommands());
    	getCommand("host").setExecutor(new EventCommand());
       	getCommand("tdm").setExecutor(new TDMCommands());
       	getCommand("edit").setExecutor(new EditCommand());
     	getCommand("tournament").setExecutor(new TournamentCommands());
    }

    @SuppressWarnings("deprecation")
	public void teleportSpawn(final Player player) {
    	
    		double x = getConfig().getDouble("spawn.x");
    		double y = getConfig().getDouble("spawn.y");
    		double z = getConfig().getDouble("spawn.z");
    		float pitch = (float) getConfig().getDouble("spawn.pitch");
    		float yaw = (float) getConfig().getDouble("spawn.yaw");
    		String world = getConfig().getString("spawn.world");
    		
    		final Location loc = new Location(Bukkit.getWorld(world), x, y, z);
    		loc.setPitch(pitch);
    		loc.setYaw(yaw);
    		
    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    			
    			public void run() {
    				player.teleport(loc);
    			}
    			
    		}, 3);
    		
    		for(PotionEffect effect : player.getActivePotionEffects()) {
    			player.removePotionEffect(effect.getType());
    		}
    		
    		
    		if(PartyManager.getManager().isPlayerInParty(player.getUniqueId())) {
    			
    			if(PartyManager.getManager().getPartyByPlayer(player.getUniqueId()).getLeader() == player.getUniqueId()) {
    				items.partyItemsLeader(player);
    			} else {
    				items.partyItemsMember(player);
    			}
    			
    		} else {
    			items.spawnItems(player);
    		}
    		
    		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
    			
    			player.showPlayer(p);
    			p.showPlayer(player);
    			
    		}
    		
    		player.setHealth(20.0);
    		player.setFoodLevel(20);
    		
    		player.setGameMode(GameMode.SURVIVAL);
    		player.setAllowFlight(false);
    }
    
    public void teleportEditor(Player player) {
    	
		double x = getConfig().getDouble("editor.x");
		double y = getConfig().getDouble("editor.y");
		double z = getConfig().getDouble("editor.z");
		float pitch = (float) getConfig().getDouble("editor.pitch");
		float yaw = (float) getConfig().getDouble("editor.yaw");
		String world = getConfig().getString("editor.world");
		
		Location loc = new Location(Bukkit.getWorld(world), x, y, z);
		loc.setPitch(pitch);
		loc.setYaw(yaw);
		
		player.teleport(loc);
		
		player.setHealth(20.0);
		player.setFoodLevel(20);
    }
}
