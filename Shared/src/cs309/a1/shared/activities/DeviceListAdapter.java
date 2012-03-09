package cs309.a1.shared.activities;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

/**
 * A specialized ListAdapter that allows us to show data in a ListView,
 * using a specific layout for each row.
 */
public class DeviceListAdapter extends ArrayAdapter<ListItem> implements ListAdapter {


	public DeviceListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#areAllItemsEnabled()
	 */
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#isEnabled(int)
	 */
	@Override
	public boolean isEnabled(int position) {
		return this.getItem(position).getType() != ListItem.ItemType.SEPARATOR;
	}
}