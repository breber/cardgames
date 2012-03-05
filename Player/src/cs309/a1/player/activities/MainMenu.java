package cs309.a1.player.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cs309.a1.player.R;

public class MainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button play = (Button) findViewById(R.id.btPlay);
		Button about = (Button) findViewById(R.id.btAbout);
		Button rules = (Button) findViewById(R.id.btRules);

		rules.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent ruleButtonClick = new Intent(MainMenu.this, RulesActivity.class);
				startActivity(ruleButtonClick);
			}
		});

		about.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent aboutButtonClick = new Intent(MainMenu.this,	AboutActivity.class);
				startActivity(aboutButtonClick);
			}
		});

		play.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent playButtonClick = new Intent(MainMenu.this, ConnectActivity.class);
				startActivity(playButtonClick);
			}
		});
	}

}
