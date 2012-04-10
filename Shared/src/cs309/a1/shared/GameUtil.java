package cs309.a1.shared;

import java.util.List;

import cs309.a1.crazyeights.CrazyEightsTabletGame;

public class GameUtil {

	public static Game getGameInstance(List<Player> players, Deck deck, Rules rules) {
		if (getGameType() == CardGame.CRAZY_EIGHTS) {
			return CrazyEightsTabletGame.getInstance(players, deck, rules);
		}

		return null;
	}

	public static Game getGameInstance() {
		if (getGameType() == CardGame.CRAZY_EIGHTS) {
			return CrazyEightsTabletGame.getInstance();
		}

		return null;
	}

	public static GameController getGameControllerInstance() {
		if (getGameType() == CardGame.CRAZY_EIGHTS) {
			// TODO: this might need to be moved to the shared project...
//			return CrazyEightsGameController.getInstance();
		}

		return null;
	}

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
