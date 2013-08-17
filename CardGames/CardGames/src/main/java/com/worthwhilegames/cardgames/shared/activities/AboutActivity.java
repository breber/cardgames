package com.worthwhilegames.cardgames.shared.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.AdActivity;

/**
 * This activity will display the About screen
 */
public class AboutActivity extends AdActivity {

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
		body.setText(R.string.about_text_gameboard);
	}
}
