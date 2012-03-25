package cs309.a1.shared;


public interface Game {

	public void setup();
	public void deal();
	public void draw(Player player);
	public void discard(Player player, Card card);
	public void shuffleDeck();
	public void dropPlayer(Player player);

}
