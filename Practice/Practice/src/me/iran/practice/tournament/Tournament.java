package me.iran.practice.tournament;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import lombok.Getter;
import lombok.Setter;
import me.iran.practice.Practice;
import me.iran.practice.arena.ArenaManager;
import me.iran.practice.duel.SoloDuel;
import me.iran.practice.duel.SoloDuelManager;
import me.iran.practice.gametype.GameType;
import net.md_5.bungee.api.ChatColor;

@Getter
@Setter
public class Tournament {

	private ArrayList<TPlayer> players;

	private int round;

	@Getter
	@Setter
	private static int ID;

	private GameType game;

	private TournamentState state;

	public Tournament(GameType game) {

		ID++;

		this.game = game;

		round = 1;

		players = new ArrayList<>();

		state = TournamentState.WAITING;
	}

	/*
	 * Need to check if any of the players are null, if they are they should advance
	 * or get removed
	 */

	public void createMatch() {

		for (int i = 0; i < players.size(); i++) {

			TPlayer tplayer = players.get(i);

			// Second player is null

			if (tplayer.getPlayer() != null) {

				// Odd number

				if ((tplayer.getPosition() % 2) != 0) {

					TPlayer tplayer2 = null;
							
					if((i + 1) < players.size()) {
						tplayer2 = players.get(i + 1);
					}

					// Second player is null, send player1 to next round

					if (tplayer2 == null) {
						tplayer.getPlayer().sendMessage(ChatColor.GOLD + "Your opponent could not be found, advancing to the next round!");
						continue;
					}

					if (tplayer2.getPlayer() == null) {
						players.remove(tplayer2);

						tplayer.getPlayer().sendMessage(
								ChatColor.GOLD + "Your opponent could not be found, advancing to the next round!");
						continue;
					}

					// Both players are valid, start game

					SoloDuelManager.getManager().createSoloDuel(tplayer.getPlayer(), tplayer2.getPlayer(), game,
							ArenaManager.getManager().randomArena());

				}

				// First player is null

			} else if (tplayer.getPlayer() == null) {

				if ((tplayer.getPosition() % 2) != 0) {

					TPlayer tplayer2 = players.get(i + 1);

					if (tplayer2 != null) {
						players.remove(tplayer);

						tplayer2.getPlayer().sendMessage(
								ChatColor.GOLD + "Your opponent could not be found, advancing to the next round!");
						continue;
					}

					SoloDuelManager.getManager().createSoloDuel(tplayer.getPlayer(), tplayer2.getPlayer(), game,
							ArenaManager.getManager().randomArena());

				}

			} else if (tplayer.getPlayer() == null) {

				if ((tplayer.getPosition() % 2) != 0) {

					TPlayer tplayer2 = players.get(i + 1);

					if (tplayer2 == null) {
						players.remove(tplayer);

						players.remove(tplayer2);
						continue;
					}

				}

			}

			/*
			 * if((player.getPosition() % 2) != 0) {
			 * 
			 * TPlayer player2 = advancing.get(i + 1);
			 * 
			 * if(player2 == null) { player.getPlayer().sendMessage(ChatColor.GOLD +
			 * "Your opponent could not be found, advancing to the next round!");
			 * advancing.remove(player2); continue; }
			 * 
			 * SoloDuelManager.getManager().createSoloDuel(player.getPlayer(),
			 * player2.getPlayer(), game, ArenaManager.getManager().randomArena());
			 * 
			 * advancing.remove(player); players.add(player); }
			 */
		}

		if (advance()) {

			if (this.getPlayers().size() == 1) {
				Bukkit.broadcastMessage(ChatColor.AQUA + this.getPlayers().get(0).getPlayer().getName() + ChatColor.DARK_AQUA + " has won the tournament!");
				this.getPlayers().clear();
				TournamentManager.getManager().getTournaments().remove(this);
				return;
			}

			this.setState(TournamentState.TRANSITION);

			Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {

				public void run() {
					setState(TournamentState.ACTIVE);
					setRound(getRound() + 1);

					createMatch();

					Bukkit.broadcastMessage(ChatColor.GREEN + "Tournament is now advancing to round " + ChatColor.BLUE + getRound());
				}

			}, 20 * 5);

		}
	}

	public void updatedPostion() {
		for (int i = 0; i < players.size(); i++) {

			TPlayer player = players.get(i);

			player.setPosition(i + 1);
		}

		System.out.println("updated positions");
	}

	public boolean advance() {

		for (SoloDuel duel : SoloDuelManager.getDuelSet()) {

			for (TPlayer player : this.getPlayers()) {
				if (duel.getPlayer1().getName().equalsIgnoreCase(player.getPlayer().getName())
						|| duel.getPlayer2().getName().equalsIgnoreCase(player.getPlayer().getName())) {
					return false;
				}
			}

		}

		return true;

	}

}
