package cs309.a1.shared;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.ImageView;
import cs309.a1.crazyeights.CrazyEightsGameController;
import cs309.a1.crazyeights.CrazyEightsPlayerController;
import cs309.a1.crazyeights.CrazyEightsTabletGame;
import cs309.a1.gameboard.activities.GameboardActivity;
import cs309.a1.shared.connection.ConnectionClient;
import cs309.a1.shared.connection.ConnectionServer;

/**
 * A Factory that will help facilitate the retrieval
 * of instances of Games. Instead of having checks all
 * throughout the code trying to figure out which implementation
 * of Game to use, we will do that all in this class.
 */
public class GameFactory {

	/**
	 * Get an instance of a Game
	 *
	 * @param players - the players to use for the game
	 * @param deck - the deck of cards to use
	 * @param rules - the rules definitions
	 * @return the Game instance as specified by the type of game currently specified
	 */
	public static Game getGameInstance(Context ctx, List<Player> players, Deck deck, Rules rules) {
		if (getGameType(ctx) == CardGame.CRAZY_EIGHTS) {
			return CrazyEightsTabletGame.getInstance(players, deck, rules);
		}

		return null;
	}

	/**
	 * Get an instance of an already started Game
	 *
	 * @return the Game instance as specified by the type of game currently specified
	 */
	public static Game getGameInstance(Context ctx) {
		if (getGameType(ctx) == CardGame.CRAZY_EIGHTS) {
			return CrazyEightsTabletGame.getInstance();
		}

		return null;
	}

	/**
	 * Get an instance of a GameController
	 *
	 * @return the GameController instance as specified by the currently specified game type
	 */
	public static GameController getGameControllerInstance(GameboardActivity activity,
			ConnectionServer connectionServer, List<Player> players, ImageView refreshButton) {
		if (getGameType(activity) == CardGame.CRAZY_EIGHTS) {
			return new CrazyEightsGameController(activity, connectionServer, players, refreshButton);
		}

		return null;
	}

	/**
	 * Get an instance of a PlayerController
	 *
	 * @return the PlayerController instance as specified by the currently specified game type
	 */
	public static PlayerController getPlayerControllerInstance(Context context, Button playButton,
			Button drawButton, ConnectionClient connectionClient, ArrayList<Card> cardHand) {
		if (getGameType(context) == CardGame.CRAZY_EIGHTS) {
			return new CrazyEightsPlayerController(context, playButton,	drawButton, connectionClient, cardHand);
		}

		return null;
	}

	/**
	 * Gets the current Game Type
	 *
	 * @return the type of Game we are playing
	 */
	public static CardGame getGameType(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREFERENCES, Context.MODE_WORLD_READABLE);
		String gameType = prefs.getString(Constants.GAME_TYPE, Constants.CRAZY_EIGHTS);

		if (Constants.CRAZY_EIGHTS.equals(gameType)) {
			return CardGame.CRAZY_EIGHTS;
		}


		return CardGame.CRAZY_EIGHTS;
	}
}
