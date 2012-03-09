package cs309.a1.shared.activities;

/**
 * Represents a separator in the ListView
 */
public class ListSeparator extends ListItem {

	private String title;

	/**
	 * Creates a new Separator for the ListView with the given title
	 * 
	 * @param title
	 */
	public ListSeparator(String title) {
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.activities.ListItem#getType()
	 */
	@Override
	public ItemType getType() {
		return ItemType.SEPARATOR;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return title;
	}
}
