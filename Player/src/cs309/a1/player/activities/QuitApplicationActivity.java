package cs309.a1.player.activities;

import android.app.Activity;
import android.widget.TextView;
import cs309.a1.player.R;

public class QuitApplicationActivity extends Activity{
	//Josh was not here
	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.information);

		TextView title = (TextView) findViewById(R.id.informationTitle);
		title.setText(R.string.QuitApplicationActivity_title);

	}
}
