package cs309.a1.gameboard;

import android.app.Activity;
import android.widget.TextView;

public class PauseMenuActivity extends Activity {

	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.information);

		TextView title = (TextView) findViewById(R.id.informationTitle);
		title.setText("Game Paused");
	}

}
