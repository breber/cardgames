package cs309.a1.shared.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import cs309.a1.R;

/**
 * This activity will display the About screen
 */
public class AboutActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);

		// Update the title to "About"
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.aboutActivityTitle);

		// Update the body text to the about text
		TextView body = (TextView) findViewById(R.id.informationBody);

		// TODO: update about text so that it covers both gameboard and player
		body.setText(R.string.about_text_gameboard);
	}
}