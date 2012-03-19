package cs309.a1.shared;


public class Card {
	
	private String suit;
	private int value;
	private int resourceId;
	
	public Card(String suit, int value, int resourceId) {
		super();
		this.suit = suit;
		this.value = value;
		this.resourceId = resourceId;
	}
	
	public String getSuit() {
		return suit;
	}
	public void setSuit(String suit) {
		this.suit = suit;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getResourceId() {
		return resourceId;
	}
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
		

	

}
