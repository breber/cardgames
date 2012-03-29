package cs309.a1.shared;

import java.util.List;


public interface Game {

	public void setup();
	public void deal();
	public Card draw(Player player);
	public void discard(Player player, Card card);
	public void shuffleDeck();
	public void dropPlayer(Player player);
	public Card getDiscardPileTop();
	public int getNumPlayers();
	public boolean isGameOver(Player player);
	public List<Player> getPlayers();

}
