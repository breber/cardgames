package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.os.Bundle;
import cs309.a1.gameboard.R;

public class ConnectionFailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disconnectedplayer);

	}
}
