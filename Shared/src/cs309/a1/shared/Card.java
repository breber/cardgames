package cs309.a1.shared;

import android.graphics.Bitmap;

public class Card {
	
	private int suit;
	private int value;
	private Bitmap cardImage;
	
	public Card(int suit, int value, Bitmap cardImage) {
		super();
		this.suit = suit;
		this.value = value;
		this.cardImage = cardImage;
	}
	
	public int getSuit() {
		return suit;
	}
	public void setSuit(int suit) {
		this.suit = suit;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Bitmap getCardImage() {
		return cardImage;
	}
	public void setCardImage(Bitmap cardImage) {
		this.cardImage = cardImage;
	}
}
