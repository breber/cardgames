package cs309.a1.gameboard.activities;

import cs309.a1.gameboard.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);

		TextView title = (TextView) findViewById(R.id.informationTitle);
		title.setText(R.string.About);

		TextView body = (TextView) findViewById(R.id.informationBody);
		body.setText(R.string.Information_about);
	}

}
