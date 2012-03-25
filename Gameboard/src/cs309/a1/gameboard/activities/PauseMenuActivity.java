package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cs309.a1.gameboard.R;

public class PauseMenuActivity extends Activity {
	
	private static final int MAIN_MENU = Math.abs("MAIN_MENU".hashCode());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pausemenu);
		
		Button rules = (Button) findViewById(R.id.btRules);

		rules.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent ruleButtonClick = new Intent(PauseMenuActivity.this, RulesActivity.class);
				startActivity(ruleButtonClick);
			}
		});
		
		Button resume = (Button) findViewById(R.id.btResume);

		resume.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		Button mainMenu = (Button) findViewById(R.id.btMainMenu);

		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mainMenu = new Intent(PauseMenuActivity.this, QuitGameActivity.class);
				startActivityForResult(mainMenu, MAIN_MENU);
			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MAIN_MENU && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}

}
