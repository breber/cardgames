package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import cs309.a1.gameboard.R;

public class RulesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.Rules);

		TextView body = (TextView) findViewById(R.id.informationBody);
		body.setText(R.string.Information_rules);
	}

}
