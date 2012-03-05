package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cs309.a1.gameboard.R;

public class GameboardActivity extends Activity {

	private static final int QUIT_GAME = "QUIT_GAME".hashCode();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameboard);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, QuitGameActivity.class);
		startActivityForResult(intent, QUIT_GAME);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == QUIT_GAME) {
			if (resultCode == RESULT_OK) {
				// Start the Main Menu
				Intent intent = new Intent(GameboardActivity.this, MainMenu.class);
				startActivity(intent);

				// Finish this activity
				finishActivity(RESULT_OK);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
