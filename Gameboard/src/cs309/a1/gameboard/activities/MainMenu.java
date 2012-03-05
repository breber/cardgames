package cs309.a1.gameboard.activities;

import cs309.a1.gameboard.R;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity {

//This will run at the start of the app
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button play = (Button) findViewById(R.id.btPlay);
		Button about = (Button) findViewById(R.id.btAbout);
		Button rules = (Button) findViewById(R.id.btRules);

		rules.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent ruleButtonClick = new Intent(MainMenu.this, RulesActivity.class);
				startActivity(ruleButtonClick);
			}
		});

		about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent aboutButtonClick = new Intent(MainMenu.this,	AboutActivity.class);
				startActivity(aboutButtonClick);
			}
		});

		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent playButtonClick = new Intent(MainMenu.this, ConnectActivity.class);
				startActivity(playButtonClick);
			}
		});

	}

}
