package cs309.a1.shared;

import java.util.List;

import cs309.a1.crazyeights.CrazyEightsTabletGame;

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
	public static Game getGameInstance(List<Player> players, Deck deck, Rules rules) {
		if (getGameType() == CardGame.CRAZY_EIGHTS) {
			return CrazyEightsTabletGame.getInstance(players, deck, rules);
		}

		return null;
	}

	/**
	 * Get an instance of an already started Game
	 * 
	 * @return the Game instance as specified by the type of game currently specified
	 */
	public static Game getGameInstance() {
		if (getGameType() == CardGame.CRAZY_EIGHTS) {
			return CrazyEightsTabletGame.getInstance();
		}

		return null;
	}

	/**
	 * Get an instance of a GameController
	 * 
	 * @return the GameController instance as specified by the currently specified game type
	 */
	public static GameController getGameControllerInstance() {
		if (getGameType() == CardGame.CRAZY_EIGHTS) {
			// TODO: this might need to be moved to the shared project...
			//			return CrazyEightsGameController.getInstance();
		}

		return null;
	}

	/**
	 * Get an instance of a PlayerController
	 * 
	 * @return the PlayerController instance as specified by the currently specified game type
	 */
	public static PlayerController getPlayerControllerInstance() {
		if (getGameType() == CardGame.CRAZY_EIGHTS) {
			// TODO: this might need to be moved to the shared project...
			//			return CrazyEightsPlayerController.getInstance();
		}

		return null;
	}

	/**
	 * Gets the current Game Type
	 *
	 * @return the type of Game we are playing
	 */
	public static CardGame getGameType() {
		return CardGame.CRAZY_EIGHTS;
	}
}
