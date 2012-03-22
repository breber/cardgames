package cs309.a1.shared;


public class Card {
	
	private int suit;
	private int value;
	private int resourceId;
	private int idNum;
	
	public Card(int suit, int value, int resourceId, int idNum) {
		super();
		this.suit = suit;
		this.value = value;
		this.resourceId = resourceId;
		this.idNum = idNum;
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
	
	/**
	 * This is a custom toString method for the transfering of card data
	 * 
	 * @return a string representation of the card: "suit value resourceId idNum "
	 */
	@Override
	public String toString(){
		StringBuilder toReturn = new StringBuilder();
		
		toReturn.append(suit);
		toReturn.append(" ");
		toReturn.append(value);
		toReturn.append(" ");
		toReturn.append(resourceId);
		toReturn.append(" ");
		toReturn.append(idNum);
		toReturn.append(" ");
		
		return toReturn.toString();
	}
}
