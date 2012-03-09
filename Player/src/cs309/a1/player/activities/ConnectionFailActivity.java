package cs309.a1.player.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import cs309.a1.player.R;

public class ConnectionFailActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.information);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.ConnectFailActivity_title);
	}
}
