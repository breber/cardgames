package com.worthwhilegames.cardgames.shared;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.crazyeights.C8Constants;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightsGameController;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightsPlayerController;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightsTabletGame;
import com.worthwhilegames.cardgames.euchre.EuchreConstants;
import com.worthwhilegames.cardgames.euchre.EuchreGameController;
import com.worthwhilegames.cardgames.euchre.EuchrePlayerController;
import com.worthwhilegames.cardgames.euchre.EuchreTabletGame;
import com.worthwhilegames.cardgames.gameboard.activities.GameboardActivity;
import com.worthwhilegames.cardgames.shared.connection.ConnectionServer;

/**
 * A Factory that will help facilitate the retrieval
 * of instances of Games. Instead of having checks all
 * throughout the code trying to figure out which implementation
 * of Game to use, we will do that all in this class.
 */
public class GameFactory {

	/**
	 * The current game type, if one has been specified
	 */
	private static CardGame mGameType = null;

	/**
	 * Get an instance of an already started Game
	 *
	 * @return the Game instance as specified by the type of game currently specified
	 */
	public static Game getGameInstance(Activity ctx) {
		if (CardGame.CrazyEights.equals(getGameType(ctx))) {
			return CrazyEightsTabletGame.getInstance();
		} else if (CardGame.Euchre.equals(getGameType(ctx))) {
			return EuchreTabletGame.getInstance();
		}

		return null;
	}

	/**
	 * Clear the game instance
	 */
	public static void clearGameInstance(Activity ctx) {
		if (CardGame.CrazyEights.equals(getGameType(ctx))) {
			CrazyEightsTabletGame.clearInstance();
		} else if (CardGame.Euchre.equals(getGameType(ctx))) {
			EuchreTabletGame.clearInstance();
		}
	}

	/**
	 * Get an instance of a GameController
	 *
	 * @return the GameController instance as specified by the currently specified game type
	 */
	public static GameController getGameControllerInstance(GameboardActivity activity,
			ConnectionServer connectionServer) {
		if (CardGame.CrazyEights.equals(getGameType(activity))) {
			return new CrazyEightsGameController(activity, connectionServer);
		} else if (CardGame.Euchre.equals(getGameType(activity))) {
			return new EuchreGameController(activity, connectionServer);
		}

		return null;
	}

	/**
	 * Get an instance of a PlayerController
	 *
	 * @return the PlayerController instance as specified by the currently specified game type
	 */
	public static PlayerController getPlayerControllerInstance(Activity activity, ArrayList<Card> cardHand) {
		if (CardGame.CrazyEights.equals(getGameType(activity))) {
			return new CrazyEightsPlayerController(activity, cardHand);
		} else if (CardGame.Euchre.equals(getGameType(activity))) {
			return new EuchrePlayerController(activity, cardHand);
		}

		return null;
	}

	/**
	 * Gets the current Game Type
	 *
	 * @return the type of Game we are playing
	 */
	public static CardGame getGameType(Context ctx) {
		if (mGameType != null) {
			return mGameType;
		}

		SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREFERENCES, 0);
		String gameType = prefs.getString(Constants.PREF_GAME_TYPE, CardGame.CrazyEights.toString());

		if (CardGame.CrazyEights.toString().equals(gameType)) {
			return CardGame.CrazyEights;
		} else if (CardGame.Euchre.toString().equals(gameType)) {
			return CardGame.Euchre;
		}

		return CardGame.CrazyEights;
	}

	/**
	 * Change the Game Type at runtime, ignoring any preferences
	 * 
	 * @param cardGame the new game type
	 */
	public static void setGameType(CardGame cardGame) {
		mGameType = cardGame;
	}

	/**
	 * Sets the current game type based on the port number used to connect
	 * 
	 * @param portNumber
	 */
	public static void setGameTypeBasedOnPort(int portNumber) {
		if (1234 == portNumber) {
			mGameType = CardGame.CrazyEights;
		} else if (1233 == portNumber) {
			mGameType = CardGame.Euchre;
		}
	}

	/**
	 * Clear the previously stored game type, and fall back to using
	 * the preferences
	 */
	public static void clearGameType() {
		mGameType = null;
	}

	/**
	 * Gets the default maximum number of players for the current game
	 *
	 * @return the type maximum number of players allowed
	 */
	public static int getMaxAllowedPlayers(Activity ctx) {
		if (CardGame.CrazyEights.equals(getGameType(ctx))) {
			return C8Constants.MAX_NUM_PLAYERS;
		} else if (CardGame.Euchre.equals(getGameType(ctx))) {
			return EuchreConstants.MAX_NUM_PLAYERS;
		}

		return Constants.DEFAULT_MAX_PLAYERS;
	}

	/**
	 * Gets the default maximum number of players for the current game
	 *
	 * @return the type maximum number of players allowed
	 */
	public static int getPlayerButtonViewLayout(Activity ctx) {
		if (CardGame.CrazyEights.equals(getGameType(ctx))) {
			return R.layout.genericbuttons;
		} else if (CardGame.Euchre.equals(getGameType(ctx))) {
			return R.layout.euchrebuttons;
		}

		return R.layout.genericbuttons;
	}

	/**
	 * Gets the resource ID of the text to display for the rules screen
	 *
	 * @return the resource id of the text to display
	 */
	public static int getGameRulesText(Activity ctx) {
		if (CardGame.CrazyEights.equals(getGameType(ctx))) {
			return R.string.crazy_eight_game_rules_text;
		} else if (CardGame.Euchre.equals(getGameType(ctx))) {
			return R.string.euchre_game_rules_text;
		}

		return R.string.crazy_eight_game_rules_text;
	}

	/**
	 * Get the number of players required in order to play the game
	 * 
	 * @param ctx
	 * @return the minimum number of players required to play the game
	 */
	public static int getRequiredNumPlayers(Activity ctx) {
		if (CardGame.CrazyEights.equals(getGameType(ctx))) {
			// Crazy Eights needs at least 2 players
			return 2;
		} else if (CardGame.Euchre.equals(getGameType(ctx))) {
			// Euchre needs 4 players
			return 4;
		}

		return 2;
	}

	/**
	 * Get the port number to use for each Game
	 * 
	 * @param ctx
	 * @return the game specific port number
	 */
	public static int getPortNumber(Context ctx) {
		if (CardGame.CrazyEights.equals(getGameType(ctx))) {
			return 1234;
		} else if (CardGame.Euchre.equals(getGameType(ctx))) {
			// Euchre needs 4 players
			return 1233;
		}

		return 1234;
	}
}
