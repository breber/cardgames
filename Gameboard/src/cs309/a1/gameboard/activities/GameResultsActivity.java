package cs309.a1.gameboard.activities;

import cs309.a1.gameboard.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GameResultsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);

		TextView title = (TextView) findViewById(R.id.informationTitle);
		title.setText("Game Results");
	}
}
