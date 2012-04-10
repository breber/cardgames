package cs309.a1.player.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import cs309.a1.player.R;
import cs309.a1.shared.SoundManager;

/**
 * This activity will display the Game Rules
 */
public class RulesActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);

		// Update the title to "Rules"
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.rulesActivityTitle);

		// Update the body text to the Rules
		TextView body = (TextView) findViewById(R.id.informationBody);
		body.setText(R.string.game_rules_text);
	}
}
