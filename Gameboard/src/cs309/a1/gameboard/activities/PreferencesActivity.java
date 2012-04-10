package cs309.a1.gameboard.activities;

import cs309.a1.gameboard.R;
import cs309.a1.shared.SoundManager;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PreferencesActivity extends Activity{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.preferencesActivityTitle);
	}
}
