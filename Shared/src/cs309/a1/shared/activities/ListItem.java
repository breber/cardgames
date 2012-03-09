package cs309.a1.shared.activities;

public abstract class ListItem {

	/**
	 * Gets the type of this ListItem
	 * 
	 * @return
	 * A value from the enum ItemType
	 */
	public abstract ItemType getType();

	/**
	 * Represents whether this item is a separator or not
	 */
	public static enum ItemType {
		SEPARATOR, NOT_SEPARATOR;
	}
}
