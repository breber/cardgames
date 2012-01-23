package cs309.a1.player;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity {

	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.main);

		Button play = (Button) findViewById(R.id.btPlay);
		Button about = (Button) findViewById(R.id.btAbout);
		Button rules = (Button) findViewById(R.id.btRules);

		rules.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent ruleButtonClick = new Intent(MainMenu.this,
						RulesActivity.class);
				startActivity(ruleButtonClick);
			}
		});

		about.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aboutButtonClick = new Intent(MainMenu.this,
						AboutActivity.class);
				startActivity(aboutButtonClick);
			}
		});

		play.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent playButtonClick = new Intent(MainMenu.this,
						ConnectActivity.class);
				startActivity(playButtonClick);
			}
		});

	}

}
