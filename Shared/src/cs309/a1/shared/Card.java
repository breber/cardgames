package cs309.a1.shared;


public class Card {
	
	private String suit;
	private int value;
	private int resourceId;
	private int idNum;
	
	public Card(String suit, int value, int resourceId, int idNum) {
		super();
		this.suit = suit;
		this.value = value;
		this.resourceId = resourceId;
		this.idNum = idNum;
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
	public int getIdNum() {
		return idNum;
	}
	public void setIdNum(int idNum) {
		this.idNum = idNum;
	}
}
