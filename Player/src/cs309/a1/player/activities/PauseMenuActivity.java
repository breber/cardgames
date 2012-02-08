package cs309.a1.player.activities;

import cs309.a1.player.R;
import cs309.a1.player.R.id;
import cs309.a1.player.R.layout;
import cs309.a1.player.R.string;
import android.app.Activity;
import android.widget.TextView;

public class PauseMenuActivity extends Activity{
	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.information);

		TextView title = (TextView) findViewById(R.id.informationTitle);
		title.setText(R.string.PauseActivity_title);
		
	}
}
