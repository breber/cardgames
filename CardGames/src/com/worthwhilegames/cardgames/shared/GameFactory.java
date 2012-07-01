package com.worthwhilegames.cardgames.shared;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.ImageView;

import com.worthwhilegames.cardgames.crazyeights.C8Constants;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightsGameController;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightsPlayerController;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightsTabletGame;
import com.worthwhilegames.cardgames.euchre.EuchreGameController;
import com.worthwhilegames.cardgames.euchre.EuchrePlayerController;
import com.worthwhilegames.cardgames.euchre.EuchreTabletGame;
import com.worthwhilegames.cardgames.gameboard.activities.GameboardActivity;
import com.worthwhilegames.cardgames.shared.connection.ConnectionClient;
import com.worthwhilegames.cardgames.shared.connection.ConnectionServer;

/**
 * A Factory that will help facilitate the retrieval
 * of instances of Games. Instead of having checks all
 * throughout the code trying to figure out which implementation
 * of Game to use, we will do that all in this class.
 */
public class GameFactory {

	/**
	 * Get an instance of an already started Game
	 *
	 * @return the Game instance as specified by the type of game currently specified
	 */
	public static Game getGameInstance(Context ctx) {
		if (getGameType(ctx) == CardGame.CrazyEights) {
			return CrazyEightsTabletGame.getInstance();
		} else if (getGameType(ctx) == CardGame.Euchre) {
			return EuchreTabletGame.getInstance();
		}

		return null;
	}

	/**
	 * Clear the game instance
	 */
	public static void clearGameInstance(Context ctx) {
		if (getGameType(ctx) == CardGame.CrazyEights) {
			CrazyEightsTabletGame.clearInstance();
		}
	}

	/**
	 * Get an instance of a GameController
	 *
	 * @return the GameController instance as specified by the currently specified game type
	 */
	public static GameController getGameControllerInstance(GameboardActivity activity,
			ConnectionServer connectionServer, ImageView refreshButton) {
		if (getGameType(activity) == CardGame.CrazyEights) {
			return new CrazyEightsGameController(activity, connectionServer, refreshButton);
		} else if (getGameType(activity) == CardGame.Euchre) {
			return new EuchreGameController(activity, connectionServer, refreshButton);
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
		if (getGameType(context) == CardGame.CrazyEights) {
			return new CrazyEightsPlayerController(context, playButton,	drawButton, connectionClient, cardHand);
		} else if (getGameType(context) == CardGame.Euchre) {
			return new EuchrePlayerController(context, playButton,	drawButton, connectionClient, cardHand);
		}

		return null;
	}

	/**
	 * Gets the current Game Type
	 *
	 * @return the type of Game we are playing
	 */
	public static CardGame getGameType(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREFERENCES, 0);
		String gameType = prefs.getString(Constants.GAME_TYPE, CardGame.CrazyEights.toString());

		if (CardGame.CrazyEights.toString().equals(gameType)) {
			return CardGame.CrazyEights;
		} else if (CardGame.Euchre.toString().equals(gameType)) {
			return CardGame.Euchre;
		}

		return CardGame.CrazyEights;
	}

	/**
	 * Gets the default maximum number of players for the current game
	 *
	 * @return the type maximum number of players allowed
	 */
	public static int getMaxAllowedPlayers(Context ctx) {
		if (CardGame.CrazyEights.equals(getGameType(ctx))) {
			return C8Constants.MAX_NUM_PLAYERS;
		}

		return Constants.DEFAULT_MAX_PLAYERS;
	}
}
