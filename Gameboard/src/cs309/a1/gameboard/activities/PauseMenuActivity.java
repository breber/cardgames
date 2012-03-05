package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import cs309.a1.gameboard.R;

public class PauseMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pausemenu);

		TextView title = (TextView) findViewById(R.id.informationTitle);
		title.setText("Game Paused");
	}

}
